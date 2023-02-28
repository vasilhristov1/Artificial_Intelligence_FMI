package hw6.ID3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DecisionNode {
    private List<DataEntity> dataSet;
    private int attrIndex;
    private String value;
    private boolean checkLeaf;
    private Map<String, DecisionNode> children;

    public DecisionNode() {
        this.dataSet = new ArrayList<>();
        this.children = new HashMap<>();
    }

    public List<DataEntity> getDataSet() {
        return this.dataSet;
    }

    public void setDataSet(List<DataEntity> dataSet) {
        this.dataSet = dataSet;
    }

    public int getAttrIndex() {
        return this.attrIndex;
    }

    public void setAttrIndex(int attrIndex) {
        this.attrIndex = attrIndex;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean getCheckLeaf() {
        return this.checkLeaf;
    }

    public void setCheckLeaf(boolean checkLeaf) {
        this.checkLeaf = checkLeaf;
    }

    public Map<String, DecisionNode> getChildren() {
        return this.children;
    }
}
