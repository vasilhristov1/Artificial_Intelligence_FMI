package hw5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NaiveBayesClassifier {
    private List<String[]> dataSet;
    private int rCount;
    private int dCount;
    private int[][] rPTable;
    private int[][] dPTable;

    public NaiveBayesClassifier() {
        dataSet = new ArrayList<>();
        ReadFile rf = new ReadFile(dataSet);

        setRCount(0);
        setDCount(0);

        rPTable = new int[3][16];
        dPTable = new int[3][16];
    }

    public void setRCount(int rCount) {
        this.rCount = rCount;
    }

    public void setDCount(int dCount) {
        this.dCount = dCount;
    }

    public List<String[]> getDataSet() {
        return dataSet;
    }

    public int getRCount() {
        return rCount;
    }

    public int getDCount() {
        return dCount;
    }

    public void train(int iter, int testSetSize) {
        for (int i = 0; i < testSetSize * iter; i++) {
            trainElem(dataSet.get(i));
        }

        for (int i = testSetSize * (iter + 1); i < dataSet.size(); i++) {
            trainElem(dataSet.get(i));
        }
    }

    private void trainElem(String[] line) {
        if(line[0].equals("democrat")) {
            dCount++;

            for(int i = 1; i < line.length; i++) {
                switch(line[i]) {
                    case "y":
                        dPTable[0][i-1]++;
                        break;
                    case "n":
                        dPTable[1][i-1]++;
                        break;
                    case "?":
                        dPTable[2][i-1]++;
                        break;
                }
            }
        } else {
            rCount++;

            for(int i = 1; i < line.length; i++) {
                switch(line[i]) {
                    case "y":
                        rPTable[0][i-1]++;
                        break;
                    case "n":
                        rPTable[1][i-1]++;
                        break;
                    case "?":
                        rPTable[2][i-1]++;
                        break;
                }
            }
        }
    }

    private boolean testElem(String[] line) {
        double rP = Math.log((getRCount() + 1) / (double)(getRCount() + getDCount() + 2));
        double dP = Math.log((getDCount() + 1) / (double)(getRCount() + getDCount() + 2));

        for (int i = 1; i < line.length; i++) {
            switch(line[i]) {
                case "y":
                    rP += (rPTable[0][i-1] + 1) / (double)(getRCount() + 2);
                    dP += (dPTable[0][i-1] + 1) / (double)(getDCount() + 2);
                case "n":
                    rP += (rPTable[1][i-1] + 1) / (double)(getRCount() + 2);
                    dP += (dPTable[1][i-1] + 1) / (double)(getDCount() + 2);
                case "?":
                    rP += (rPTable[2][i-1] + 1) / (double)(getRCount() + 2);
                    dP += (dPTable[2][i-1] + 1) / (double)(getDCount() + 2);
            }
        }

        return line[0].equals("democrat") ? dP >= rP : rP >= dP;
    }

    double accuracy(int iter, int testSetSize) {
        double predictions = 0.0;

        for (int i = testSetSize * iter; i < testSetSize * (iter + 1); i++) {
            predictions += testElem(dataSet.get(i)) ? 1 : 0;
        }

        return predictions * 100 / testSetSize;
    }

    public void test() {
        int testSetSize = getDataSet().size() / 10;
        double sumOfAccuracy = 0.0;
        for (int iteration = 0; iteration < 10; iteration++) {
            Collections.shuffle(getDataSet());
            train(iteration, testSetSize);
            double accuracy = accuracy(iteration, testSetSize);
            System.out.printf("Iteration %d : %f%% %n", iteration, accuracy);
            sumOfAccuracy += accuracy;
        }

        double avgAccuracy = sumOfAccuracy / 10.0;

        System.out.printf("Average accuracy: %f %n", avgAccuracy);
    }
}
