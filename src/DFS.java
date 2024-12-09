import java.util.*;

public class DFS {
    public static List<Node> findDFSPath(List<Node> nodes, Node startNode, Node endNode) {
        Set<Node> visited = new HashSet<>();
        List<Node> path = new ArrayList<>();
        
        // Start DFS from the start node
        if (dfsHelper(startNode, endNode, visited, path)) {
            return path;
        }
        return Collections.emptyList();  // Return empty list if no path is found
    }

    private static boolean dfsHelper(Node current, Node target, Set<Node> visited, List<Node> path) {
        visited.add(current);
        path.add(current);  // Add the current node to the path

        // If we've reached the target node, return true
        if (current.equals(target)) {
            return true;
        }

        // Iterate over the neighbors of the current node
        for (Node neighbor : current.getNeighbors()) {
            if (!visited.contains(neighbor)) {
                if (dfsHelper(neighbor, target, visited, path)) {
                    return true;  // Path found
                }
            }
        }

        // Backtrack if no path was found from this node
        path.remove(path.size() - 1);
        return false;
    }
}
