import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class AlgorithmCodeProvider {
    // Method to load the Java code of the algorithm
    public static String getAlgorithmCode(String algorithmName) {
        String filePath;
        if ("Dijkstra’s Algorithm".equals(algorithmName)) {
            filePath = "E:\\3rd year_1st sem\\minor\\project\\Graph_visualiser\\src\\DijkstraAlgorithm.java";
        } else if ("DFS Algorithm".equals(algorithmName)) {
            filePath = "E:\\3rd year_1st sem\\minor\\project\\cart_craft\\src\\DFS.java";
        } else if ("BFS Algorithm".equals(algorithmName)) {
            filePath = "E:\\3rd year_1st sem\\minor\\project\\cart_craft\\src\\BFS.java";
        
        } else {
            return "Algorithm code not available.";
        }

        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            return "Error loading algorithm code: " + e.getMessage();
        }
    }
}
