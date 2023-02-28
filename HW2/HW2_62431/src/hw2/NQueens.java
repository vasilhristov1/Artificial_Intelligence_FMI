package hw2;

import java.util.ArrayList;
import java.util.Random;

public class NQueens {
    private final int N; // stores the number of queens
    private boolean isSolution = false; // used to check if the current state is the solution
    private int[] queensBoard; // represents the board of the queens(the first element is the row of the first queen)
    private int[] queensRow; // stores the number of queens per row
    private int[] mainDiagonalsQueens; // stores the number of queens per main diagonals
    private int[] secondaryDiagonalsQueens; // stores the number of queens per secondary diagonals
    private final Random random = new Random(); // creating Random object

    // constructor of the class NQueens
    public NQueens(int numberQueens) {
        this.N = numberQueens; // initializing the number of queens
        initialize(); // initialize the queens board
    }

    // method to initialize the board of queens
    private void initialize() {
        this.queensBoard = new int[this.N]; // set the number of elements in the queens board to be N
        this.queensRow = new int[this.N]; // set the number of elements in the queens board to be N
        this.mainDiagonalsQueens = new int[2 * this.N - 1]; // set the number of elements to be the number of main diagonals
        this.secondaryDiagonalsQueens = new int[2 * this.N - 1]; // set the number of elements to be the number of secondary diagonals

        // setting all elements of the secondaryDiagonalsQueens,
        // mainDiagonalsQueens and queensRow to be zero
        for (int i = 0; i < 2 * this.N - 1; i++) {
            this.secondaryDiagonalsQueens[i] = 0;
            this.mainDiagonalsQueens[i] = 0;
            if (i < this.N) {
                queensRow[i] = 0;
            }
        }


        for (int column = 0; column < this.N; column++) {
            int row = minConflictsRow(column); // get the row where we have minimum conflicts
            queensBoard[column] = row; // setting a queen at the column and the row with minimum conflicts
            queensRow[row] += 1; // increasing the number of queens on the row with minimum conflicts
            mainDiagonalsQueens[(column - row) + this.N - 1] += 1; // increasing the number of queens on the certain main diagonal
            secondaryDiagonalsQueens[column + row] += 1; // increasing the number of queens on the certain secondary diagonal
        }
    }

    // method to find the column with max conflicts
    private int maxConflictsColumn() {
        int maxConflicts = 0; // stores the maximum number of conflicts
        ArrayList<Integer> columnsWithMaxConflicts = new ArrayList<>(); // stores all the columns with maximum conflicts

        for (int column = 0; column < this.N; column++) {
            int row = queensBoard[column]; // getting the row of the certain queen
            // calculate the number of conflicts
            int conflicts =
                    queensRow[row] + mainDiagonalsQueens[column - row + this.N - 1] + secondaryDiagonalsQueens[column + row] - 3;

            // checks if the current number of conflicts is more than or equal to the max number of conflicts
            // if there are more than one queen with the same max conflicts they are being added to the columnsWithMaxConflicts list
            if (conflicts == maxConflicts) {
                columnsWithMaxConflicts.add(column);
            }
            // else if the current conflicts are more than the max conflicts
            else if (conflicts > maxConflicts) {
                maxConflicts = conflicts; // the maxConflicts is equal to the current number of conflicts
                columnsWithMaxConflicts.clear(); // clear the list columnsWithMaxConflicts
                columnsWithMaxConflicts.add(column); // add the new column with max conflicts to columnsWithMaxConflicts list
            }
        }

        // if the number of max conflicts is 0, return -1
        if (maxConflicts == 0) {
            return -1;
        }

        // returning a random column from the columnsWithMaxConflicts list
        return columnsWithMaxConflicts.get(random.nextInt(columnsWithMaxConflicts.size()));
    }

    // method to find the queen with minimum conflicts
    private int minConflictsRow(int column) {
        int minConflicts = this.N; // set the number of minimum conflicts to be the number of queens
        ArrayList<Integer> queensWithMinConflicts = new ArrayList<>(); // gets the queens with minimum conflicts

        for (int i = 0; i < this.N; i++) {
            int row = queensBoard[column]; // gets the row of the current queen
            if (i != row) {
                // calculate the number of conflicts
                int conflicts =
                        queensRow[i] + mainDiagonalsQueens[column - i + this.N - 1] + secondaryDiagonalsQueens[column + i];
                // checks if the current number of conflicts is more than or equal to the min number of conflicts
                // if there are more than one queen with the same min conflicts they are being added to the queensWithMinConflicts list
                if (conflicts == minConflicts) {
                    queensWithMinConflicts.add(i);
                }
                // else if the current conflicts are less than the min conflicts
                else if (conflicts < minConflicts) {
                    minConflicts = conflicts; // the minConflicts is equal to the current number of conflicts
                    queensWithMinConflicts.clear(); // clear the list queensWithMinConflicts
                    queensWithMinConflicts.add(i); // add the new queen with min conflicts to queensWithMinConflicts list
                }
            }
        }

        // if there are no queens with minimum conflicts, return 0
        if (queensWithMinConflicts.isEmpty()) {
            return 0;
        }

        // returning a random queen from the queensWithMinConflicts list
        return queensWithMinConflicts.get(random.nextInt(queensWithMinConflicts.size()));
    }

    public void findSolution() {
        int maxSteps = 0;

        if (this.N <= 4000) {
            maxSteps = 2 * this.N;
        } else {
            maxSteps = 8500;
        }

        for (int steps = 0; steps < maxSteps; steps++) {
            int column = maxConflictsColumn(); // gets the column with max conflicts

            // if the column with max conflicts is -1 it means that there are no conflicts and the solution is found
            if (column == -1) {
                isSolution = true; // set isSolution to be true
                break;
            }

            int previousRow = queensBoard[column]; // set to be the queensBoard element with the column with max conflicts
            int row = minConflictsRow(column); // gets the row with min conflicts for the column with max conflicts
            queensBoard[column] = row; // setting the queensBoard with the max conflicts column to be the row with min conflicts
            // recalculating conflicts after the change
            queensRow[previousRow] = queensRow[previousRow] - 1;
            queensRow[row] = queensRow[row] + 1;

            mainDiagonalsQueens[column - previousRow + this.N - 1] =
                    mainDiagonalsQueens[column - previousRow + this.N - 1] - 1;
            mainDiagonalsQueens[column - row + this.N - 1] = mainDiagonalsQueens[column - row + this.N - 1] + 1;

            secondaryDiagonalsQueens[column + previousRow] = secondaryDiagonalsQueens[column + previousRow] - 1;
            secondaryDiagonalsQueens[column + row] = secondaryDiagonalsQueens[column + row] + 1;
        }

        // if the solution is not found in the given steps, we initialize the board again and try to find the solution again
        if (!isSolution) {
            initialize();
            findSolution();
        }
    }

    // method to print the solution board
    public void print() {
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.N; j++) {
                // prints " * " when i (row) and the value of the element of the queens board at column j are equal
                // and prints " _ " if not
                if (queensBoard[j] == i) {
                    System.out.print("* ");
                } else {
                    System.out.print("_ ");
                }
            }
            System.out.println();
        }
    }
}