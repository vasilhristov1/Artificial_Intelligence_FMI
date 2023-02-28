package hw2;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in); // Scanner object to read the input
        int N = scan.nextInt(); // entering the value of the variable of the number of the queens

        long start = System.currentTimeMillis(); // stores the starting time
        NQueens board = new NQueens(N); // creating the N by N board of queens
        board.findSolution(); // finding the solution to the task

        double time = (System.currentTimeMillis() - start) / 1000.0;

        if (N < 50) {
            board.print();
        }

        // printing the time for finding the solution
        System.out.printf("Process finished for %.2f seconds.%n", time);
    }
}
