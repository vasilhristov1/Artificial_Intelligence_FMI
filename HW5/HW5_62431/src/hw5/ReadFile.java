package hw5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class ReadFile {
    public ReadFile(List<String[]> dataSet) {
        File file = new File("src\\hw5\\data\\house-votes-84.data");
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String set;
            while ((set = bufferedReader.readLine()) != null) {
                String[] splitSet = set.split(",");
                if (splitSet.length != 17) {
                    continue;
                }
                dataSet.add(splitSet);
            }
        } catch (IOException exception) {
            System.err.println("Cannot read from file" + file.getPath());
        }
    }
}
