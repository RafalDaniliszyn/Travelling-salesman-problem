package Service;

import Interface.Drawable;
import Model.GraphModel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GraphService implements Drawable {

    public GraphModel graphModel;
    public int[] way;
    public List<Integer> wayList;

    public GraphService(GraphModel graphModel) {
        wayList = new ArrayList<>();

        if (graphModel.matrix.length != graphModel.matrix[0].length){
            System.out.println("the side of the matrix music be equal");
        }else {
            this.graphModel = graphModel;
            checkMatrix();
        }
    }

    private void checkMatrix(){
        for (int i = 0; i < graphModel.matrix.length; i++) {
            for (int j = 0; j < graphModel.matrix[0].length; j++) {
                if (graphModel.matrix[i][j] > 0 && graphModel.matrix[i][j] != graphModel.matrix[j][i]){
                    graphModel.matrix[j][i] = graphModel.matrix[i][j];
                }

                if (graphModel.matrix[i][i] != 0){
                    graphModel.matrix[i][i] = 0;
                }
            }
        }
    }

    public void setWay(int[] way){
        this.way = way;
    }

    public void setWay(List<Integer> integerList){
        this.wayList = integerList;

    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g1 = (Graphics2D) g;
        for (int i = 0; i < graphModel.points.size(); i++) {
            g.fillOval(graphModel.points.get(i).x, graphModel.points.get(i).y, 5,5);
            g.setColor(Color.red);
            if (i == 0){
                g.setColor(Color.green);
            }
            g.setFont(g.getFont().deriveFont(13.0f));
            g.drawString(String.valueOf(i), graphModel.points.get(i).x, graphModel.points.get(i).y);
            g.setColor(Color.black);
        }

        for (int i = 0; i < wayList.size()-1; i++) {
            g1.setStroke(new BasicStroke(1));
            g1.setColor(Color.red);
            g1.drawLine(graphModel.points.get(wayList.get(i)).x, graphModel.points.get(wayList.get(i)).y, graphModel.points.get(wayList.get(i+1)).x, graphModel.points.get(wayList.get(i+1)).y);
            g1.setColor(Color.black);
        }
    }

}
