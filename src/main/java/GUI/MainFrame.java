package GUI;

import GUI.Center.Left;
import GUI.Center.Right;

import javax.swing.*;
import java.awt.*;

public class MainFrame {

    private static final int WIDTH  = 600;
    private static final int HEIGHT = 450;

    JFrame frame;
    JPanel background;
    JPanel top, right, left, bottom;

    public MainFrame(){
        initFrame();
    }

    public void initFrame(){
        frame = new JFrame("Secure File Sender");
        frame.setResizable(false);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        background = new JPanel();
        background.setBackground(Color.lightGray);
        background.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        // all sides added to frame inside classes
        top    = new Top(frame);
        left   = new Left(frame);
        right  = new Right(frame);
        bottom = new Bottom(frame);

        frame.getContentPane().add(background);
        frame.setVisible(true);
    }
}
