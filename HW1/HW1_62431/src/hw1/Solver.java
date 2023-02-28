package hw1;

import java.util.Scanner;

public class Solver {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in); // to get the input

        int N = scan.nextInt(); // gets the number of tiles
        scan.nextLine();
        int I = scan.nextInt(); // gets the position of the 0 tile
        scan.nextLine();

        int boardSize = (int) Math.sqrt(N + 1); // calculates and stores the size of the board
        int[][] initialBoard = new int[boardSize][boardSize]; // the board to be entered

        // input for the tiles of the initial board
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                System.out.print("initialBoard[" + i + "][" + j + "]: ");
                initialBoard[i][j] = scan.nextInt();
            }
        }

        Node initNode = new Node(); // creating the initial node
        initNode.setNodeSize(boardSize); // setting the initial node size to be the size of the initial board
        initNode.setState(initialBoard); // setting the state of the initial node to be the initial board

        int[][] goalBoard = new int[boardSize][boardSize]; // stores the state of the goal board
        int count = 0; // stores the position of the tiles
        int tileNum = 0; // stores the tile number to be added on the board

        // if the position of the empty tile is -1 it should be positioned in the down right corner
        if (I == -1) {
            I = boardSize * boardSize - 1;
        }

        // initialization of the goal board
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (count != I) {
                    count++;
                    tileNum++;
                    goalBoard[i][j] = tileNum;
                } else {
                    count++;
                }
            }
        }

        Node goalNode = new Node(); // creating the goal node
        goalNode.setState(goalBoard); // setting the state of the goal node to be the goal board
        goalNode.setNodeSize(boardSize); // setting the goal node size
        IDAStar solver = new IDAStar(); // creating the IDAStar solver object
        solver.ida_star(initNode, goalNode); // execution of the idaStar method which gives the final result
    }
}
