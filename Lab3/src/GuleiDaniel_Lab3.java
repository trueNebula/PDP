import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.time.LocalTime;



public class Main {
    public static int computeMatrixElement(int[][] matrixA, int[][] matrixB, int row, int col, int matrixSize) {
        int result = 0;

        for (int k = 0; k < matrixSize; k++) {
            result += matrixA[row][k] * matrixB[k][col];
        }

        return result;
    }

    public static Runnable createRowWiseTask(int[][] matrixA, int[][] matrixB, int[][] resultMatrix,
                                             int taskIndex, int numTasks, int matrixSize) {
        return () -> {
            int matrixSpaces = matrixSize * matrixSize;
            int spacesPerTask = matrixSpaces / numTasks;
            int startRow = (taskIndex * spacesPerTask) / matrixSize;
            int startIndex = (taskIndex * spacesPerTask) % matrixSize;
            int endRow = ((taskIndex + 1) * spacesPerTask - 1) / matrixSize;
            int endIndex;

            if(taskIndex == numTasks - 1)
                endIndex = matrixSize - 1;
            else
                endIndex = ((taskIndex + 1) * spacesPerTask - 1) % matrixSize;

            System.out.println("Task #" + taskIndex + " computing from row " + startRow + " index " + startIndex +
                    " to row " + endRow + " index " + endIndex);

            for (int j = startIndex; j < matrixSize; j++) {
                resultMatrix[startRow][j] = computeMatrixElement(matrixA, matrixB, startRow, j, matrixSize);
            }

            for (int i = startRow + 1; i < endRow; i++) {
                for (int j = 0; j < matrixSize; j++) {
                    resultMatrix[i][j] = computeMatrixElement(matrixA, matrixB, i, j, matrixSize);
                }
            }

            for (int j = 0; j <= endIndex; j++) {
                resultMatrix[endRow][j] = computeMatrixElement(matrixA, matrixB, endRow, j, matrixSize);
            }
        };
    }

    public static Runnable createColumnWiseTask(int[][] matrixA, int[][] matrixB, int[][] resultMatrix,
                                                int taskIndex, int numTasks, int matrixSize) {
        return () -> {
            int matrixSpaces = matrixSize * matrixSize;
            int spacesPerTask = matrixSpaces / numTasks;
            int startCol = (taskIndex * spacesPerTask) / matrixSize;
            int startIndex = (taskIndex * spacesPerTask) % matrixSize;
            int endCol = ((taskIndex + 1) * spacesPerTask - 1) / matrixSize;
            int endIndex;

            if(taskIndex == numTasks - 1)
                endIndex = matrixSize - 1;
            else
                endIndex = ((taskIndex + 1) * spacesPerTask - 1) % matrixSize;

            System.out.println("Task #" + taskIndex + " computing from column " + startCol + " index " + startIndex +
                    " to column " + endCol + " index " + endIndex);

            for (int i = startIndex; i < matrixSize; i++) {
                resultMatrix[i][startCol] = computeMatrixElement(matrixA, matrixB, i, startCol, matrixSize);
            }

            for (int i = startCol + 1; i < endCol; i++) {
                for (int j = 0; j < matrixSize; j++) {
                    resultMatrix[j][i] = computeMatrixElement(matrixA, matrixB, j, i, matrixSize);
                }
            }

            for (int i = 0; i <= endIndex; i++) {
                resultMatrix[i][endCol] = computeMatrixElement(matrixA, matrixB, i, endCol, matrixSize);
            }
        };
    }

    public static Runnable createKthElementTask(int[][] matrixA, int[][] matrixB, int[][] resultMatrix,
                                                int taskIndex, int numTasks, int matrixSize) {
        return () -> {
            int matrixSpaces = matrixSize * matrixSize;
            int currentSpace = taskIndex;

            while (currentSpace < matrixSpaces) {
                int row = currentSpace / matrixSize;
                int col = currentSpace % matrixSize;

                resultMatrix[row][col] = computeMatrixElement(matrixA, matrixB, row, col, matrixSize);
                System.out.println("Task #" + taskIndex + " computing element at row " + row + " column " + col);
                currentSpace += numTasks;
            }
        };
    }

    public static void main(String[] args) {
        int[][] matrixA = {
                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                {1, 2, 3, 4, 5, 6, 7, 8, 9}
        };
        int[][] matrixB = {
                {9, 8, 7, 6, 5, 4, 3, 2, 1},
                {9, 8, 7, 6, 5, 4, 3, 2, 1},
                {9, 8, 7, 6, 5, 4, 3, 2, 1},
                {9, 8, 7, 6, 5, 4, 3, 2, 1},
                {9, 8, 7, 6, 5, 4, 3, 2, 1},
                {9, 8, 7, 6, 5, 4, 3, 2, 1},
                {9, 8, 7, 6, 5, 4, 3, 2, 1},
                {9, 8, 7, 6, 5, 4, 3, 2, 1},
                {9, 8, 7, 6, 5, 4, 3, 2, 1}
        };
        int[][] resultMatrix = {
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
        };

        int matrixSize = 9;
        int numTasks =4;
        ExecutorService executor = Executors.newFixedThreadPool(4);

        LocalTime start = LocalTime.now();
//
//        for (int i = 0; i < 4; i++) {
//            Runnable task = createRowWiseTask(matrixA, matrixB, resultMatrix, i, numTasks, matrixSize);
//            task.run();
//        }

//        for (int i = 0; i < numTasks; i++) {
//            Runnable task = createColumnWiseTask(matrixA, matrixB, resultMatrix, i, numTasks, matrixSize);
//            task.run();
//        }

//        for (int i = 0; i < numTasks; i++) {
//            Runnable task = createKthElementTask(matrixA, matrixB, resultMatrix, i, numTasks, matrixSize);
//            task.run();
//        }
//
//
        for (int i = 0; i < numTasks; i++) {
            Runnable task = createRowWiseTask(matrixA, matrixB, resultMatrix, i, numTasks, matrixSize);
            executor.execute(task);
        }

//        for (int i = 0; i < numTasks; i++) {
//            Runnable task = createColumnWiseTask(matrixA, matrixB, resultMatrix, i, numTasks, matrixSize);
//            executor.execute(task);
//        }
//
//        for (int i = 0; i < 10; i++) {
//            Runnable task = createKthElementTask(matrixA, matrixB, resultMatrix, i, numTasks, matrixSize);
//            executor.execute(task);
//        }

        // Wait for all tasks to finish
        while (!executor.isTerminated()) {
            executor.shutdown();
        }

        LocalTime end = LocalTime.now();

        System.out.println(start.until(end, ChronoUnit.MILLIS));

        // Print the result matrix
        for (int i = 0; i < matrixSize; i++) {
            System.out.println(java.util.Arrays.toString(resultMatrix[i]));
        }

    }
}