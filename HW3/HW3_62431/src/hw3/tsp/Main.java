package hw3.tsp;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int N;
        do {
            N = scan.nextInt();
        } while (N > 100 || N <= 0);

        TSPSolver solver = new TSPSolver(N);
        solver.solve();
    }
}
