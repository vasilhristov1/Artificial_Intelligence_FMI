package hw4;

import java.util.Random;

public class TicTacToe {
    private String[][] board; // the board of the game
    private String PLAYER_SYMBOL; // the symbol of the player
    private String BOT_SYMBOL; // the symbol of the bot
    private boolean isGameEnd; // used to be checked if the game is over

    // set method to set the player's symbol
    public void setPLAYER_SYMBOL(String player_symbol) {
        this.PLAYER_SYMBOL = player_symbol;
    }

    // set method to set the symbol of the bot
    public void setBOT_SYMBOL(String bot_symbol) {
        this.BOT_SYMBOL = bot_symbol;
    }

    // constructor
    TicTacToe(int firstPlayer, String symbol){
        this.board = new String[3][3]; // setting the size of the game board
        this.isGameEnd = false; // setting that the game is not still over
        this.setPLAYER_SYMBOL(symbol); // set the players symbol

        // check what is the player's symbol so that the other symbol is set for the bot
        if (symbol.equals("X")) {
            this.setBOT_SYMBOL("O");
        } else {
            this.setBOT_SYMBOL("X");
        }

        // initializing the board with empty cells(_)
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[0].length; j++){
                this.board[i][j] = "_";
            }
        }

        int playerFirst = 1; // this is the value if the player is first
        int botFirst = 2; // this is the value if the bot is first

        // checks whose turn is chosen to be first the one of the player or the one of the bot
        if (firstPlayer == playerFirst){
            System.out.println("The first move is yours!");
        } else if (firstPlayer == botFirst){
            System.out.println("Bot makes the first move.");
            Random random = new Random(); // random object to get random numbers
            int row = random.nextInt(board.length); // random selected row
            int col = random.nextInt(board[0].length); // random selected column
            this.board[row][col] = this.BOT_SYMBOL; // initializing a random cell with the move of the bot
            printBoard(); // printing the board after the first move of the bot
        }
    }

    // method to print the board
    private void printBoard(){
        for(int i = 0; i < this.board.length; i++){
            for(int j = 0; j < this.board[0].length; j++){
                System.out.print(this.board[i][j] + " ");
            }
            System.out.println();
        }
    }

    // method that checks if there is a winner and returns the symbol of the winner line
    private String winChecker(){
        String winSymbol = ""; // stores the symbol of the winner

        // checks if there is a horizontal win
        // one of the board elements in the horizontal line should be different from an empty cell
        // so that the empty cells are not considered as a win
        for (int i = 0; i < this.board.length; i++){
            if (!this.board[i][0].equals("_") &&
                    this.board[i][0].equals(this.board[i][1]) &&
                    this.board[i][1].equals(this.board[i][2])){
                winSymbol = this.board[i][0];
            }
        }

        // checks if there is a vertical win
        // one of the board elements in the vertical line should be different from an empty cell
        // so that the empty cells are not considered as a win
        for (int i = 0; i < this.board[0].length; i++){
            if (!this.board[0][i].equals("_") &&
                    this.board[0][i].equals(this.board[1][i]) &&
                    this.board[1][i].equals(this.board[2][i])){
                winSymbol = this.board[0][i];
            }
        }

        // checks if there is a win by the main diagonal
        // one of the board elements in the diagonal should be different from an empty cell
        // so that the empty cells are not considered as a win
        if  (!this.board[0][0].equals("_") &&
                this.board[0][0].equals(this.board[1][1]) &&
                this.board[1][1].equals(this.board[2][2])){
            winSymbol = this.board[0][0];
        }

        // checks if there is a win by the second diagonal
        // one of the board elements in the diagonal should be different from an empty cell
        // so that the empty cells are not considered as a win
        if  (!this.board[0][2].equals("_") &&
                this.board[0][2].equals(this.board[1][1]) &&
                this.board[1][1].equals(this.board[2][0])){
            winSymbol = this.board[0][2];
        }

        return winSymbol;
    }

    // method to make the move of the player
    public void playerMove(int row, int col){
        this.board[row - 1][col - 1] = this.PLAYER_SYMBOL; // set the cell chosen by the player to be equal to his symbol
        this.printBoard(); // printing the board after the players move
    }

    // method to get the move of the bot
    private int[] botMove(){
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        int[] move = new int[2]; // stores the cell turn of the bot

        for (int i = 0; i < this.board.length; i++){
            for (int j = 0; j < this.board[0].length; j++){
                // if the current cell is empty
                if (this.board[i][j].equals("_")){
                    this.board[i][j] = this.BOT_SYMBOL; // set the symbol of the bot on this cell
                    // do the minimax algorithm to get the value of this move
                    int moveValue = this.minimax(this.board, 0, false, alpha, beta);
                    this.board[i][j] = "_"; // makes the current cell empty again
                    // if the value of this move is more than alpha(the best value) then it is the right move to be done
                    if (moveValue > alpha) {
                        alpha = moveValue; // make this current move value to be alpha(the best value)
                        move[0] = i; // the row of the move of the bot
                        move[1] = j; // the column of the move of the bot
                    }
                }
            }
        }

        return move;
    }

    // the min-max algorithm
    private int minimax(String[][] board, int depth, boolean minimize, int alpha, int beta){
        String winner = this.winChecker(); // stores the symbol of the winner

        // if there is a winner
        if (!winner.isEmpty()){
            // if the winner is the player
            if (winner.equals(this.PLAYER_SYMBOL)){
                return depth - 10;
            }
            // if the winner is the bot
            else if (winner.equals(this.BOT_SYMBOL)){
                return 10 - depth;
            }
        }

        // if the game ends
        if (checkForEndOfGame()){
            return 0;
        }

        if (minimize){
            for (int i = 0; i < this.board.length; i++){
                for (int j = 0; j < this.board[0].length; j++){
                    // if the current cell is empty
                    if (board[i][j].equals("_")){
                        board[i][j] = this.BOT_SYMBOL; // set the symbol of the bot on this cell
                        // do the minimax algorithm to get the value of this move when the next move is the player's one
                        int best = this.minimax(board, depth + 1, false, alpha, beta);
                        board[i][j] = "_"; // makes the current cell empty again
                        // if the value of this move is more than or equal to beta then it is the right move to be done
                        if (best >= beta){
                            return best;
                        }
                        alpha = Math.max(best, alpha); // alpha is equal to the bigger value between best and the current alpha
                    }
                }
            }
            return alpha;
        } else {
            for (int i = 0; i < this.board.length; i++){
                for (int j = 0; j < this.board[0].length; j++){
                    // if the current cell is empty
                    if (board[i][j].equals("_")){
                        board[i][j] = this.PLAYER_SYMBOL; // set the symbol of the player on this cell
                        // do the minimax algorithm to get the value of this move when the next move is of the bot
                        int best = this.minimax(board, depth + 1, true, alpha, beta);
                        board[i][j] = "_"; // makes the current cell empty again
                        // if the value of this move is less than or equal to alpha then it is the right move to be done
                        if (best <= alpha){
                            return best;
                        }
                        beta = Math.min(best, beta); // beta is equal to the lower value between best and the current beta
                    }
                }
            }
            return beta;
        }
    }

    // method to make move
    public void makeMove(int[] cellTurn){
        // checks if the entered cell is empty, or it is already taken
        if (!this.board[cellTurn[0] - 1][cellTurn[1] - 1].equals("_")){
            System.out.println("Select other valid cell.");
            return;
        }

        this.playerMove(cellTurn[0], cellTurn[1]); // make the player move
        String winnerSymbol = this.winChecker(); // if there is a winner it stores the winner's symbol

        if (!winnerSymbol.isEmpty()){
            this.isGameEnd = true; // if there is a winner symbol the game ends

            // check whose symbol is the winning one so that the program prints the correct message
            if (winnerSymbol.equals(this.PLAYER_SYMBOL)){
                System.out.println("\nCongratulations! You are the winner.");
            } else {
                System.out.println("\nSorry! You lost to the bot.");
            }
        } else if (checkForEndOfGame()){
            this.isGameEnd = true; // if all the cells are filled and there is no winner then the game ends, and it is a draw
            System.out.println("\nGame ended! It's a draw!");
        } else {
            // if neither the game ended nor there was a winner then it is the turn of the bot
            System.out.println("\nBot turn.");
            int[] move = this.botMove(); // takes the bot move
            this.board[move[0]][move[1]] = this.BOT_SYMBOL; // sets the cell of the board to be the symbol of the bot
            printBoard(); // printing the board after the turn of the bot
            winnerSymbol = winChecker(); // if there is a winner it stores the winner's symbol
            if (winnerSymbol.equals(this.BOT_SYMBOL)){
                this.isGameEnd = true; // if the winner symbol equals to the symbol of the bot the game ends
                System.out.println("\nSorry! You lost to the bot.");
            } else {
                this.isGameEnd = this.checkForEndOfGame(); // if there is no winner it checks if the game can continue
                if (this.isGameEnd) {
                    System.out.println("\nGame ended! It's a draw!");
                }
            }
        }
    }

    // method to return if the game ends
    public boolean gameEnds(){
        return this.isGameEnd;
    }

    // method to check if all cells are filled
    // if one cell is empty the game has not ended yet
    private boolean checkForEndOfGame(){
        for (int i = 0; i < this.board.length; i++){
            for (int j = 0; j < this.board[0].length; j++){
                if (this.board[i][j].equals("_")){
                    return false;
                }
            }
        }
        return true;
    }
}
