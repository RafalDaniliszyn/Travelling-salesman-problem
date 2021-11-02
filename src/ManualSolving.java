import Model.GraphModel;
import Service.GraphService;
import Singleton.Information;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ManualSolving{

    private GraphModel graphModel;
    public GraphService graphService;
    private List<Integer> newWay;

    public ManualSolving(GraphModel graphModel) {
        this.graphModel = graphModel;
        graphService = new GraphService(graphModel);
        newWay = new ArrayList<>();
        newWay.add(0);
        graphService.setWay(newWay);
    }

    private void evaluateWay(List<Integer>way){
        long rate = 0;

        for (int i = 0; i < way.size()-1; i++) {
            rate = rate + graphService.graphModel.matrix[way.get(i)][way.get(i+1)];
        }

        double reverseRate = rate;
        reverseRate = 1/reverseRate;
        reverseRate = reverseRate * 1000000000;
        Information.information.rate = (long)reverseRate;
    }

    public void manualSolve(){
        new Thread(()->{

            while (newWay.size() != graphModel.points.size()){
                if (Mouse.mouse.isPressed()){

                    Point chosenPoint = new Point(Mouse.mouse.p);
                    chosenPoint.x -= 10;
                    chosenPoint.y -= 23;

                    for (int i = 0; i < graphModel.points.size(); i++) {
                        Point graphPoint = graphModel.points.get(i);
                        if (chosenPoint.x <= graphPoint.x && chosenPoint.x >= graphPoint.x - 10 || chosenPoint.x >= graphPoint.x && chosenPoint.x <= graphPoint.x + 10 ){
                            if (chosenPoint.y <= graphPoint.y && chosenPoint.y >= graphPoint.y - 10 || chosenPoint.y >= graphPoint.y && chosenPoint.y <= graphPoint.y + 10){
                                if (!newWay.contains(i)){

                                    try {
                                        newWay.add(i);
                                        Thread.sleep(300);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                    graphService.setWay(newWay);
                }

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            newWay.add(0);
            graphService.setWay(newWay);

            evaluateWay(newWay);
        }).start();
    }


}
