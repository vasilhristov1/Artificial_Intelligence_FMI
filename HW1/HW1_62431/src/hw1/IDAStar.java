package hw1;

import java.util.*;

public class IDAStar {
    static long startTime; // stores the time of the start of the solving process
    private int steps = 0; // stores the number of steps necessary to be reached the goal state

    // method to check if the given puzzle can be solved
    public boolean isSolvable(Node initNode) {
        int inversions = 0; // stores the number of inversions
        int rowOfZero = 0; // stores the index of the row of the 0 tile
        ArrayList<Integer> iNode = new ArrayList<>(); // helps with the counting of the inversions

        // finds the row of the 0 tile and fills the iNode array list
        for (int i = 0; i < initNode.getNodeSize(); i++) {
            for (int j = 0; j < initNode.getNodeSize(); j++) {
                iNode.add(initNode.getState()[i][j]);
                if (initNode.getState()[i][j] == 0) {
                    rowOfZero = i;
                }
            }
        }

        // counts the inversions
        for (int i = 0; i < iNode.size(); i++) {
            if (i < iNode.size() - 1) {
                for (int j = i + 1; j < iNode.size(); j++) {
                    if (iNode.get(i) > iNode.get(j) && iNode.get(j) != 0) {
                        inversions++;
                    }
                }
            }
        }

        // returns if the given board can be solved
        if (initNode.getNodeSize() % 2 == 1) {
            return inversions % 2 == 0;
        } else {
            return (inversions + rowOfZero) % 2 == 1;
        }
    }

    // method ida_star
    public void ida_star(Node initNode, Node finalNode) {
        // if the puzzle can be solved the ida_star algorithm is executed
        if (isSolvable(initNode)) {
            Stack<Node> path = new Stack<>(); // the current search path
            startTime = System.currentTimeMillis(); // setting the start time
            int threshold = finalNode.manhattanDistanceNodes(initNode); // calculate the estimated cost of the cheapest path
            path.push(initNode); // adding the initial node to the current search path

            while (true) {
                int temp = search(path, 0, finalNode, threshold);
                threshold++;
                if (temp == 0) {
                    return;
                }
            }
        } else {
            System.out.println("The entered slide puzzle cannot be solved.");
        }
    }

    private int search(Stack<Node> path, int g, Node finalNode, int threshold) {
        Node currentNode = path.peek(); // the last node in the current path
        int f = g + finalNode.manhattanDistanceNodes(currentNode); // the estimated cost of the cheapest path
        if (f > threshold) return f;

        // if the manhattan distance of the current node is 0
        // then the goal state is reached
        if (currentNode.manhattanDistanceNodes(finalNode) == 0) {
            long timeInMillis = System.currentTimeMillis() - startTime; // stores the time from the start of the searching to the time when the goal state is reached

            System.out.println(steps); // prints the number of the necessary steps to reach the goal state

            // prints all movements
            for (Node n : path) {
                if (n.getMovement() != null) {
                    System.out.println(n.getMovement());
                }
            }
            System.out.println("Process finished for " + String.format("%.2f", (timeInMillis / 1000.00))); // prints the time for which the solution is found
            return 0;
        }

        int min = Integer.MAX_VALUE;
        List<Node> children = getChildren(currentNode);
        for (Node child : children) {
            path.push(child);
            steps++;
            int t = search(path, g + 1, finalNode, threshold);
            if (t == 0) return 0;
            if (t < min) min = t;
            path.pop();
            steps--;
        }
        return min;
    }

    public List<Node> getChildren(Node parent) {
        List<Node> children = new ArrayList<>(); // a list of all children of the parent node

        // checks if a tile can be slide up and
        // if the previous movement is not down so that it will not go back in the previous position
        if (parent.checkUp() && (parent.getMovement() == null || !parent.getMovement().equals("down"))) {
            int[][] stateUp = parent.moveUp(); // the state of the parent when there is sliding up
            Node nodeUp = new Node(); // the child node with the up state
            nodeUp.setState(stateUp); // setting the up state to the child node
            nodeUp.setNodeSize(parent.getNodeSize()); // setting the size to the child node
            nodeUp.setMovement("up"); // setting the movement of the child node

            children.add(nodeUp); // adding the child node nodeUp to the children list
        }

        // checks if a tile can be slide down and
        // if the previous movement is not up so that it will not go back in the previous position
        if (parent.checkDown() && (parent.getMovement() == null || !parent.getMovement().equals("up"))) {
            int[][] stateDown = parent.moveDown(); // the state of the parent when there is sliding down
            Node nodeDown = new Node(); // the child node with the down state
            nodeDown.setState(stateDown); // setting the down state to the child node
            nodeDown.setNodeSize(parent.getNodeSize()); // setting the size to the child node
            nodeDown.setMovement("down"); // setting the movement of the child node

            children.add(nodeDown); // adding the child node nodeDown to the children list
        }

        // checks if a tile can be slide left and
        // if the previous movement is not right so that it will not go back in the previous position
        if (parent.checkLeft() && (parent.getMovement() == null || !parent.getMovement().equals("right"))) {
            int[][] stateLeft = parent.moveLeft(); // the state of the parent when there is sliding left
            Node nodeLeft = new Node(); // the child node with the left state
            nodeLeft.setState(stateLeft); // setting the left state to the child node
            nodeLeft.setNodeSize(parent.getNodeSize()); // setting the size to the child node
            nodeLeft.setMovement("left"); // setting the movement of the child node

            children.add(nodeLeft); // adding the child node nodeLeft to the children list
        }

        // checks if a tile can be slide right and
        // if the previous movement is not left so that it will not go back in the previous position
        if (parent.checkRight() && (parent.getMovement() == null || !parent.getMovement().equals("left"))) {
            int[][] stateRight = parent.moveRight(); // the state of the parent when there is sliding right
            Node nodeRight = new Node(); // the child node with the right state
            nodeRight.setState(stateRight); // setting the right state to the child node
            nodeRight.setNodeSize(parent.getNodeSize()); // setting the size to the child node
            nodeRight.setMovement("right"); // setting the movement of the child node

            children.add(nodeRight); // adding the child node nodeRight to the children list
        }
        return children;
    }

}