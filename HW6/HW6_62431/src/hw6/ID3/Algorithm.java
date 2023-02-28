package hw6.ID3;

import java.util.*;

public class Algorithm {
    private static final int K = 10;
    private static final int FOLDS = 10;
    private List<String> attributes;

    public Algorithm() {
        attributes = new ArrayList<>();
        attributes.add("class");
        attributes.add("age");
        attributes.add("menopause");
        attributes.add("tumor-size");
        attributes.add("inv-nodes");
        attributes.add("node-caps");
        attributes.add("deg-malig");
        attributes.add("breast");
        attributes.add("breast-quad");
        attributes.add("irradiat");
    }

    public void crossValidation(List<List<DataEntity>> folds) {
        double sumOfAccuracy = 0.0;

        for (int fIndex = 0; fIndex < FOLDS; fIndex++) {
            List<DataEntity> trainingSet = getTrainingData(folds, fIndex);
            DecisionNode root = decisionTreeConstructor(trainingSet, new HashSet<>());

            int counter = 0;

            for (int i = 0; i < folds.get(fIndex).size(); i++) {
                String prediction = prediction(root, folds.get(fIndex).get(i));
                if (prediction.equals(folds.get(fIndex).get(i).getAttrValues().get(0))) {
                    counter++;
                }
            }

            double accuracy = counter / (double) folds.get(fIndex).size();
            System.out.println(accuracy);
            sumOfAccuracy += accuracy;
        }

        double avgAccuracy = sumOfAccuracy / (double) FOLDS;
        System.out.printf("Average accuracy: %f%n", avgAccuracy);
    }

    private Set<String> getAttributeValues(List<DataEntity> data, int index) {
        Set<String> values = new HashSet<>();

        for (DataEntity e : data) {
            String value = e.getAttrValues().get(index);
            values.add(value);
        }

        return values;
    }

    private String prediction(DecisionNode dNode, DataEntity e) {
        DecisionNode parent = new DecisionNode();

        while (dNode != null && !dNode.getCheckLeaf()) {
            String attributeValue = e.getAttrValues().get(dNode.getAttrIndex());
            parent = dNode;
            dNode = dNode.getChildren().get(attributeValue);
        }

        Map<String, Integer> frequency = new HashMap<>();

        for (int i = 0; i < parent.getDataSet().size(); i++) {
            String value = parent.getDataSet().get(i).getAttrValues().get(0);

            if (frequency.get(value) != null) {
                int v = frequency.get(value) + 1;
                frequency.put(value, v);
            } else {
                frequency.put(value, 1);
            }
        }

        boolean frequencyCheck = frequency.get("no-recurrence-events") >= frequency.get("recurrence-events");

        return (frequencyCheck ? "no-recurrence-events" : "recurrence-events");
    }

    private List<DataEntity> getTrainingData(List<List<DataEntity>> folds, int index) {
        List<DataEntity> data = new ArrayList<>();

        for (int i = 0; i < FOLDS; i++) {
            if (i == index) {
                continue;
            }

            for (int j = 0; j < folds.get(i).size(); j++) {
                data.add(folds.get(i).get(j));
            }
        }

        return data;
    }

    private Map<String, Integer> getValues(List<DataEntity> data, int index) {
        Map<String, Integer> values = new HashMap<>();

        for (DataEntity e : data) {
            String value = e.getAttrValues().get(index);

            if (values.get(value) != null) {
                int cnt = values.get(value) + 1;
                values.put(value, cnt);
            } else {
                values.put(value, 1);
            }
        }

        return values;
    }

    private double calculateAttrEntropy(List<DataEntity> data, int index) {
        double entropy = 0.0;

        for (Map.Entry<String, Integer> entry : getValues(data, index).entrySet()) {
            double p = entry.getValue() / (double) data.size();
            entropy += (-1) * p * Math.log(p);
        }

        return entropy;
    }

    private double calculateClassEntropy(List<DataEntity> data, int index) {
        double entropy = 0.0;

        for (Map.Entry<String, Integer> entry : getValues(data, index).entrySet()) {
            List<DataEntity> splitData = new ArrayList<>();

            for (DataEntity e : data) {
                if (e.getAttrValues().get(index).equals(entry.getKey())) {
                    splitData.add(e);
                }
            }

            double p = entry.getValue() / (double) data.size();
            entropy += p * calculateAttrEntropy(splitData, 0);
        }

        return entropy;
    }

    private double calculateGain(List<DataEntity> data, int index) {
        return calculateAttrEntropy(data, 0) - calculateClassEntropy(data, index);
    }

    private int getMaxGainIndex(List<DataEntity> data, Set<Integer> branchAttrs) {
        double maxGain = -1.0;
        int maxIndex = Integer.MIN_VALUE;

        for (int i = 1; i < 10; i++) {
            if (branchAttrs.contains(i)) {
                continue;
            }

            double currentGain = calculateGain(data, i);

            if (currentGain > maxGain) {
                maxGain = currentGain;
                maxIndex = i;
            }
        }

        return maxIndex;
    }

    private DecisionNode decisionTreeConstructor(List<DataEntity> data, Set<Integer> branchAttrs) {
        DecisionNode dNode = new DecisionNode();

        if (calculateAttrEntropy(data, 0) == 0) {
            dNode.setCheckLeaf(true);
            dNode.setValue(data.get(0).getAttrValues().get(0));

            return dNode;
        }

        if (data.size() < K || branchAttrs.size() == 9) {
            dNode.setCheckLeaf(true);
            Map<String, Integer> values = getValues(data, 0);

            if (values.get("no-recurrence-events") >= values.get("recurrence-events")) {
                dNode.setValue("no-recurrence-events");
            } else {
                dNode.setValue("recurrence-events");
            }

            return dNode;
        }

        int maxIndex = getMaxGainIndex(data, branchAttrs);

        dNode.setAttrIndex(maxIndex);
        dNode.setValue(attributes.get(maxIndex));
        dNode.setCheckLeaf(false);
        dNode.setDataSet(data);

        for (String value : getAttributeValues(data, maxIndex)) {
            List<DataEntity> subData = new ArrayList<>();

            for (DataEntity e : data) {
                if (e.getAttrValues().get(maxIndex).equals(value)) {
                    subData.add(e);
                }
            }

            branchAttrs.add(maxIndex);
            dNode.getChildren().put(value, decisionTreeConstructor(subData, branchAttrs));
        }

        return dNode;
    }
}
