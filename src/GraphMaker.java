import Generator.UserGraphGenerator;
import Interface.Drawable;

import java.awt.*;

public class GraphMaker implements Drawable, Runnable {

    UserGraphGenerator generator;

    public GraphMaker() {
        generator = new UserGraphGenerator();
    }

    @Override
    public void draw(Graphics g) {

        for (int i = 0; i < generator.getGraphModel().points.size(); i++) {
            g.fillOval(generator.getGraphModel().points.get(i).x, generator.getGraphModel().points.get(i).y, 5, 5);
        }

    }

    @Override
    public void run() {
        while (!generator.isPrepared()){
            if (Mouse.mouse.isPressed()){
                generator.addPoint(Mouse.mouse.p);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
