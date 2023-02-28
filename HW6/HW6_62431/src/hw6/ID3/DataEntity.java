package hw6.ID3;

import java.util.ArrayList;
import java.util.List;

public class DataEntity {
    private List<String> attrValues;

    public List<String> getAttrValues() {
        return attrValues;
    }

    public void setAttrValues(List<String> attrValues) {
        this.attrValues = attrValues;
    }

    public static DataEntity parseEntity(String line) {
        String[] currentLine = line.split(",");
        DataEntity e = new DataEntity();
        List<String> attrValues = new ArrayList<>();

        for (String str : currentLine) {
            if (str.equals("?")) {
                return null;
            }

            attrValues.add(str);
        }

        e.setAttrValues(attrValues);

        return e;
    }
}
