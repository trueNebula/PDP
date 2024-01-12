import mpi.MPI;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class asdasdsa {

    public static void main(String[] args) {
        MPI.Init(args);
        LocalTime start = LocalTime.now();

        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        int[] product = new int[20];

        if (size < 2) {
            System.out.println("This program requires at least 2 processes. Exiting.");
            MPI.Finalize();
            System.exit(1);
        }

        if (rank == 0) {
            // Process 0 holds the first polynomial coefficients
            int[] polynomial1 = {1, 2, 3}; // Example coefficients: 1 + 2x + 3x^2
            MPI.COMM_WORLD.Bcast(polynomial1, 0, polynomial1.length, MPI.INT, 0);

            // Initialize the product array on the root process
            product = new int[polynomial1.length * size];
        }

        // All processes receive the first polynomial coefficients
        int[] polynomial1 = new int[3];
        MPI.COMM_WORLD.Bcast(polynomial1, 0, polynomial1.length, MPI.INT, 0);

        // Each process computes its portion of the product
        int[] polynomial2 = {4, 5}; // Example coefficients: 4 + 5x
        int[] productPart = multiplyPolynomials(polynomial1, polynomial2);

        // All processes send their partial results to the root process
        MPI.COMM_WORLD.Gather(productPart, 0, productPart.length, MPI.INT, product, 0, productPart.length, MPI.INT, 0);

        // Synchronize processes after the gather operation
        MPI.COMM_WORLD.Barrier();

        LocalTime end = LocalTime.now();

        if (rank == 0) {
            // Print the result only from the root process
            System.out.println("Time taken: " + start.until(end, ChronoUnit.MICROS) + "us");

            int degree = product.length - 1;
            System.out.print("Product of the polynomials: ");
            for (int coefficient : product) {
                if (degree > 0) {
                    System.out.print(coefficient + "x^" + degree + " + ");
                } else {
                    System.out.print(coefficient);
                }
                degree--;
            }
            System.out.println();
        }

        MPI.Finalize();
    }

    private static int[] multiplyPolynomials(int[] poly1, int[] poly2) {
        int degree1 = poly1.length - 1;
        int degree2 = poly2.length - 1;
        int[] product = new int[degree1 + degree2 + 1];

        for (int i = 0; i <= degree1; i++) {
            for (int j = 0; j <= degree2; j++) {
                product[i + j] += poly1[i] * poly2[j];
            }
        }

        return product;
    }
}
