import mpi.MPI;

public class Main {
    static final int POLYNOMIAL_SIZE_P1 = 101;
    static final int POLYNOMIAL_SIZE_P2 = 101;

    public static void master(Polynomial p1, Polynomial p2, int processes) {
        long startTime = System.nanoTime();
        // length of data for each process to handle
        int length = p1.getSize() / (processes - 1);

        int start;
        int end = 0;
        // going through each worker process and sending the data
        for (int i = 1; i < processes; i++) {
            start = end;
            end = start + length;
            if (i == processes - 1) {
                end = p1.getSize();
            }

            MPI.COMM_WORLD.Send(new Object[]{p1}, 0, 1, MPI.OBJECT, i, 0);
            MPI.COMM_WORLD.Send(new Object[]{p2}, 0, 1, MPI.OBJECT, i, 0);
            MPI.COMM_WORLD.Send(new int[]{start}, 0, 1, MPI.INT, i, 0);
            MPI.COMM_WORLD.Send(new int[]{end}, 0, 1, MPI.INT, i, 0);
        }

        // getting the results
        Object[] results = new Object[processes - 1];
        for (int i = 1; i < processes; i++) {
            MPI.COMM_WORLD.Recv(results, i - 1, 1, MPI.OBJECT, i, 0);
        }

        // transforming the results
        Polynomial result = Utils.getResult(results);

        long endTime = System.nanoTime();
        double time = (endTime - startTime) / 1000000000.0;
        System.out.println("WORKER" + ":\n");
        System.out.println("Execution time: " + time);
        System.out.println(result + "\n");
    }

    public static void regularWorker(int me) {
        Object[] p1 = new Object[2];
        Object[] p2 = new Object[2];
        int[] start = new int[1];
        int[] end = new int[1];

        MPI.COMM_WORLD.Recv(p1, 0, 1, MPI.OBJECT, 0, 0);
        MPI.COMM_WORLD.Recv(p2, 0, 1, MPI.OBJECT, 0, 0);
        MPI.COMM_WORLD.Recv(start, 0, 1, MPI.INT, 0, 0);
        MPI.COMM_WORLD.Recv(end, 0, 1, MPI.INT, 0, 0);

        Polynomial pol1 = (Polynomial) p1[0];
        Polynomial pol2 = (Polynomial) p2[0];

        Polynomial result = Utils.multiplySequence(pol1, pol2, start[0], end[0]);

        MPI.COMM_WORLD.Send(new Object[]{result}, 0, 1, MPI.OBJECT, 0, 0);
    }

    public static void karatsubaWorker(int me) {
        Object[] p1 = new Object[2];
        Object[] p2 = new Object[2];
        int[] start = new int[1];
        int[] end = new int[1];

        MPI.COMM_WORLD.Recv(p1, 0, 1, MPI.OBJECT, 0, 0);
        MPI.COMM_WORLD.Recv(p2, 0, 1, MPI.OBJECT, 0, 0);
        MPI.COMM_WORLD.Recv(start, 0, 1, MPI.INT, 0, 0);
        MPI.COMM_WORLD.Recv(end, 0, 1, MPI.INT, 0, 0);

        Polynomial pol1 = (Polynomial) p1[0];
        Polynomial pol2 = (Polynomial) p2[0];

        for (int i = 0; i < start[0]; i++) {
            pol1.getCoefficients().set(i, 0);
        }
        for (int j = end[0]; j < pol1.getCoefficients().size(); j++) {
            pol1.getCoefficients().set(j, 0);
        }

        Polynomial result = Utils.KaratsubaSequential(pol1, pol2);

        MPI.COMM_WORLD.Send(new Object[]{result}, 0, 1, MPI.OBJECT, 0, 0);
    }

    // Main method for execution
    public static void main(String[] args) {
        System.out.println("Regular Algorithm");
        // MPI initialization
        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        if (me == 0) {
            // Master process
            System.out.println("Master process: \n");
            Polynomial p1 = new Polynomial(POLYNOMIAL_SIZE_P1);
            p1.generateCoefficients();
            Polynomial p2 = new Polynomial(POLYNOMIAL_SIZE_P2);
            p2.generateCoefficients();

            System.out.println("Polynomial 1:" + p1);
            System.out.println("Polynomial 2:" + p2);
            System.out.println("\n");

            master(p1, p2, size);
        } else {
            // Worker processes
            regularWorker(me);
        }

        // MPI finalization
        MPI.Finalize();

        System.out.println("\nKaratsuba Algorithm");
        // MPI initialization
        MPI.Init(args);
        me = MPI.COMM_WORLD.Rank();
        size = MPI.COMM_WORLD.Size();

        if (me == 0) {
            // Master process
            System.out.println("Master process: \n");
            Polynomial p1 = new Polynomial(POLYNOMIAL_SIZE_P1);
            p1.generateCoefficients();
            Polynomial p2 = new Polynomial(POLYNOMIAL_SIZE_P2);
            p2.generateCoefficients();

            System.out.println("Polynomial 1:" + p1);
            System.out.println("Polynomial 2:" + p2);
            System.out.println("\n");

            master(p1, p2, size);
        } else {
            // Worker processes
            karatsubaWorker(me);
        }

        // MPI finalization
        MPI.Finalize();
    }
}