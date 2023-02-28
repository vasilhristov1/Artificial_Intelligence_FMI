package hw7;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);

        System.out.println("Enter the path of a file: ");
        String filename = scan.nextLine();

        System.out.println("Enter K(the number of clusters): ");
        int clustersNum = scan.nextInt();

        kMeans kmeans = new kMeans(filename, clustersNum);
        kmeans.clustering();

        ImageCreator imageCreator;

        if (kmeans.isNormal()) {
            imageCreator = new ImageCreator(kmeans, "./imageNormal.png");
        } else if (kmeans.isUnbalance()) {
            imageCreator = new ImageCreator(kmeans, "./imageUnbalance.png");
        } else {
            imageCreator = new ImageCreator(kmeans, "./image.png");
        }

        imageCreator.createImg();
    }
}
