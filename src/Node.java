import java.util.ArrayList;
import java.util.List;

public class Node {
    private final int x;
    private final int y;
    private List<Node> neighbors;
    private final String name;

    public Node(int x, int y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.neighbors = new ArrayList<>();  // Initialize neighbors list as an empty list
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    public List<Node> getNeighbors() {
        return neighbors;
    }

    // Optionally, you can add a method to add neighbors dynamically
    public void addNeighbor(Node neighbor) {
        if (!this.neighbors.contains(neighbor)) {
            this.neighbors.add(neighbor);
            neighbor.getNeighbors().add(this); // Ensure bidirectional connection
        }
    }
    
    
}
