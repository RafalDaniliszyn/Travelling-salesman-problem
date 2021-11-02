import Algorithm.GeneticAlgorithm;
import Interface.Drawable;
import Model.GraphModel;
import Singleton.AlgorithmSettings;
import Singleton.Information;
import View.Panel;
import Enum.*;

import java.awt.*;

public class Action implements Drawable {

    Panel panel;
    GeneticAlgorithm geneticAlgorithm;
    GraphMaker graphMaker;
    ManualSolving manualSolving;
    GraphModel graphModel;
    Mode mode;

    public Action(Panel panel) {
        this.panel = panel;
        selectGraphMode();
        panel.generateButtons();



        waitTostart();
    }

    private void selectGraphMode(){
        boolean selected = false;

        while (!selected){
            if (panel.randomGraphMode.getModel().isPressed()){
                this.mode = Mode.RANDOMGRAPH;
                this.graphModel = new Generator.RandomGraphGenerator().getRandomGraph();
                this.geneticAlgorithm = new GeneticAlgorithm(graphModel, this.mode);
                panel.removeModeButtons();

                panel.addDrawable(geneticAlgorithm);
                panel.addDrawable(this);

                this.manualSolving = new ManualSolving(graphModel);
                panel.addDrawable(manualSolving.graphService);
                manualSolving.manualSolve();

                selected = true;
            }

            if (panel.userGraphMode.getModel().isPressed()){
                this.graphMaker = new GraphMaker();
                Thread graphMakerThread = new Thread(graphMaker);
                graphMakerThread.start();
                panel.addDrawable(graphMaker);
                panel.removeModeButtons();
                this.mode = Mode.USERGRAPH;
                selected = true;
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void waitTostart(){
        new Thread(()->{
            while (!panel.isStart){

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.start();
        }).start();

    }

    private void start(){
        if (panel.isStart){
            if (this.mode == Mode.USERGRAPH){
                this.geneticAlgorithm = new GeneticAlgorithm(graphMaker.generator.getUserGeneratedGraph(), this.mode);
                panel.addDrawable(geneticAlgorithm.graphService);
                panel.addDrawable(geneticAlgorithm);
                panel.addDrawable(this);
            }

            if (this.mode == Mode.RANDOMGRAPH){
                panel.addDrawable(geneticAlgorithm.graphService);
                panel.deleteDrawable(manualSolving.graphService);
            }

            new Thread(()->{
                while (!panel.stop.getModel().isPressed()){

                    geneticAlgorithm.work();

                    geneticAlgorithm.increaseMutation(panel.increaseMutation.getModel().isPressed());
                    geneticAlgorithm.decreaseMutation(panel.decreaseMutation.getModel().isPressed());
                    geneticAlgorithm.increaseSurvivalRate(panel.increaseSurvivalRate.getModel().isPressed());
                    geneticAlgorithm.decreaseSurvivalRate(panel.decreaseSurvivalRate.getModel().isPressed());

                    try {
                        Thread.sleep(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.blue);
        g.setFont(g.getFont().deriveFont(20.0f));
        g.drawString("GENERATION: " + geneticAlgorithm.generation, 10,200);
        g.drawString("MUTATION: " + AlgorithmSettings.settings.mutation, 10,250);
        g.drawString("SURVIVAL RATE: " + AlgorithmSettings.settings.survivalRate, 10, 300);
        g.drawString("YOUR RATE: " + Information.information.rate, 10, 350);
        g.setColor(Color.black);
    }
}













