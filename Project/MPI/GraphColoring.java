package MPI;

import mpi.MPI;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;

public class GraphColoring {
    public static Map<Integer,String> graphColoringMain(int mpiSize, Graph graph, Colors colors) throws Exception {
        int size = colors.size();
        int[] indices = graphColoringRecursive(0, graph, size, new int[graph.size()], 0, mpiSize, 0);

        if (indices[0] == -1) {
            throw new Exception("No solution found!");
        }

        return colors.getColorsForIndices(indices);
    }
    private static int[] graphColoringRecursive(int nodeId, Graph graph, int colorsSize, int[] codes, int mpiRank, int mpiSize, int power) {
        int graphSize = graph.size();

        // base case: invalid solution (neighbours have the same color)
        if (!valid(nodeId, codes, graph)) {
            int[] array = new int[graphSize];
            Arrays.fill(array, -1);

            return array;
        }

        // base case: valid solution
        if (nodeId + 1 == graph.size()) {
            return codes;
        }

        // assign a color to a vertex and check if the given solution is valid
        Random rand = new Random();
        int coefficient = rand.nextInt() & Integer.MAX_VALUE;
        int colorIndex = 0;
        int destination = mpiRank + coefficient * (colorIndex + 1);

        while (colorIndex + 1 < colorsSize && destination < mpiSize) {
            colorIndex++;
            destination = mpiRank + coefficient * (colorIndex + 1 );
        }

        // send data to destination process
        int nextNode = nodeId + 1;
        int nextPower = power + 1;

        for (int currentIndex = 1; currentIndex < colorIndex; currentIndex++) {
            System.out.println(coefficient + ' ' + currentIndex);
            destination = mpiRank + coefficient * currentIndex;

            int[] data = new int[]{mpiRank, nextNode, nextPower};
            MPI.COMM_WORLD.Send(data, 0, data.length, MPI.INT, destination, 0);

            int[] nextColorCodes = Arrays.copyOf(codes, codes.length);
            nextColorCodes[nextNode] = currentIndex;

            MPI.COMM_WORLD.Send(nextColorCodes, 0, graphSize, MPI.INT, destination, 0);
        }

        // try color 0 for next node on this process
        int[] nextColorCodes = Arrays.copyOf(codes,codes.length);
        nextColorCodes[nextNode] = 0;

        int[] result = graphColoringRecursive(nextNode, graph, colorsSize, nextColorCodes, mpiRank, mpiSize, nextPower);
        if (result[0] != -1) {
            return result;
        }

        // receive data from destination processes
        for (int currentIndex = 1; currentIndex < colorIndex; currentIndex++) {
            destination = mpiRank + coefficient * currentIndex;
            result = new int[graphSize];

            MPI.COMM_WORLD.Recv(result, 0, graphSize, MPI.INT, destination, MPI.ANY_TAG);
            if (result[0] != -1) {
                return result;
            }
        }

        // try remaining colors for next node (on this process)
        for (int currentIndex = colorIndex ; currentIndex < colorsSize; currentIndex++) {
            nextColorCodes = Arrays.copyOf(codes,codes.length);
            nextColorCodes[nextNode] = currentIndex;

            result = graphColoringRecursive(nextNode, graph, colorsSize, nextColorCodes, mpiRank, mpiSize, nextPower);

            if (result[0] != -1) {
                return result;
            }
        }

        // invalid solution (result is an array of -1)
        return result;

    }

    public static void graphColoringChild(int mpiRank, int mpiSize, Graph graph, int colorsSize) {
        int graphSize = graph.size();

        // receive data
        int[] data = new int[3];
        MPI.COMM_WORLD.Recv(data, 0, data.length, MPI.INT, MPI.ANY_SOURCE, MPI.ANY_TAG);

        int parent = data[0];
        int nodeId = data[1];
        int power = data[2];

        int[] indices = new int[graphSize];
        MPI.COMM_WORLD.Recv(indices, 0, graphSize, MPI.INT, MPI.ANY_SOURCE, MPI.ANY_TAG);

        // recursive call
        int[] result = graphColoringRecursive(nodeId, graph, colorsSize, indices, mpiRank, mpiSize, power);

        // send data to parent
        MPI.COMM_WORLD.Send(result, 0, graphSize, MPI.INT, parent, 0);

    }

    private static boolean valid( int node, int[] codes, Graph graph) {
        for (int current = 0; current < node; current++) {
            if ((graph.has(node, current) || graph.has(current, node)) && codes[node] == codes[current]) {
                return false;
            }
        }
        return true;
    }
}