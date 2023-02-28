package hw3.tsp;

import java.util.*;

public class TSPSolver {
    private final int MAX_INDIVIDUALS;
    private final int INDIVIDUALS;

    private final ArrayList<City> cities;
    private final int N;
    private ArrayList<City[]> population;
    private final ArrayList<City[]> generatedIndividuals;
    private final Random random;


    // constructor
    TSPSolver(int N) {
        this.cities = new ArrayList<>();
        this.population = new ArrayList<>();
        this.N = N;
        this.random = new Random();
        this.MAX_INDIVIDUALS = this.N / 3; // the max number of individuals for population
        this.INDIVIDUALS = MAX_INDIVIDUALS / 3; // the number of individuals for population
        this.generatedIndividuals = new ArrayList<>();

        // generate random coordinates for each of the N cities
        for (int i = 0; i < N; i++){
            int X = this.random.nextInt(1000);
            int Y = this.random.nextInt(1000);
            this.cities.add(new City(X,Y));
        }
    }

    // method to check if a certain route is already in the generatedIndividuals
    private boolean isCheckedPath(City[] path){
        for (City[] checkedRoute: this.generatedIndividuals) {
            if(Arrays.equals(checkedRoute, path)){
                return true;
            }
        }
        return false;
    }

    // method to check if a certain city is already in a certain route
    private boolean isVisitedCity(City[] path, City city){
        for(City visitedCity: path){
            if (city.equals(visitedCity)){
                return true;
            }
        }
        return false;
    }

    // method to make population with given number of individuals in it
    private void makePopulation(int numberOfIndividuals){
        // adds each individual in the population and in the generated individuals arrays
        for (int i = 0; i < numberOfIndividuals; i++){
            ArrayList<City> path = new ArrayList<>(this.cities); // contains all cities of the path
            City[] routeToArray; // the path represented as an array

            // make different routes while the certain route is unique
            do {
                Collections.shuffle(path);
                routeToArray = path.toArray(new City[this.N]);
            } while(isCheckedPath(routeToArray));

            this.population.add(routeToArray); // adds the route to the population
            this.generatedIndividuals.add(routeToArray.clone()); // adds clone of the root to the array with all generated individuals
        }
    }

    // method to sort the population
    private void sortPopulation(ArrayList<City[]> population){
        population.sort(new Comparator<City[]>() {
            @Override
            public int compare(City[] t1, City[] t2) {
                double routeCostT1 = routeDistance(t1);
                double routeCostT2 = routeDistance(t2);
                return Double.compare(routeCostT1, routeCostT2);
            }
        });
    }

    // method to fill the child route of a parent individual route
    private void fillChild(City[] parent, City[] child, int rightIndex){
        int indexInChild = rightIndex + 1;
        int indexInParent = rightIndex + 1;

        do {
            if (indexInParent == this.N){
                indexInParent = 0;
            }
            if (!isVisitedCity(child, parent[indexInParent])){
                if (indexInChild == this.N){
                    indexInChild = 0;
                }

                child[indexInChild] = parent[indexInParent];
                indexInChild++;

            }

            indexInParent++;
        } while (indexInParent != rightIndex + 1);

    }

    // method to do two points crossover and get the best children individuals
    private ArrayList<City[]> crossover(City[] parent1, City[] parent2){
        int index1 = this.random.nextInt(this.N);
        int index2 = this.random.nextInt(this.N);

        int min = Math.min(index1, index2);
        int max = Math.max(index1, index2);

        City[] child1 = new City[this.N];
        City[] child2 = new City[this.N];

        for (int i = min; i <= max; i++){
            child1[i] = parent2[i];
            child2[i] = parent1[i];
        }

        this.fillChild(parent1, child1, max);
        this.fillChild(parent2, child2, max);

        ArrayList<City[]> children = new ArrayList<>();
        children.add(child1);
        children.add(child2);
        return children;
    }

    // method to calculate the total cost of a route
    private double routeDistance(City[] individual){
        double cost = 0.0;

        for (int i = 1; i < individual.length; i++){
            cost += individual[i].calculateDistance(individual[i-1]);
        }

        return cost;
    }


    public void solve(){
        this.population.add(this.cities.toArray(new City[this.N]));
        this.makePopulation(this.MAX_INDIVIDUALS - 1);

        int generation = 0; // number of generations

        while (generation < 500){
            generation++; // increasing the number of generations

            // check on which generation is the program now
            if (generation == 1 || generation == 500 || generation == 400 || generation == 250 || generation == 100){
                System.out.printf("Generation: %d%n", generation);
                System.out.printf("Distance: %.2f%n%n", this.routeDistance(this.population.get(0)));
            }

            this.sortPopulation(this.population); // sorting the population

            ArrayList<City[]> nextPopulation = new ArrayList<>(); // the array that stores the next population

            // add 1/3 of the population to the next population
            for (int i = 0; i < this.INDIVIDUALS; i++){
                nextPopulation.add(this.population.get(i));
            }

            // we add the cities after the crossover function
            for (int i = this.INDIVIDUALS; i <= this.population.size() / 2; i++){
                nextPopulation.addAll(this.crossover(this.population.get(i-1), this.population.get(i)));
            }

            // here is the mutation part of the algorithm
            for (int i = 1; i < nextPopulation.size(); i++){
                do {
                    int gene1 = random.nextInt(this.N); // get the index of the first random city
                    int gene2 = random.nextInt(this.N); // get the index of the second random city

                    // swap these cities
                    City temp = nextPopulation.get(i)[gene1];
                    nextPopulation.get(i)[gene1] = nextPopulation.get(i)[gene2];
                    nextPopulation.get(i)[gene2] = temp;
                } while (isCheckedPath(nextPopulation.get(i)));

                this.generatedIndividuals.add(nextPopulation.get(i).clone()); // add each individual of the next population to the generated individuals
            }

            this.population = nextPopulation; // set the new population
        }
    }
}
