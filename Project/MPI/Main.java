package MPI;

import mpi.MPI;
import java.util.Map;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Main {
    public static void main(String[] args) {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

//         // Example 1
//         Graph graph2 = new Graph(5);
//         graph2.add(0,1);
//         graph2.add(1,2);
//         graph2.add(2,3);
//         graph2.add(3,4);
//         graph2.add(4,0);
//         graph2.add(2,0);
//         graph2.add(0,4);
//         graph2.add(4,3);
//         graph2.add(3,1);
//
//         Colors colors2 = new Colors(3);
//         colors2.add(0, "red");
//         colors2.add(1, "green");
//         colors2.add(2, "blue");

        //Example 2
        Graph graph2 = Graph.generateRandomGraph(500);
        Colors colors2 = new Colors(10);

        colors2.add(0, "red");
        colors2.add(1, "green");
        colors2.add(2, "blue");
        colors2.add(3, "yellow");
        colors2.add(4, "pink");
        colors2.add(5, "black");
        colors2.add(6, "white");
        colors2.add(7, "orange");
        colors2.add(8, "transparent");
        colors2.add(9, "new");

        if (rank == 0) {
            System.out.println("Main process");

            try {
                LocalTime start = LocalTime.now();

                Map<Integer,String> result = GraphColoring.graphColoringMain(size, graph2, colors2);

                LocalTime end = LocalTime.now();
                System.out.println("Time taken: " + start.until(end, ChronoUnit.MICROS) + "us");
                System.out.println(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Process number: " + rank);

            int colorSize = colors2.size();

            GraphColoring.graphColoringChild(rank, size, graph2, colorSize);
        }
        MPI.Finalize();

    }
}

