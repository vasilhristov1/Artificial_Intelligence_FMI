package hw7;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class kMeans {
    private String filename;
    private int clustersNum;
    private List<Point> points;
    private List<Point> centroids;
    private Map<Point, List<Point>> clusters;
    private final boolean isNormal;
    private final boolean isUnbalance;

    public String getFilename() {
        return filename;
    }

    public int getClustersNum() {
        return clustersNum;
    }

    public boolean isNormal() {
        return isNormal;
    }

    public boolean isUnbalance() {
        return isUnbalance;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setClustersNum(int clustersNum) {
        this.clustersNum = clustersNum;
    }

    kMeans(String filename, int clustersNum) {
        this.points = new ArrayList<>();
        this.centroids = new ArrayList<>();
        this.clusters = new HashMap<>();
        this.setFilename(filename);
        this.setClustersNum(clustersNum);
        this.isNormal = filename.contains("normal");
        this.isUnbalance = filename.contains("unbalance");

        readFromFile(filename);
    }

    private void readFromFile(String filename) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] coordinates = line.split("\\s");
                double x = Double.parseDouble(coordinates[0]);
                double y = Double.parseDouble(coordinates[1]);
                this.points.add(new Point(x,y));
            }

            this.initCentroids();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void clustering() throws IOException {
        int counter = 0;
        boolean restartClustering = false;

        do {
            this.points = new ArrayList<>();
            this.centroids = new ArrayList<>();
            this.clusters = new HashMap<>();
            readFromFile(filename);
            restartClustering = this.formClusters(counter);
        } while (!restartClustering);
    }

    public boolean formClusters(int counter) throws IOException {
        boolean isChanged;
        double cost = Double.MAX_VALUE;
        double centroidsDist = Double.MIN_VALUE;

        do {
            if (counter > 50) {
                // System.out.println(counter);
                return false;
            } else {
                isChanged = false;
                counter++;


                for (Point centroid : this.getClusters().keySet()) {
                    this.getClusters().get(centroid).clear();
                }

                for (Point point : this.getPoints()) {
                    Point centroid = this.nearestCentroid(point);
                    this.getClusters().get(centroid).add(point);
                }

                for (Point centroid : this.getClusters().keySet()) {
                    Point newCoordinate = this.centroidUpdate(this.getClusters().get(centroid));

                    if (newCoordinate.getX() != centroid.getX() || newCoordinate.getY() != centroid.getY()) {
                        centroid.setX(newCoordinate.getX());
                        centroid.setY(newCoordinate.getY());
                        isChanged = true;
                    }
                }

                String filename;
                if (this.isNormal()) {
                    filename = "./result/result_normal/image" + counter + ".png";
                } else if (this.isUnbalance()) {
                    filename = "./result/result_unbalance/image" + counter + ".png";
                } else {
                    filename = "./result/image" + counter + ".png";
                }

                ImageCreator imageCreator = new ImageCreator(this, filename);
                imageCreator.createImg();

                double distance = this.calcCentroidDist();
                double currentCost = this.calcWithinPointScatter(this.getClusters());

                if (distance < centroidsDist || cost < currentCost || Double.isNaN(distance)) {
                    centroidsDist = Double.MIN_VALUE;
                    cost = Double.MAX_VALUE;
                    this.initCentroids();
                } else {
                    centroidsDist = distance;
                    cost = currentCost;
                }
            }

        } while (isChanged);

        System.out.println("Iterations: " + counter);
        return true;
    }

    public Map<Point, List<Point>> getClusters() {
        return clusters;
    }

    public List<Point> getPoints() {
        return points;
    }

    public List<Point> getCentroids() {
        return centroids;
    }

    private Point nearestCentroid(Point point) {
        Point nearestCentroid = null;
        double nearestCentroidDist = Double.MAX_VALUE;

        for (Point centroid : this.getClusters().keySet()) {
            double distance = point.calcDistance(centroid);

            if (distance < nearestCentroidDist) {
                nearestCentroid = centroid;
                nearestCentroidDist = distance;
            }
        }

        return nearestCentroid;
    }

    private Point centroidUpdate(List<Point> points) {
        int pointsNum = points.size();
        double x = 0.0;
        double y = 0.0;

        for (Point point : points) {
            x += point.getX();
            y += point.getY();
        }

        return new Point(x / pointsNum, y / pointsNum);
    }

    private void initCentroids() {
        Random rand = new Random();
        List<Integer> indexes = new ArrayList<>();
        this.getCentroids().clear();
        this.getClusters().clear();

        while (indexes.size() < this.getClustersNum()) {
            int pointIndex = rand.nextInt(this.getPoints().size());
            if (indexes.indexOf(pointIndex) == -1) {
                Point centroid = new Point(this.getPoints().get(pointIndex).getX(), this.getPoints().get(pointIndex).getY());
                this.getCentroids().add(centroid);
                this.getClusters().put(centroid, new ArrayList<>());
                indexes.add(pointIndex);
            }
        }
    }

    private double calcWithinPointScatter(Map<Point, List<Point>> clusters) {
        double cost = 0.0;

        for (Point centroid : clusters.keySet()) {
            double clusterCost = 0.0;

            for (Point point : clusters.get(centroid)) {
                clusterCost += Math.pow(point.calcDistance(centroid), 2);
            }
            cost += clusterCost * clusters.get(centroid).size();
        }

        return cost;
    }

    private double calcCentroidDist() {
        double centroidsDist = 0.0;

        for (int i = 0; i < centroids.size(); i++) {
            for (int j = i + 1; j < centroids.size(); j++) {
                double dist = centroids.get(i).calcDistance(centroids.get(j));
                centroidsDist += dist;
            }
        }

        return centroidsDist;
    }
}

