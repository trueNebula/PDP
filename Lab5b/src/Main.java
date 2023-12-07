import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    // Helper function to add two polynomials
    private static List<Integer> add(List<Integer> A, List<Integer> B) {
        int n = Math.max(A.size(), B.size());
        List<Integer> result = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            result.add(0);
        }

        for (int i = 0; i < A.size(); ++i) {
            result.set(i, result.get(i) + A.get(i));
        }
        for (int i = 0; i < B.size(); ++i) {
            result.set(i, result.get(i) + B.get(i));
        }
        return result;
    }

    // Helper function to subtract two polynomials
    private static List<Integer> subtract(List<Integer> A, List<Integer> B) {
        int n = Math.max(A.size(), B.size());
        List<Integer> result = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            result.add(0);
        }

        for (int i = 0; i < A.size(); ++i) {
            result.set(i, result.get(i) + A.get(i));
        }
        for (int i = 0; i < B.size(); ++i) {
            result.set(i, result.get(i) - B.get(i));
        }
        return result;
    }

    // Multiply two polynomials using Karatsuba algorithm
    private static List<Integer> karatsuba(List<Integer> A, List<Integer> B) {
        int n = A.size();

        // Base case: if the polynomials are small, use the simple algorithm
        if (n <= 4) {
            List<Integer> result = new ArrayList<>(2 * n - 1);
            for (int i = 0; i < 2 * n - 1; ++i) {
                result.add(0);
            }

            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < n; ++j) {
                    result.set(i + j, result.get(i + j) + A.get(i) * B.get(j));
                }
            }
            return result;
        }

        // Split the polynomials into two halves
        int mid = n / 2;
        List<Integer> AL = A.subList(0, mid);
        List<Integer> AH = A.subList(mid, n);
        List<Integer> BL = B.subList(0, mid);
        List<Integer> BH = B.subList(mid, n);

        // Recursive steps
        List<Integer> P1 = karatsuba(AL, BL);
        List<Integer> P2 = karatsuba(add(AL, AH), add(BL, BH));
        List<Integer> P3 = karatsuba(AH, BH);

        // Combine the results
        List<Integer> result = subtract(subtract(P2, P1), P3);

        // Extend the result to the correct size
        result.addAll(0, Arrays.asList(new Integer[2 * n - 1 - result.size()]));

        // Add the results at the appropriate positions
        for (int i = 0; i < P1.size(); ++i) {
            result.set(i, result.get(i) + P1.get(i));
            if (i + mid < result.size()) {
                result.set(i + mid, result.get(i + mid) + P2.get(i));
            }
            if (i + 2 * mid < result.size()) {
                result.set(i + 2 * mid, result.get(i + 2 * mid) + P3.get(i));
            }
        }

        return result;
    }

    public static void main(String[] args) {
        // Example usage
        List<Integer> A = Arrays.asList(1, 2, 3, 1, 2, 3, 1, 2, 3); // Representing the polynomial 1 + 2x + 3x^2
        List<Integer> B = Arrays.asList(4, 5, 4, 5, 0, 5); // Representing the polynomial 4 + 5x + 6x^2

        // Multiply polynomials
        List<Integer> result = karatsuba(A, B);

        // Print the result
        System.out.print("Result: ");
        for (int coeff : result) {
            System.out.print(coeff + " ");
        }
        System.out.println();
    }
}
