package hw1;

import java.util.Arrays;

public class Node {
    int [][] state;
    int nodeSize;
    String movement;

    // constructor
    public Node() {
        this.state = null;
        this.nodeSize = 0;
        this.movement = null;
    }

    // get method for the state of the node
    public int[][] getState() {
        return state;
    }

    // set method for the state of the node
    public void setState(int[][] state) {
        this.state = state;
    }

    // get method for the number of rows
    public int getNodeSize() {
        return nodeSize;
    }

    // set method for the number of rows
    public void setNodeSize(int nodeSize) {
        this.nodeSize = nodeSize;
    }

    // get method for the movement
    public String getMovement() {
        return movement;
    }

    // set method for the movement
    public void setMovement(String movement) {
        this.movement = movement;
    }

    // function to check if we can slide up
    public boolean checkUp() {
        int[] zeroPosCoordinates = getNumberPosition(0); // gets the position of the zero tile of the current state
        int zeroPosition = zeroPosCoordinates[0] * this.state.length + zeroPosCoordinates[1]; // the position of the zero tile with one number
        return zeroPosition / this.state.length < this.state.length - 1;
    }

    // function to check if we can slide down
    public boolean checkDown() {
        int[] zeroPosCoordinates = getNumberPosition(0); // gets the position of the zero tile of the current state
        int zeroPosition = zeroPosCoordinates[0] * this.state.length + zeroPosCoordinates[1]; // the position of the zero tile with one number
        return zeroPosition / this.state.length > 0;
    }

    // function to check if we can slide to the left
    public boolean checkLeft() {
        int[] zeroPosCoordinates = getNumberPosition(0); // gets the position of the zero tile of the current state
        int zeroPosition = zeroPosCoordinates[0] * this.state.length + zeroPosCoordinates[1]; // the position of the zero tile with one number
        return zeroPosition % this.state.length < this.state.length - 1;
    }

    // function to check if we can slide to the right
    public boolean checkRight() {
        int[] zeroPosCoordinates = getNumberPosition(0); // gets the position of the zero tile of the current state
        int zeroPosition = zeroPosCoordinates[0] * this.state.length + zeroPosCoordinates[1]; // the position of the zero tile with one number
        return zeroPosition % this.state.length > 0;
    }

    // function to get the position of a certain tile
    public int[] getNumberPosition(int number) {
        int[] position = new int[2]; // stores the result position
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state.length; j++) {
                if (this.state[i][j] == number) {
                    position[0] = i; // the row position
                    position[1] = j; // the column position
                }
            }
        }
        return position;
    }

    // method to calculate the total manhattan distance
    public int manhattanDistanceNodes(Node node) {
        int result = 0; // stores the result manhattan distance
        int[] currentNodeNumPos; // stores the position of the number of the current state
        int[] otherNodeNumPos; // stores the position of the number of the other state

        // loop to calculate the distance
        for (int i = 1; i < this.state.length * this.state.length; i++) {
            currentNodeNumPos = this.getNumberPosition(i);
            otherNodeNumPos = node.getNumberPosition(i);
            result += Math.abs(currentNodeNumPos[0] - otherNodeNumPos[0]) + Math.abs(currentNodeNumPos[1] - otherNodeNumPos[1]);
        }
        return result;
    }

    // method to slide a tile up
    public int[][] moveUp() {
        int[] zeroPosition = getNumberPosition(0); // gets the position of the zero tile of the current state
        int numToMove = this.state[zeroPosition[0] + 1][zeroPosition[1]]; // gets the number that will be moved up
        int[][] newState = copyState(this.getState()); // copying current state to the new state
        newState[zeroPosition[0]][zeroPosition[1]] = numToMove; // set the number to the zero position
        newState[zeroPosition[0] + 1][zeroPosition[1]] = 0; // set zero to the number position
        return newState;
    }

    // method to slide a tile down
    public int[][] moveDown() {
        int[] zeroPosition = getNumberPosition(0); // gets the position of the zero tile of the current state
        int numToMove = this.state[zeroPosition[0] - 1][zeroPosition[1]]; // gets the number that will be moved down
        int[][] newState = copyState(this.getState()); // copying current state to the new state
        newState[zeroPosition[0]][zeroPosition[1]] = numToMove; // set the number to the zero position
        newState[zeroPosition[0] - 1][zeroPosition[1]] = 0; // set zero to the number position
        return newState;
    }

    // method to slide a tile to the left
    public int[][] moveLeft() {
        int[] zeroPosition = getNumberPosition(0); // gets the position of the zero tile of the current state
        int numToMove = this.state[zeroPosition[0]][zeroPosition[1] + 1]; // gets the number that will be moved left
        int[][] newState = copyState(this.getState()); // copying current state to the new state
        newState[zeroPosition[0]][zeroPosition[1]] = numToMove; // set the number to the zero position
        newState[zeroPosition[0]][zeroPosition[1]  + 1] = 0; // set zero to the number position
        return newState;
    }

    // method to slide a tile to the right
    public int[][] moveRight() {
        int[] zeroPosition = getNumberPosition(0); // gets the position of the zero tile of the current state
        int numToMove = this.state[zeroPosition[0]][zeroPosition[1] -1]; // gets the number that will be moved right
        int[][] newState = copyState(this.getState()); // copying current state to the new state
        newState[zeroPosition[0]][zeroPosition[1]] = numToMove; // set the number to the zero position
        newState[zeroPosition[0]][zeroPosition[1] - 1] = 0; // set zero to the number position
        return newState;
    }

    // method to copy the state of a node
    private int[][] copyState(int[][] currentState) {
        int[][] copiedState  = new int[nodeSize][nodeSize];
        for (int i = 0; i < currentState.length; i++) {
            System.arraycopy(currentState[i], 0, copiedState[i], 0, currentState.length);
        }
        return copiedState ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Arrays.deepEquals(state, node.state);
    }
}
