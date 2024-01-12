import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;


public class Main {

    private static class Graph {
        int vertices;
        boolean[][] adjacencyMatrix;

        public Graph(int vertices) {
            this.vertices = vertices;
            this.adjacencyMatrix = new boolean[vertices][vertices];
        }

        public void addEdge(int src, int dest) {
            adjacencyMatrix[src][dest] = true;
        }
    }

    private static class SharedData {
        List<List<Integer>> partialCycles = new ArrayList<>();
    }

    private static class HamiltonianCycleFinder implements Runnable {
        Graph graph;
        int startVertex;
        SharedData sharedData;

        public HamiltonianCycleFinder(Graph graph, int startVertex, SharedData sharedData) {
            this.graph = graph;
            this.startVertex = startVertex;
            this.sharedData = sharedData;
        }

        private void exploreParallel(int currentVertex, List<Integer> partialCycle) {
            if (partialCycle.size() == graph.vertices) {
                // Check if the current partial cycle forms a Hamiltonian cycle
                if (graph.adjacencyMatrix[currentVertex][startVertex]) {
                    synchronized (sharedData) {
                        sharedData.partialCycles.add(new ArrayList<>(partialCycle));
                    }
                }
                return;
            }

            for (int neighbor = 0; neighbor < graph.vertices; neighbor++) {
                if (!partialCycle.contains(neighbor) && graph.adjacencyMatrix[currentVertex][neighbor]) {
                    List<Integer> newPartialCycle = new ArrayList<>(partialCycle);
                    newPartialCycle.add(neighbor);

                    // Recursively explore the next level in parallel
                    exploreParallel(neighbor, newPartialCycle);
                }
            }
        }

        @Override
        public void run() {
            exploreParallel(startVertex, List.of(startVertex));
        }
    }

    public static List<Integer> findHamiltonianCycleParallel(Graph graph, int numThreads) {
        SharedData sharedData = new SharedData();

        // Create and start multiple threads
        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(new HamiltonianCycleFinder(graph, i, sharedData));
            threads[i].start();
        }
        LocalTime start = LocalTime.now();

        // Wait for all threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        LocalTime end = LocalTime.now();
        System.out.println("Time taken: " + start.until(end, ChronoUnit.MICROS) + "us");

        // Analyze results from shared data structure
        return chooseBestHamiltonianCycle(sharedData.partialCycles);
    }

    private static List<Integer> chooseBestHamiltonianCycle(List<List<Integer>> partialCycles) {
        // Implement logic to choose the best Hamiltonian cycle from the list
        // For simplicity, here we just return the first found cycle (if any)
        return partialCycles.isEmpty() ? null : partialCycles.get(0);
    }

    public static void main(String[] args) {
        Graph graph = new Graph(10);

        // Edges that form a cycle
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(4, 5);
        graph.addEdge(5, 6);
        graph.addEdge(6, 7);
        graph.addEdge(7, 8);
        graph.addEdge(8, 9);
        graph.addEdge(9, 0);

        // Additional edges to break the cycle
        graph.addEdge(0, 2);
        graph.addEdge(2, 4);
        graph.addEdge(4, 6);
        graph.addEdge(6, 8);
        graph.addEdge(8, 0);

        int numThreads = 4;
        List<Integer> hamiltonianCycle = findHamiltonianCycleParallel(graph, numThreads);

        if (hamiltonianCycle != null) {
            System.out.println("Hamiltonian Cycle found: " + hamiltonianCycle);
        } else {
            System.out.println("No Hamiltonian Cycle found.");
        }
    }
}
