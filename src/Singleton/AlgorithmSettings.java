package Singleton;

public class AlgorithmSettings {

    public int survivalRate = 4;
    public int populationLength = 20;
    public int genomeLength = 100;
    public int mutation = 5;

    public static AlgorithmSettings settings = new AlgorithmSettings();

    private AlgorithmSettings() {
    }

}
