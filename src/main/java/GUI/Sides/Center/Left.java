package GUI.Sides.Center;

import GUI.Sides.Center.LeftParts.LeftBottom;
import GUI.Sides.Center.LeftParts.LeftTop;

import javax.swing.*;
import java.awt.*;

public class Left extends JPanel{

    private static final int WIDTH  = (int) (41 * 9.5); // 390
    private static final int HEIGHT = (int) (40 * 7.0); // 280

    private JFrame mainFrame;
    protected LeftTop leftTop;
    private LeftBottom leftBottom;

    public Left(JFrame mainFrame) {
        this.mainFrame = mainFrame;
        setPanel();
    }

    public void setPanel(){
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        leftTop = new LeftTop();
        leftBottom = new LeftBottom();

        this.add(leftTop);
        this.add(leftBottom);
        mainFrame.getContentPane().add(BorderLayout.WEST, this);
    }

    public LeftTop getLeftTop() {
        return leftTop;
    }
    public LeftBottom getLeftBottom() {
        return leftBottom;
    }
}
