package Generator;

import Model.GraphModel;
import Singleton.AlgorithmSettings;

import java.awt.*;

public class UserGraphGenerator {

    private GraphModel graphModel;

    public UserGraphGenerator() {
        graphModel = new GraphModel();
        graphModel.matrix = new long[AlgorithmSettings.settings.genomeLength][AlgorithmSettings.settings.genomeLength];
    }

    public GraphModel getUserGeneratedGraph(){
        double weight;
        for (int i = 0; i < graphModel.matrix.length; i++) {
            for (int j = 0; j < graphModel.matrix[0].length; j++) {
                weight = Math.sqrt(Math.pow(graphModel.points.get(i).x - graphModel.points.get(j).x, 2)
                        + Math.pow(graphModel.points.get(i).y - graphModel.points.get(j).y, 2));
                graphModel.matrix[i][j] = (int)weight / 10;
            }
        }
        return graphModel;
    }

    public void addPoint(Point p){
        if (graphModel.points.size() != graphModel.matrix.length){
            graphModel.points.add(new Point(p.x, p.y));
        }
    }

    public boolean isPrepared(){
        if (graphModel.points.size() == graphModel.matrix.length){
            return true;
        }
        return false;
    }

    public GraphModel getGraphModel() {
        return graphModel;
    }
}
