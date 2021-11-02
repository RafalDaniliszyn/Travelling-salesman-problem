package View;

import Interface.Drawable;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Panel extends JPanel implements ActionListener {
    public List<Drawable>drawables;

    public JButton randomGraphMode;
    public JButton userGraphMode;

    public JButton start;
    public JButton increaseMutation;
    public JButton decreaseMutation;
    public JButton stop;
    public JButton increaseSurvivalRate;
    public JButton decreaseSurvivalRate;
    public boolean isStart = false;

    private BufferedImage map;
    private BufferedImage background;
    private BufferedImage axis;

    public Panel() {
        loadBackground();
        loadAxis();

        setPreferredSize(new Dimension(1000,700));
        drawables = new ArrayList<>();
        generateModeButtons();
    }

    private void loadAxis(){
        try {
            this.axis = ImageIO.read(new File("images\\axis.png"));
        } catch (IOException e) {
            System.out.println("can not load background");
            e.printStackTrace();
        }
    }

    private void loadBackground(){
        try {
            this.background = ImageIO.read(new File("images\\background.png"));
        } catch (IOException e) {
            System.out.println("can not load axis");
            e.printStackTrace();
        }
    }

    private void generateModeButtons(){
        randomGraphMode = new JButton("GET RANDOM GRAPH");
        this.add(randomGraphMode);
        randomGraphMode.addActionListener(this);

        randomGraphMode.setForeground(Color.black);
        randomGraphMode.setBackground(new Color(100,90,90,150));

        userGraphMode = new JButton("PREPARE YOUR GRAPH");
        this.add(userGraphMode);
        userGraphMode.addActionListener(this);
        userGraphMode.setForeground(Color.black);
        userGraphMode.setBackground(new Color(100,90,90,200));
    }

    public void removeModeButtons(){
        this.remove(randomGraphMode);
        this.remove(userGraphMode);
    }

    public void generateButtons(){

        start = new JButton("START");
        start.setBounds(20,20,100,30);
        this.add(start);
        start.addActionListener(this);
        start.setForeground(Color.black);
        start.setBackground(new Color(100,90,90,150));

        stop = new JButton("STOP");
        stop.setBounds(20, 70, 100, 30);
        this.add(stop);
        stop.addActionListener(this);
        stop.setForeground(Color.black);
        stop.setBackground(new Color(100,90,90,150));

        increaseMutation = new JButton("INCREASE MUTATION");
        increaseMutation.setBounds(200, 20, 200, 30);
        this.add(increaseMutation);
        increaseMutation.addActionListener(this);
        increaseMutation.setForeground(Color.black);
        increaseMutation.setBackground(new Color(100,90,90,150));

        decreaseMutation = new JButton("DECREASE MUTATION");
        decreaseMutation.setBounds(200, 70, 200, 30);
        this.add(decreaseMutation);
        decreaseMutation.addActionListener(this);
        decreaseMutation.setForeground(Color.black);
        decreaseMutation.setBackground(new Color(100,90,90,150));

        increaseSurvivalRate = new JButton("INCREASE SURVIVAL RATE");
        increaseSurvivalRate.setBounds(420,20, 200,30);
        this.add(increaseSurvivalRate);
        increaseSurvivalRate.addActionListener(this);
        increaseSurvivalRate.setForeground(Color.black);
        increaseSurvivalRate.setBackground(new Color(100,90,90,150));

        decreaseSurvivalRate = new JButton("DECREASE SURVIVAL RATE");
        decreaseSurvivalRate.setBounds(420, 70, 200, 30);
        this.add(decreaseSurvivalRate);
        decreaseSurvivalRate.addActionListener(this);
        decreaseSurvivalRate.setForeground(Color.black);
        decreaseSurvivalRate.setBackground(new Color(100,90,90,150));
    }

    public void addDrawable(Drawable drawable){
        drawables.add(drawable);
    }

    public void deleteDrawable(Drawable drawable){
        drawables.remove(drawable);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(background,0,0,null);
        g.drawImage(axis, 250,150,null);
        drawables.forEach(drawable1 -> drawable1.draw(g));

        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == start){
            isStart = true;
        }
    }
}
