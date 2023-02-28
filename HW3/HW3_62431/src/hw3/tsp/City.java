package hw3.tsp;

import java.util.Objects;

public class City {
    private int x; // coordinate x of a city
    private int y; // coordinate y of a city

    // constructor for the City class
    public City(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // method to get the x coordinates
    public int getX() {
        return this.x;
    }

    // method to get the y coordinates
    public int getY() {
        return this.y;
    }

    // method to calculate the distance between two cities
    public double calculateDistance(City city) {
        int xDistance = Math.abs(this.getX() - city.getX());
        int yDistance = Math.abs(this.getY() - city.getY());

        return Math.sqrt((xDistance * xDistance) + (yDistance * yDistance));
    }

    // Override functions equals and hashCode
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final City city = (City) obj;
        return x == city.x && y == city.y;
    }

    public int hashCode() {
        return Objects.hash(x, y);
    }
}
