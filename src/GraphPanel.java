import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class GraphPanel extends JPanel {
    private boolean interactivityEnabled = false;
    @SuppressWarnings("unused")
    private boolean edgeDirectionEnabled;
    private boolean isDirected = false;
    private final List<Node> nodes;    // List to store nodes
    private final List<Edge> edges;    // List to store edges
    private Node selectedNode = null;  // Keeps track of the first node selected to form an edge

    private static final int NODE_RADIUS = 10; // Reduced radius for smaller nodes
    private final List<HighlightedPath> highlightedPaths;

    public GraphPanel() {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
        highlightedPaths = new ArrayList<>();
        setBackground(Color.WHITE);

        // Set up mouse listener for creating nodes and edges
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!interactivityEnabled) {
                    JOptionPane.showMessageDialog(GraphPanel.this,
                        "Please select a graph type before interacting with the graph.",
                        "Action Disabled",
                        JOptionPane.WARNING_MESSAGE);
                    return; // Exit if interactivity is disabled
                }
        
                Node clickedNode = getNodeAtPosition(e.getX(), e.getY());
        
                // Check if the user clicked on an existing node
                if (clickedNode != null) {
                    if (selectedNode != null && selectedNode != clickedNode) {
                        if (!edgeExists(selectedNode, clickedNode)) {
                            createEdge(selectedNode, clickedNode);
                        } else {
                            JOptionPane.showMessageDialog(GraphPanel.this, 
                                "Edge already exists between these nodes.", 
                                "Edge Exists", 
                                JOptionPane.INFORMATION_MESSAGE);
                        }
                        selectedNode = null;
                    } else {
                        selectedNode = clickedNode;
                    }
                } else {
                    String nodeName = getNodeNameFromUser();
                    if (nodeName != null && !nodeName.isEmpty() && isUniqueNodeName(nodeName) && isPositionAvailable(e.getX(), e.getY())) {
                        createNode(e.getX(), e.getY(), nodeName);
                        repaint();
                    }
                }
            }
        });
        

        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 2, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
    }

    

    public void setGraphType(boolean isDirected) {
        this.isDirected = isDirected;
        repaint();
    }


    // Method to toggle interactivity
    public void setInteractivityEnabled(boolean enabled) {
        this.interactivityEnabled = enabled;
        repaint(); // Update the UI
    }

    public void setEdgeDirectionEnabled(boolean enabled) {
        this.edgeDirectionEnabled = enabled;
    }

    private String getNodeNameFromUser() {
        return JOptionPane.showInputDialog(this, "Enter unique node name:", "Node Creation", JOptionPane.PLAIN_MESSAGE);
    }

    private boolean isUniqueNodeName(String name) {
        for (Node node : nodes) {
            if (node.getName().equals(name)) {
                JOptionPane.showMessageDialog(this, "Node name must be unique!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private boolean isPositionAvailable(int x, int y) {
        for (Node node : nodes) {
            double distance = Math.sqrt(Math.pow(node.getX() - x, 2) + Math.pow(node.getY() - y, 2));
            if (distance < NODE_RADIUS * 2) {
                JOptionPane.showMessageDialog(this, "Node position overlaps with an existing node!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private void createNode(int x, int y, String name) {
        nodes.add(new Node(x, y, name));
    }

    private void createEdge(Node startNode, Node endNode) {
        while (true) {
            String weightInput = JOptionPane.showInputDialog(this, "Enter weight for the edge:", "Edge Weight", JOptionPane.PLAIN_MESSAGE);
            if (weightInput == null) {
                JOptionPane.showMessageDialog(this, "Edge creation canceled.", "Canceled", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            try {
                int weight = Integer.parseInt(weightInput);
                edges.add(new Edge(startNode, endNode, weight));
                repaint();
                break;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid weight. Please enter a numeric value.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public List<Node> depthFirstSearch(Node startNode, Node endNode) {
        List<Node> visited = new ArrayList<>();
        List<Node> path = new ArrayList<>();
        if (dfsHelper(startNode, endNode, visited, path)) {
            return path;
        }
        return new ArrayList<>(); // Return an empty list if no path is found
    }
    
    private boolean dfsHelper(Node current, Node target, List<Node> visited, List<Node> path) {
        visited.add(current);
        path.add(current);
    
        if (current == target) {
            return true; // Path found
        }
    
        for (Node neighbor : current.getNeighbors()) {
            if (!visited.contains(neighbor)) {
                if (dfsHelper(neighbor, target, visited, path)) {
                    return true;
                }
            }
        }
    
        path.remove(current); // Backtrack
        return false;
    }
    
    

    private boolean edgeExists(Node startNode, Node endNode) {
        for (Edge edge : edges) {
            if ((edge.getStartNode() == startNode && edge.getEndNode() == endNode) ||
                (edge.getStartNode() == endNode && edge.getEndNode() == startNode)) {
                return true;
            }
        }
        return false;
    }

    private Node getNodeAtPosition(int x, int y) {
        for (Node node : nodes) {
            double distance = Math.sqrt(Math.pow(node.getX() - x, 2) + Math.pow(node.getY() - y, 2));
            if (distance < NODE_RADIUS) {
                return node;
            }
        }
        return null;
    }

    public void clearGraph() {
        nodes.clear();
        edges.clear();
        clearHighlightedPaths();
        repaint();
    }

    // New method to clear the highlighted paths
    public void clearHighlightedPaths() {
        highlightedPaths.clear();
        repaint();
    }

    public void highlightPath(List<Node> path, Color color) {
        highlightedPaths.add(new HighlightedPath(path, color));
        repaint();
    }

    

    public Color generateUniqueColor() {
        Color color;

        do {
            color = new Color((int) (Math.random() * 0x1000000));
        } while (colorExists(color)); // Call a method to check if the color exists

        return color;
    }

    private boolean colorExists(Color color) {
        return highlightedPaths.stream()
                            .anyMatch(p -> p.getColor().getRGB() == color.getRGB());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw edges
        g2.setColor(Color.BLACK);  // Set edge color to black
        for (Edge edge : edges) {
            int startX = edge.getStartNode().getX();
            int startY = edge.getStartNode().getY();
            int endX = edge.getEndNode().getX();
            int endY = edge.getEndNode().getY();

            // Draw the edge line
            g2.drawLine(startX, startY, endX, endY);

            // Draw an arrow for directed graphs
            if (isDirected) {
                drawArrow(g2, startX, startY, endX, endY);
            }

            // Draw the weight in black color
            int midX = (startX + endX) / 2;
            int midY = (startY + endY) / 2;
            g2.setColor(Color.BLACK); // Ensure the weight is always drawn in black
            g2.drawString(String.valueOf(edge.getWeight()), midX, midY);
        }

        // Highlight paths
        for (HighlightedPath highlightedPath : highlightedPaths) {
            g2.setColor(highlightedPath.getColor());
            List<Node> path = highlightedPath.getPath();
            for (int i = 0; i < path.size() - 1; i++) {
                Node start = path.get(i);
                Node end = path.get(i + 1);
                g2.drawLine(start.getX(), start.getY(), end.getX(), end.getY());
            }
        }

        // Draw nodes
        for (Node node : nodes) {
            g2.setColor(Color.WHITE);
            g2.fillOval(node.getX() - NODE_RADIUS, node.getY() - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);
            g2.setColor(Color.BLACK);
            g2.drawOval(node.getX() - NODE_RADIUS, node.getY() - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);
            g2.drawString(node.getName(), node.getX() - NODE_RADIUS, node.getY() - NODE_RADIUS - 5);
        }
    }

    private void drawArrow(Graphics2D g2, int x1, int y1, int x2, int y2) {
        double angle = Math.atan2(y2 - y1, x2 - x1);
        int arrowSize = 10;

        int xArrow1 = (int) (x2 - arrowSize * Math.cos(angle - Math.PI / 6));
        int yArrow1 = (int) (y2 - arrowSize * Math.sin(angle - Math.PI / 6));
        int xArrow2 = (int) (x2 - arrowSize * Math.cos(angle + Math.PI / 6));
        int yArrow2 = (int) (y2 - arrowSize * Math.sin(angle + Math.PI / 6));

        g2.drawLine(x2, y2, xArrow1, yArrow1);
        g2.drawLine(x2, y2, xArrow2, yArrow2);
    }



    // Helper class for highlighted paths
    private static class HighlightedPath {
        private final List<Node> path;
        private final Color color;

        public HighlightedPath(List<Node> path, Color color) {
            this.path = path;
            this.color = color;
        }

        public List<Node> getPath() {
            return path;
        }

        public Color getColor() {
            return color;
        }
    }

    // Method to retrieve a node by its name
    public Node getNodeByName(String name) {
        for (Node node : nodes) {
            if (node.getName().equals(name)) {
                return node;
            }
        }
        return null;
    }

    // Method to delete a node by name
    public boolean deleteNode(String name) {
        Node nodeToDelete = getNodeByName(name);
        if (nodeToDelete == null) {
            return false; // Node does not exist
        }

        // Remove all edges connected to this node
        edges.removeIf(edge -> edge.getStartNode() == nodeToDelete || edge.getEndNode() == nodeToDelete);

        // Remove the node itself
        nodes.remove(nodeToDelete);
        repaint(); // Redraw the graph
        return true;
    }

    // Method to delete an edge between two nodes
    public boolean deleteEdge(String startNodeName, String endNodeName) {
        Node startNode = getNodeByName(startNodeName);
        Node endNode = getNodeByName(endNodeName);

        if (startNode == null || endNode == null) {
            return false; // One or both nodes do not exist
        }

        // Find and remove the edge
        Edge edgeToRemove = null;
        for (Edge edge : edges) {
            if ((edge.getStartNode() == startNode && edge.getEndNode() == endNode) ||
                (edge.getStartNode() == endNode && edge.getEndNode() == startNode)) {
                edgeToRemove = edge;
                break;
            }
        }

        if (edgeToRemove != null) {
            edges.remove(edgeToRemove);
            repaint(); // Redraw the graph
            return true;
        }

        return false; // Edge does not exist
    }

    // Check if node exists by name
    public boolean nodeExists(String name) {
        return nodes.stream().anyMatch(node -> node.getName().equals(name));
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }
}

