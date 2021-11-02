import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Mouse implements MouseListener {
    Point p = new Point(100,100);
    private boolean isPressed = false;

    public static Mouse mouse = new Mouse();

    private Mouse() {
    }

    public boolean isPressed() {
        return isPressed;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        isPressed = true;
        p = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isPressed = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
