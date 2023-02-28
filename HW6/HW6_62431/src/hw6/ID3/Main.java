package hw6.ID3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final int FOLDS = 10;

    private static void readFromDataFile(List<DataEntity> dataSet) throws FileNotFoundException {
        try (Scanner scan = new Scanner(new File("src/hw6/data/breast-cancer.data"))) {
            while (scan.hasNext()) {
                DataEntity entity = DataEntity.parseEntity(scan.nextLine());

                if (entity != null) {
                    dataSet.add(entity);
                }
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<DataEntity> dataSet = new ArrayList<>();
        Algorithm algorithm = new Algorithm();
        List<List<DataEntity>> folds = new ArrayList<>();

        readFromDataFile(dataSet);

        for (int i = 0; i < FOLDS; i++) {
            folds.add(new ArrayList<>());
        }

        for (DataEntity entity : dataSet) {
            Random rand = new Random();
            int rIndex = rand.nextInt() % 10;

            if (rIndex < 0) {
                rIndex *= -1;
            }

            folds.get(rIndex).add(entity);
        }

        algorithm.crossValidation(folds);
    }
}
