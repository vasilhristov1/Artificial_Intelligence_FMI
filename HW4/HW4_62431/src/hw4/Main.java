package hw4;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in); // Scanner object to take the input

        System.out.println("\t\t\t\t\t\t\tTic-tac-toe"); // prints the name of the game
        System.out.println("\n========================================================================\n");
        // asks the player who wants to be first
        System.out.println("Who is first? (enter 1 for you, enter 2 for the bot and enter 0 to exit)");

        int choice; // stores the choice of the player
        String symbol; // stores the symbol which the player chose

        // entering the choice (one of the values in the question above)
        do {
            choice = scan.nextInt();
        } while (choice > 2 || choice < 0);

        // if the choice is 0 the program ends else the game starts
        if (choice != 0) {
            System.out.println("Choose your symbol(X or O): "); // asks the player tp choose his symbol to play with
            scan.nextLine();
            symbol = scan.nextLine();

            TicTacToe game = new TicTacToe(choice, symbol); // creating the game
            int[] cellTurn = new int[2]; // used to get the position of the player turn


            do {
                System.out.println("\nChoose cell: ");
                cellTurn[0] = scan.nextInt(); // enter the row of the cell
                cellTurn[1] = scan.nextInt(); // enter the column of the cell
                game.makeMove(cellTurn); // make player's move in the chosen column
            } while (!game.gameEnds());
        }
    }
}
