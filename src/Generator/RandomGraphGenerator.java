package Generator;

import Model.GraphModel;
import Singleton.AlgorithmSettings;

import java.awt.*;
import java.util.Random;

public class RandomGraphGenerator {

    private GraphModel graphModel;

    public RandomGraphGenerator() {
        graphModel = new GraphModel();
        graphModel.matrix = new long[AlgorithmSettings.settings.genomeLength][AlgorithmSettings.settings.genomeLength];
    }

    public GraphModel getRandomGraph(){
        Random random = new Random();

        for (int i = 0; i < graphModel.matrix.length; i++) {
            graphModel.points.add(new Point(random.nextInt(650)+270, random.nextInt(460)+170));
        }

        double weight;
        for (int i = 0; i < graphModel.matrix.length; i++) {
            for (int j = 0; j < graphModel.matrix[0].length; j++) {
                weight = Math.sqrt(Math.pow(graphModel.points.get(i).x - graphModel.points.get(j).x, 2)
                        + Math.pow(graphModel.points.get(i).y - graphModel.points.get(j).y, 2));
                graphModel.matrix[i][j] = (long)weight/10;
            }
        }

        return graphModel;
    }
}
