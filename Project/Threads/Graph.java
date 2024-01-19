package Threads;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph {
    private int size;
    private Map<Integer, Set<Integer>> nodes;

    //constructor
    public Graph(int n) {
        this.size = n;

        nodes = new HashMap<>();
        for (int node = 0; node < n; node++) {
            nodes.put(node, new HashSet<>());
        }
    }

    public boolean add(int startNode, int endNode) {
        return nodes.get(startNode).add(endNode);
    }

    public boolean has(int startNode, int endNode) {
        return nodes.get(startNode).contains(endNode);
    }

    public int size() {
        return size;
    }

    public static Graph generateRandomGraph(int size) {
        Graph graph = new Graph(size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i != j) {
                    if (Math.random() < 0.01 && !graph.has(i, j)) {
                        graph.add(i, j);
                    }
                }
            }
        }
        return graph;
    }

    @Override
    public String toString() {
        return "Graph { " +
                " size = " + size +
                ", nodes = " + nodes +
                " }";
    }
}