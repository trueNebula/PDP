package Threads;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        int threads = 10;

        //Example 1
        Graph graph = new Graph(4);
        graph.add(0, 1);
        graph.add(1, 2);
        graph.add(0, 3);

        Colors colors = new Colors(2);
        colors.add(0, "pink");
        colors.add(1, "black");

//        // Example 2
//        Graph graph = new Graph(5);
//        graph.add(0, 1);
//        graph.add(1, 2);
//        graph.add(2, 3);
//        graph.add(3, 4);
//        graph.add(4, 0);
//        graph.add(2, 0);
//        graph.add(0, 4);
//        graph.add(4, 3);
//        graph.add(3, 1);
//
//        Colors colors = new Colors(3);
//        colors.add(0, "red");
//        colors.add(1, "green");
//        colors.add(2, "blue");

//        // Example 3
//        Graph graph = Graph.generateRandomGraph(500);
//        System.out.println(graph);
//        Colors colors = new Colors(10);
//        colors.add(0, "red");
//        colors.add(1, "green");
//        colors.add(2, "blue");
//        colors.add(3, "yellow");
//        colors.add(4, "pink");
//        colors.add(5, "black");
//        colors.add(6, "white");
//        colors.add(7, "orange");
//        colors.add(8, "transparent");
//        colors.add(9, "new");

        try {
            LocalTime start = LocalTime.now();

            Map<Integer,String> result = GraphColoring.graphColoringMain(threads, graph, colors);

            LocalTime end = LocalTime.now();
            System.out.println("Time taken: " + start.until(end, ChronoUnit.MICROS) + "us");
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
