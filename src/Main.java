import View.Frame;
import View.Panel;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        Frame frame = new Frame();
        Panel panel = new Panel();
        frame.add(panel);
        frame.pack();
        frame.addMouseListener(Mouse.mouse);
        Action action = new Action(panel);

    }
}
