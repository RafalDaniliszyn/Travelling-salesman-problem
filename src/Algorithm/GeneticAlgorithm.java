package Algorithm;

import Enum.*;
import Interface.Drawable;
import Model.GraphModel;
import Service.GraphService;
import Singleton.AlgorithmSettings;
import Model.Creature;
import Singleton.Information;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class GeneticAlgorithm implements Drawable {
    public GraphService graphService;
    public List<Creature> population;
    public int survivalRate = AlgorithmSettings.settings.survivalRate;
    public int generation = 0;


    public GeneticAlgorithm(GraphModel graphModel, Mode mode) {
        if (mode == Mode.RANDOMGRAPH){
            this.graphService = new GraphService(graphModel);
            population = getRandomPopulation(AlgorithmSettings.settings.populationLength, graphModel.matrix.length);
            graphService.setWay(population.get(0).genome);
            setNearestPointFirst();
        }

        if (mode == Mode.USERGRAPH){
            this.graphService = new GraphService(graphModel);
            population = getRandomPopulation(AlgorithmSettings.settings.populationLength, graphModel.matrix.length);
            graphService.setWay(population.get(0).genome);
        }
    }

    public void work(){
        evaluatePopulation();
        sortPopulation();
        Information.information.setBestDistance(population.get(0).rate);
        for (int i = 0; i < population.size(); i++) {
            repairCreature(population.get(i));
        }

        graphService.setWay(population.get(0).getGenomeAsList());
        graphService.setWay(population.get(0).genome);
        generateNewPopulation();
        mutation(AlgorithmSettings.settings.mutation, population);
        generation +=1;
    }

    private List getRandomPopulation(int populationLength, int genomeLength) {
        List<Creature>creatures = new ArrayList<>();
        for (int i = 0; i < populationLength; i++) {
            creatures.add(new Creature(genomeLength));
        }
        return  creatures;
    }

    //this method check if two lines are crossed and swap it vertices only if it cause better rate
    private void repairCreature(Creature creature){
        for (int i = 0; i < creature.genome.length-1; i++) {
            Point p1 = graphService.graphModel.points.get(creature.genome[i]);
            Point p2 = graphService.graphModel.points.get(creature.genome[i+1]);
            Line2D line = new Line2D.Double(p1, p2);

            for (int j = i; j < creature.genome.length-2; j++) {
                Point pNext1 = graphService.graphModel.points.get(creature.genome[j]);
                Point pNext2 = graphService.graphModel.points.get(creature.genome[j+1]);
                Line2D lineNext = new Line2D.Double(pNext1, pNext2);

                if (lineNext.intersectsLine(line)
                        && line.getX1() != lineNext.getX1()
                        && line.getX2() != lineNext.getX2()
                        && line.getY1() != lineNext.getY1()

                        && line.getY2() != lineNext.getY2()
                        && line.getX1() != lineNext.getX2()

                        && line.getY1() != lineNext.getY2()
                        && line.getX2() != lineNext.getX1()
                        && line.getY2() != lineNext.getY1()
                        && i > 0){
                    long oldRate = evaluateWay(creature.genome);
                    int[] way = creature.genome;
                    swap(creature, i+1, j);
                    long newRate = evaluateWay(creature.genome);
                    if (oldRate > newRate){
                        creature.genome = way;
                    }
                }
            }
        }
    }

    private void swap(Creature creature, int i, int j){
        int k = creature.genome[i];
        creature.genome[i] = creature.genome[j];
        creature.genome[j] = k;
    }

    //this method set first and last step of genome to nearest from 0 point
    private void setNearestPointFirst(){
        int[] points = findTwoNearestPoint();
        for (int i = 0; i < population.size(); i++) {

            int index = indexOf(points[0], population.get(i).genome);
            int k = 0;
            if (index != 0){
                k = population.get(i).genome[1];
                population.get(i).genome[1] = points[0];
                population.get(i).genome[index] = k;
            }

            index = indexOf(points[1], population.get(i).genome);
            if (index != 0){
                k = population.get(i).genome[AlgorithmSettings.settings.genomeLength-1];
                population.get(i).genome[AlgorithmSettings.settings.genomeLength-1] = points[1];
                population.get(i).genome[index] = k;
            }

        }
    }

    //this method find two nearest point from 0
    private int[] findTwoNearestPoint(){
        int[] points = new int[2];

        long nearest = graphService.graphModel.matrix[0][1];
        for (int i = 1; i < graphService.graphModel.matrix.length-1; i++) {
            if (graphService.graphModel.matrix[0][i] <= nearest){
                nearest = graphService.graphModel.matrix[0][i];
                points[0] = i;
            }
        }
        nearest = graphService.graphModel.matrix[0][1];
        for (int i = 1; i < graphService.graphModel.matrix.length-1; i++) {
            if (graphService.graphModel.matrix[0][i] <= nearest && i != points[0]){
                nearest = graphService.graphModel.matrix[0][i];
                points[1] = i;
            }
        }

        return points;
    }

    private long evaluateWay(int[] way){
        long rate = 0;

        for (int i = 0; i < way.length-1; i++) {
            rate = rate + graphService.graphModel.matrix[way[i]][way[i+1]];
        }

        double reverseRate = rate;
        reverseRate = 1/reverseRate;
        reverseRate = reverseRate * 1000000000;
        rate = (long)reverseRate;

        return rate ;
    }

    public void evaluatePopulation(){
        population.forEach(creature -> creature.rate = evaluateWay(creature.genome));
    }

    public void sortPopulation(){
        population = population.stream().sorted(Comparator.comparing(creature -> creature.rate)).collect(Collectors.toList());
        Collections.reverse(population);
    }

    public void generateNewPopulation() {
        List<Creature> restChildren = crossover();
        for (int i = 0; i < restChildren.size(); i++) {
            population.get(i + survivalRate).genome = restChildren.get(i).genome;
        }
    }

    public int indexOf(int value, int[] tab){
        for (int i = 0; i < tab.length; i++) {
            if (tab[i] == value){
                return i;
            }
        }
        return 0;
    }

    //return unique value from - to range in parent2
    //return 0 if is no unique value
    public int findNextUnique(int from, int to, int originalFrom, Creature parent1, Creature parent2){
        for (int j = from; j < to; j++) {
            for (int k = originalFrom; k < to; k++) {
                if (parent2.genome[j] == parent1.genome[k]){
                    break;
                }
                if (k == to - 1){
                    return parent2.genome[j];
                }
            }
        }
        return 0;
    }

    private int map(int uniqueValue, Creature parent1, Creature parent2, int crossPointFrom, int crossPointTo){
        int indexOfUniqueValue = indexOf(uniqueValue, parent2.genome);   //Note the index of this value in Parent 2.
        int valueParent1 = parent1.genome[indexOfUniqueValue];           //Locate the value from parent 1 in this same position.
        int IndexValueParent2 = indexOf(valueParent1, parent2.genome);  //Locate this same value in parent 2.

        if (IndexValueParent2 < crossPointFrom || IndexValueParent2 >= crossPointTo){
            return IndexValueParent2;
        }
        else {
            return map(parent2.genome[IndexValueParent2], parent1, parent2, crossPointFrom, crossPointTo);
        }

    }

    public void increaseMutation(boolean increase){
        if (increase){
            AlgorithmSettings.settings.mutation += 1;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void decreaseMutation(boolean decrease){
        if (decrease){
            AlgorithmSettings.settings.mutation -=1;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void increaseSurvivalRate(boolean increase){
        if (increase){
            AlgorithmSettings.settings.survivalRate += 1;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void decreaseSurvivalRate(boolean decrease){
        if (decrease){
            AlgorithmSettings.settings.survivalRate -= 1;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void mutation(int rate, List<Creature>creatures){
        Random random = new Random();

        for (int i = 0; i < creatures.size(); i++) {
            int number = random.nextInt(100);
            if (number <= rate){
                int randomI = random.nextInt(AlgorithmSettings.settings.genomeLength-1)+1;
                int randomJ = random.nextInt(AlgorithmSettings.settings.genomeLength-1)+1;
                swap(creatures.get(i), randomI, randomJ);
            }
        }
    }

    private List crossover(){
        Random random = new Random();
        List<Creature>childrens = new ArrayList<>();

        for (int i = survivalRate; i < population.size(); i++) {

            int crossPointFrom = 0;
            int crossPointTo = 0;

            while (crossPointFrom == crossPointTo ){
                crossPointFrom = random.nextInt(population.get(0).genome.length-1);
                crossPointTo = random.nextInt(population.get(0).genome.length - crossPointFrom) + crossPointFrom+1;
            }

            int[] childGenome = new int[population.get(0).genome.length];

            Creature parent1 = population.get(random.nextInt(population.size()));;
            Creature parent2 = population.get(random.nextInt(population.size()));

            while (parent1.equals(parent2)){
                parent1 = population.get(random.nextInt(population.size()));
                parent2 = population.get(random.nextInt(population.size()));
            }

            for (int j = crossPointFrom; j < crossPointTo; j++) {
                childGenome[j] = parent1.genome[j];
            }

            int previousUniqueIndex = crossPointFrom;

            for (int j = crossPointFrom; j < crossPointTo; j++) {
                int uniqueValue = findNextUnique(previousUniqueIndex ,crossPointTo, crossPointFrom, parent1, parent2); //value that hasn't already been copied to the child.

                if(uniqueValue != 0){
                    previousUniqueIndex = indexOf(uniqueValue, parent2.genome)+1;
                    childGenome[map(uniqueValue, parent1, parent2, crossPointFrom, crossPointTo)] = uniqueValue;
                }
            }
            for (int j = 0; j < childGenome.length; j++) {
                if (childGenome[j] == 0){
                    childGenome[j] = parent2.genome[j];
                }
            }

            childrens.add(new Creature(childGenome));
        }
        return childrens;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.blue);
        g.setFont(g.getFont().deriveFont(20.0f));
        g.drawString("ALGORITHM RATE: " + String.valueOf(evaluateWay(graphService.way)), 10,150);
        g.setColor(Color.black);
    }
}
