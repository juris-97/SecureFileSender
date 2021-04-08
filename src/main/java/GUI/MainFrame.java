package GUI;

import Connection.Receiver;
import Connection.Sender;
import Events.BottomListeners;
import Events.LeftSideListeners;
import Events.RightSideListeners;
import Events.TopListeners;
import GUI.Sides.Bottom;
import GUI.Sides.Center.Left;
import GUI.Sides.Center.Right;
import GUI.Sides.Top;
import Security.KeyHandler;

import javax.swing.*;
import java.awt.*;

public class MainFrame {
    private static final int WIDTH  = 600;
    private static final int HEIGHT = 450;

    private Top    top;
    private Bottom bottom;
    private Left   left;
    private Right  right;

    private Sender sender;
    private Receiver receiver;

    private KeyHandler keyHandler;

    public MainFrame(){
        initFrame();
        initKeyHandler();
        initConnection();
        initListeners();
    }

    public void initFrame(){
        JFrame frame = new JFrame("Secure File Sender");
        frame.setResizable(false);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel background = new JPanel();
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

    public void initConnection(){
        sender = new Sender();
        receiver = new Receiver(right, keyHandler);
    }

    public void initKeyHandler(){
        // create keyHandler and generates
        // pair of key (public, private)
        keyHandler = new KeyHandler();
    }

    public void initListeners(){

        new BottomListeners(bottom, top,  left.getLeftBottom(), keyHandler, sender);
        new TopListeners(top, sender, keyHandler);
        new LeftSideListeners(left,  keyHandler, sender);
        new RightSideListeners(right);
    }
}
