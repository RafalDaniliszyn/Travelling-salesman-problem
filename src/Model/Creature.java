package Model;

import java.util.*;
import java.util.stream.Collectors;

public class Creature {
    public int[] genome;
    public long rate;

    public Creature(int genomeLength) {
        generateGenome(genomeLength,0);
    }

    public Creature(int[] genome) {
        this.genome = genome;
    }

    public List<Integer> getGenomeAsList(){
        List<Integer>integerList = Arrays.stream(this.genome).boxed().collect(Collectors.toList());
        return integerList;
    }

    public void generateGenome(int genomeLength, int startIndex){
        genome = new int[genomeLength +1];
        Random random = new Random();
        int randomNumber = random.nextInt(genomeLength);
        Set<Integer> preparationSet = new LinkedHashSet<>();
        preparationSet.add(startIndex);
        while (preparationSet.size() != genomeLength){
            preparationSet.add(randomNumber);
            randomNumber = random.nextInt(genomeLength);
        }
        Integer[] integers = preparationSet.toArray(new Integer[0]);

        for (int i = 0; i < integers.length; i++) {
            genome[i] = integers[i];
        }

    }
}
