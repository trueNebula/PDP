package Threads;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GraphColoring {
    public static Map<Integer,String> graphColoringMain(int threadsNumber, Graph graph, Colors colors) throws Exception {
        Vector<Integer> codes = new Vector<>();

        int codesNumber = colors.size();

        Vector<Integer> partialIndices = new Vector<>(Collections.nCopies(graph.size(),0));
        Lock lock = new ReentrantLock();

        graphColoringRecursive(new AtomicInteger(threadsNumber), 0, graph, codesNumber, partialIndices, lock, codes);

        if (codes.isEmpty()) {
            throw new Exception("No solution found!");
        }

        return colors.getColorsForIndices(codes);
    }

    private static void graphColoringRecursive(AtomicInteger threadsNumber, int nodeId, Graph graph, int codesNumber, Vector<Integer> partialIndices, Lock lock, Vector<Integer>codes) {
        // base case: we already have a solution
        if (!codes.isEmpty()) {
            return;
        }

        // base case: valid solution
        if (nodeId+1 == graph.size()) {
            if (valid(nodeId, partialIndices, graph)) {
                lock.lock();
                if (codes.isEmpty()) {
                    codes.addAll(partialIndices);
                }
                lock.unlock();
            }
            return;
        }

        // find possible colors for the next node
        int nextNode = nodeId + 1;

        List<Thread> threads = new ArrayList<>();
        List<Integer> validColors = new ArrayList<>();

        for (int code = 0; code < codesNumber; code++) {
            partialIndices.set(nextNode, code);
            if (valid(nextNode, partialIndices, graph)) {
                if (threadsNumber.getAndDecrement() > 0 ) {
                    Vector<Integer> nextPartialIndices = new Vector<>(partialIndices);
                    Thread thread = new Thread(() -> graphColoringRecursive(threadsNumber, nextNode, graph, codesNumber, nextPartialIndices, lock, codes));

                    thread.start();
                    threads.add(thread);
                }
                else {
                    validColors.add(code);
                }
            }
        }

        // join threads
        for (Thread thread : threads) {
            try {
                thread.join();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (int code : validColors) {
            partialIndices.set(nextNode, code);
            Vector<Integer> nextPartialIndices = new Vector<>(partialIndices);
            graphColoringRecursive(threadsNumber, nextNode, graph, codesNumber, nextPartialIndices, lock, codes);
        }

    }

    private static boolean valid (int node, Vector<Integer> codes, Graph graph) {
        for (int current = 0; current < node; current++) {
            if ((graph.has(node,current) || graph.has(current,node)) && codes.get(node) == codes.get(current).intValue()) {
                return false;
            }
        }
        return true;
    }
}