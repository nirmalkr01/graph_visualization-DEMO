import java.util.*;

public class DijkstraAlgorithm {

    public static List<Node> findShortestPath(List<Node> nodes, List<Edge> edges, Node startNode, Node endNode) {
        Map<Node, Integer> distances = new HashMap<>();
        Map<Node, Node> previousNodes = new HashMap<>();
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        // Initialize distances to infinity for all nodes except the start node
        for (Node node : nodes) {
            distances.put(node, Integer.MAX_VALUE);
            previousNodes.put(node, null);
        }
        distances.put(startNode, 0);
        queue.add(startNode);

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();

            // Skip if the current node's distance is infinity
            if (distances.get(currentNode) == Integer.MAX_VALUE) {
                break;
            }

            // Process neighbors of the current node
            for (Edge edge : edges) {
                Node neighbor = null;

                if (edge.getStartNode().equals(currentNode)) {
                    neighbor = edge.getEndNode();
                } else if (edge.getEndNode().equals(currentNode)) {
                    neighbor = edge.getStartNode();
                }

                if (neighbor != null) {
                    int newDist = distances.get(currentNode) + edge.getWeight();

                    if (newDist < distances.get(neighbor)) {
                        distances.put(neighbor, newDist);
                        previousNodes.put(neighbor, currentNode);

                        // Remove and re-add to update queue
                        queue.remove(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
        }

        // Reconstruct the path
        List<Node> path = new ArrayList<>();
        for (Node at = endNode; at != null; at = previousNodes.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);

        return (path.size() > 0 && path.get(0).equals(startNode)) ? path : Collections.emptyList();
    }

    public static boolean isPathExists(List<Node> nodes, List<Edge> edges, Node startNode, Node endNode) {
        Set<Node> visited = new HashSet<>();
        Queue<Node> queue = new LinkedList<>();

        queue.add(startNode);
        visited.add(startNode);

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();

            if (currentNode.equals(endNode)) {
                return true;
            }

            for (Edge edge : edges) {
                Node neighbor = null;

                if (edge.getStartNode().equals(currentNode)) {
                    neighbor = edge.getEndNode();
                } else if (edge.getEndNode().equals(currentNode)) {
                    neighbor = edge.getStartNode();
                }

                if (neighbor != null && !visited.contains(neighbor)) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                }
            }
        }

        return false;
    }

    public static int getPathLength(List<Node> nodes, List<Edge> edges, Node startNode, Node endNode) {
        List<Node> path = findShortestPath(nodes, edges, startNode, endNode);

        if (path.isEmpty()) {
            return -1;
        }

        return calculatePathLength(edges, path);
    }

    public static int calculatePathLength(List<Edge> edges, List<Node> path) {
        int totalLength = 0;

        for (int i = 0; i < path.size() - 1; i++) {
            Node current = path.get(i);
            Node next = path.get(i + 1);

            for (Edge edge : edges) {
                if ((edge.getStartNode().equals(current) && edge.getEndNode().equals(next)) ||
                    (edge.getStartNode().equals(next) && edge.getEndNode().equals(current))) {
                    totalLength += edge.getWeight();
                    break;
                }
            }
        }

        return totalLength;
    }
}
