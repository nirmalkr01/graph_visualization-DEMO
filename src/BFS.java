import java.util.*;

public class BFS {
    public static List<Node> findBFSPath(List<Node> nodes, Node startNode, Node endNode) {
        Map<Node, Node> parentMap = new HashMap<>();
        Queue<Node> queue = new LinkedList<>();
        Set<Node> visited = new HashSet<>();

        queue.add(startNode);
        visited.add(startNode);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            if (current.equals(endNode)) {
                return constructPath(parentMap, startNode, endNode);
            }

            for (Node neighbor : current.getNeighbors()) {
                if (!visited.contains(neighbor)) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                    parentMap.put(neighbor, current);
                }
            }
        }

        return Collections.emptyList();
    }

    private static List<Node> constructPath(Map<Node, Node> parentMap, Node start, Node end) {
        List<Node> path = new ArrayList<>();
        Node current = end;

        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }

        Collections.reverse(path);
        return path;
    }

    public static boolean isPathExists(List<Node> nodes, Node startNode, Node endNode) {
        return !findBFSPath(nodes, startNode, endNode).isEmpty();
    }
}
