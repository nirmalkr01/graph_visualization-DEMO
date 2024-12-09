// Edge.java
public class Edge {
    private final Node startNode;
    private final Node endNode;
    private final int weight;

    public Edge(Node startNode, Node endNode, int weight) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.weight = weight;
        startNode.addNeighbor(endNode);
        endNode.addNeighbor(startNode);
    }

    public Node getStartNode() {
        return startNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    public int getWeight() {
        return weight;
    }
}