import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.List;


public class GraphVisualizer extends JFrame {
    private RoundedPanel topPanel;
    private GraphPanel graphPanel;
    private RoundedPanel rightPanel;
    private JComboBox<String> algorithmDropdown;
    private JComboBox<String> graphtype;
    private JTextField startNodeField; // Input for start node name
    private JTextField endNodeField;   // Input for end node name
    private JTextArea solutionArea;
    private JButton clearButton;
    

    @SuppressWarnings("unused")
    public GraphVisualizer() {
        setTitle("ChartCraft");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 240, 240));

        // Set application icon
        String iconPath = "E:\\3rd year_1st sem\\minor\\project\\Graph_visualiser\\assets\\icon.png";
        File iconFile = new File(iconPath);
        if (iconFile.exists()) {
            ImageIcon icon = new ImageIcon(iconPath);
            Image scaledIcon = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            setIconImage(scaledIcon);
        } else {
            System.out.println("Icon file not found: " + iconPath);
        }

        // Top panel with title, algorithm dropdown, and node selection fields
        topPanel = new RoundedPanel(20, Color.GRAY);
        topPanel.setBackground(new Color(220, 220, 220));
        topPanel.setPreferredSize(new Dimension(1000, 100));
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));

        // Graph type dropdown menu
        graphtype = new JComboBox<>(new String[]{"Select Graph Type", "Directed", "Undirected"});
        graphtype.setPreferredSize(new Dimension(150, 25));
        graphtype.addActionListener(e -> {
            String selectedType = (String) graphtype.getSelectedItem();
            if (!"Select Graph Type".equals(selectedType)) {
                algorithmDropdown.setEnabled(true); // Enable the algorithm dropdown
                if ("Directed".equals(selectedType)) {
                    restrictDirectedGraph(); // Restrict directed graph option
                } else if ("Undirected".equals(selectedType)) {
                    applyUndirected();
                }
            } else {
                algorithmDropdown.setEnabled(false); // Disable the algorithm dropdown if no valid graph type is selected
            }
            updateGraphPanelInteractivity(); // Call the new method to update interactivity
        });
        topPanel.add(graphtype);

        // Algorithm dropdown menu
        algorithmDropdown = new JComboBox<>(new String[]{"Select Algorithm", "Dijkstra’s Algorithm","DFS Algorithm","BFS Algorithm"});
        algorithmDropdown.setPreferredSize(new Dimension(150, 25));
        algorithmDropdown.setEnabled(false); // Initially disabled
        algorithmDropdown.addActionListener(e -> {
            if (!algorithmDropdown.isEnabled()) {
                JOptionPane.showMessageDialog(this, "Please select a graph type first.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String selectedAlgorithm = (String) algorithmDropdown.getSelectedItem();
            if ("Dijkstra’s Algorithm".equals(selectedAlgorithm)) {
                applyDijkstraAlgorithm();
            }

            if ("DFS Algorithm".equals(selectedAlgorithm)) {
                applyDFSAlgorithm();
            }

            if ("BFS Algorithm".equals(selectedAlgorithm)){
                applyBFSAlgorithm();
            }
        });
        topPanel.add(algorithmDropdown);


        // Start and End Node fields
        topPanel.add(new JLabel("Start Node:"));
        startNodeField = new JTextField(8);
        topPanel.add(startNodeField);

        topPanel.add(new JLabel("End Node:"));
        endNodeField = new JTextField(8);
        topPanel.add(endNodeField);

        // Add "Delete Node" button
        JButton deleteNodeButton = new JButton("Delete Node");
        deleteNodeButton.addActionListener(e -> {
            String nodeName = JOptionPane.showInputDialog(this, "Enter the name of the node to delete:", "Delete Node", JOptionPane.PLAIN_MESSAGE);
            if (nodeName != null && !nodeName.isEmpty()) {
                if (graphPanel.deleteNode(nodeName)) {
                    graphPanel.clearHighlightedPaths();
                    JOptionPane.showMessageDialog(this, "Node deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Node not found. Please ensure the node exists.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        topPanel.add(deleteNodeButton);

        // Add "Delete Edge" button
        JButton deleteEdgeButton = new JButton("Delete Edge");
        deleteEdgeButton.addActionListener(e -> {
            String startNode = JOptionPane.showInputDialog(this, "Enter the name of the start node for the edge:", "Delete Edge", JOptionPane.PLAIN_MESSAGE);
            String endNode = JOptionPane.showInputDialog(this, "Enter the name of the end node for the edge:", "Delete Edge", JOptionPane.PLAIN_MESSAGE);
            if (startNode != null && endNode != null && !startNode.isEmpty() && !endNode.isEmpty()) {
                if (graphPanel.deleteEdge(startNode, endNode)) {
                    JOptionPane.showMessageDialog(this, "Edge deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Edge not found. Please ensure the edge exists.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        topPanel.add(deleteEdgeButton);

        add(topPanel, BorderLayout.NORTH);

        // Add "Restart" button
        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to restart? This will remove all nodes and edges.", 
                "Restart Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                graphPanel.clearGraph();
                solutionArea.setText("");
                JOptionPane.showMessageDialog(this, "Graph cleared successfully!", "Restarted", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        topPanel.add(restartButton);

        add(topPanel, BorderLayout.NORTH);


        // Graph panel
        graphPanel = new GraphPanel();
        graphPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        graphPanel.setPreferredSize(new Dimension(700, 500));
        add(graphPanel, BorderLayout.CENTER);

        // Right panel (Optional features placeholder)
        rightPanel = new RoundedPanel(20, Color.GRAY);
        rightPanel.setBackground(new Color(240, 240, 240));
        rightPanel.setPreferredSize(new Dimension(300, 500));
        rightPanel.setLayout(new BorderLayout(10, 10)); // Use BorderLayout for better positioning
        rightPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Text area to display the solution of the selected algorithm
        solutionArea = new JTextArea(20, 25);
        solutionArea.setEditable(false);
        solutionArea.setLineWrap(true);
        solutionArea.setWrapStyleWord(true);
        solutionArea.setBackground(new Color(230, 230, 230));
        JScrollPane solutionScrollPane = new JScrollPane(solutionArea);
        rightPanel.add(solutionScrollPane, BorderLayout.CENTER);

        // Panel for clear button at the bottom right
        JPanel clearButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        clearButtonPanel.setBackground(new Color(240, 240, 240)); // Match background
        clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> solutionArea.setText(""));
        clearButtonPanel.add(clearButton);

        rightPanel.add(clearButtonPanel, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.EAST);


        setVisible(true);
    }

    // Restrict directed graph selection
    private void restrictDirectedGraph() {
        JOptionPane.showMessageDialog(this, "This section is not ready for now. Please go with Undirected only.", "Info", JOptionPane.INFORMATION_MESSAGE);
        graphtype.setSelectedItem("Select Graph Type"); // Reset dropdown to default
    }

    // Apply directed graph settings
    @SuppressWarnings("unused")
    private void applyDirected() {
        graphPanel.setGraphType(true);
        graphPanel.setInteractivityEnabled(true);
    }

    // Apply undirected graph settings
    private void applyUndirected() {
        graphPanel.setGraphType(false);
        graphPanel.setInteractivityEnabled(true);
    }
    // Update graph panel interactivity
    private void updateGraphPanelInteractivity() {
        String selectedType = (String) graphtype.getSelectedItem();
        boolean enableGraphPanel = "Undirected".equals(selectedType);
        graphPanel.setInteractivityEnabled(enableGraphPanel);

        if (!enableGraphPanel) {
            JOptionPane.showMessageDialog(this, "Please select 'Undirected' graph type to proceed.", "Warning", JOptionPane.WARNING_MESSAGE);
        }

        // Update edge direction feature based on graph type
        if ("Directed".equals(selectedType)) {
            graphPanel.setEdgeDirectionEnabled(false); // Disable all features for directed graph
        } else if ("Undirected".equals(selectedType)) {
            graphPanel.setEdgeDirectionEnabled(false); // Undirected graph does not require edge direction
        }
    }

    private void applyDijkstraAlgorithm() {
        String startNodeName = startNodeField.getText().trim();
        String endNodeName = endNodeField.getText().trim();
        Node startNode = graphPanel.getNodeByName(startNodeName);
        Node endNode = graphPanel.getNodeByName(endNodeName);
    
        if (startNode == null || endNode == null) {
            JOptionPane.showMessageDialog(this, "Invalid start or end node name. Please ensure both nodes exist.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        if (startNode == endNode) {
            JOptionPane.showMessageDialog(this, "Invalid start or end node name. Please ensure both nodes are different.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        List<Node> nodes = graphPanel.getNodes();
        List<Edge> edges = graphPanel.getEdges();
    
        boolean pathExists = DijkstraAlgorithm.isPathExists(nodes, edges, startNode, endNode);
        if (!pathExists) {
            JOptionPane.showMessageDialog(this, "No edge exists between the selected nodes.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        List<Node> shortestPath = DijkstraAlgorithm.findShortestPath(nodes, edges, startNode, endNode);
    
        long startTime = System.nanoTime();
        long endTime = System.nanoTime();
        long timeTaken = endTime - startTime;
    
        if (shortestPath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No path found between the selected nodes.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        graphPanel.clearHighlightedPaths();
    
        Color pathColor = graphPanel.generateUniqueColor();
        graphPanel.highlightPath(shortestPath, pathColor);
    
        StringBuilder solutionText = new StringBuilder();
        solutionText.append("-------------------------------------------------\n");
        solutionText.append("Shortest Path from ").append(startNodeName).append(" to ").append(endNodeName).append("(Dijkstra’s):\n");
        for (Node node : shortestPath) {
            solutionText.append(node.getName()).append(" -> ");
        }
        solutionText.delete(solutionText.length() - 4, solutionText.length());
        solutionText.append("\n");
    
        int pathLength = DijkstraAlgorithm.getPathLength(nodes, edges, startNode, endNode);
        solutionText.append("Path Length: ").append(pathLength).append("\n");
        solutionText.append("Time Complexity: O(E + V log V)\n");
        solutionText.append("Space Complexity: O(V + E)\n");
        solutionText.append("Execution Time: ").append(timeTaken).append(" ns\n");
    
        solutionArea.append(solutionText.toString());
    
        String algorithmCode = AlgorithmCodeProvider.getAlgorithmCode("Dijkstra’s Algorithm");
    
        solutionArea.append("\nJava Code for Dijkstra's Algorithm:\n");
        solutionArea.append("\n");
        solutionArea.append(algorithmCode);
        solutionArea.append("\n");
        solutionArea.append("-------------------------------------------------\n");
    
        JOptionPane.showMessageDialog(this, "Shortest path!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    

    private void applyDFSAlgorithm() {
        String startNodeName = startNodeField.getText().trim();
        String endNodeName = endNodeField.getText().trim();
        Node startNode = graphPanel.getNodeByName(startNodeName);
        Node endNode = graphPanel.getNodeByName(endNodeName);
    
        if (startNode == null || endNode == null) {
            JOptionPane.showMessageDialog(this, "Invalid start or end node name. Please ensure both nodes exist.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        if (startNode == endNode) {
            JOptionPane.showMessageDialog(this, "Start and end nodes cannot be the same.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        long startTime = System.nanoTime();
        List<Node> path = DFS.findDFSPath(graphPanel.getNodes(), startNode, endNode);
        long endTime = System.nanoTime();
        long timeTaken = endTime - startTime;
    
        if (path.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No path exists between the selected nodes.", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            graphPanel.clearHighlightedPaths();
            graphPanel.highlightPath(path, graphPanel.generateUniqueColor());
    
            StringBuilder solutionText = new StringBuilder();
            solutionText.append("-------------------------------------------------\n");
            solutionText.append("Path from ").append(startNodeName).append(" to ").append(endNodeName).append(" (DFS):\n");
            for (Node node : path) {
                solutionText.append(node.getName()).append(" -> ");
            }
            solutionText.delete(solutionText.length() - 4, solutionText.length());
            solutionText.append("\n");
            solutionText.append("Time Complexity: O(V + E)\n");
            solutionText.append("Space Complexity: O(V)\n");
            solutionText.append("Execution Time: ").append(timeTaken).append(" ns\n");
    
            solutionArea.append(solutionText.toString());
    
            String algorithmCode = AlgorithmCodeProvider.getAlgorithmCode("DFS Algorithm");
    
            solutionArea.append("\nJava Code for DFS Algorithm:\n");
            solutionArea.append("\n");
            solutionArea.append(algorithmCode);
            solutionArea.append("\n");
            solutionArea.append("-------------------------------------------------\n");
    
            JOptionPane.showMessageDialog(this, "DFS path found!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void applyBFSAlgorithm() {
        String startNodeName = startNodeField.getText().trim();
        String endNodeName = endNodeField.getText().trim();
        Node startNode = graphPanel.getNodeByName(startNodeName);
        Node endNode = graphPanel.getNodeByName(endNodeName);
    
        if (startNode == null || endNode == null) {
            JOptionPane.showMessageDialog(this, "Invalid start or end node name. Please ensure both nodes exist.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        if (startNode == endNode) {
            JOptionPane.showMessageDialog(this, "Start and end nodes cannot be the same.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        long startTime = System.nanoTime();
        List<Node> path = BFS.findBFSPath(graphPanel.getNodes(), startNode, endNode); // Use BFS class
        long endTime = System.nanoTime();
        long timeTaken = endTime - startTime;
    
        if (path.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No path exists between the selected nodes.", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            graphPanel.clearHighlightedPaths();
            graphPanel.highlightPath(path, graphPanel.generateUniqueColor());
    
            StringBuilder solutionText = new StringBuilder();
            solutionText.append("-------------------------------------------------\n");
            solutionText.append("Path from ").append(startNodeName).append(" to ").append(endNodeName).append(" (BFS):\n");
            for (Node node : path) {
                solutionText.append(node.getName()).append(" -> ");
            }
            solutionText.delete(solutionText.length() - 4, solutionText.length());
            solutionText.append("\n");
            solutionText.append("Time Complexity: O(V + E)\n");
            solutionText.append("Space Complexity: O(V)\n");
            solutionText.append("Execution Time: ").append(timeTaken).append(" ns\n");
    
            solutionArea.append(solutionText.toString());
    
            String algorithmCode = AlgorithmCodeProvider.getAlgorithmCode("BFS Algorithm");
    
            solutionArea.append("\nJava Code for BFS Algorithm:\n");
            solutionArea.append("\n");
            solutionArea.append(algorithmCode);
            solutionArea.append("\n");
            solutionArea.append("-------------------------------------------------\n");
    
            JOptionPane.showMessageDialog(this, "BFS path found!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GraphVisualizer::new);
    }
}
