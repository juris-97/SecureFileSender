package GUI;

import Connection.Receiver;
import Connection.Sender;
import Events.BottomListeners;
import Events.LeftSideListeners;
import Events.RightSideListeners;
import Events.TopListeners;
import GUI.Center.Left;
import GUI.Center.Right;
import Keys.KeyManager;

import javax.swing.*;
import java.awt.*;

public class MainFrame {

    private static final int WIDTH  = 600;
    private static final int HEIGHT = 450;

    JFrame frame;
    JPanel background;
    JPanel top, right, left, bottom;

    BottomListeners   bottomListeners;
    TopListeners      topListeners;
    LeftSideListeners leftSideListener;
    RightSideListeners rightSideListeners;

    Receiver receiver;
    Sender sender;

    KeyManager keyManager;

    public MainFrame(){
        initFrame();
    }

    public void initSender(){
        sender = new Sender((Top) top, (Bottom) bottom, keyManager);
        sender.establishConnection();
    }

    public void initReceiving(){
        receiver = new Receiver((Right) right,  keyManager);
        Thread recThread = new Thread(receiver);
        recThread.start();
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

        keyManager = new KeyManager();

        initSender();
        initReceiving();
        initListeners();

        frame.getContentPane().add(background);
        frame.setVisible(true);
    }

    public void initListeners(){

        bottomListeners = new BottomListeners((Bottom) bottom, (Top) top, (Left) left, (Right) right, sender, receiver);
        topListeners = new TopListeners((Top) top, sender);
        leftSideListener = new LeftSideListeners((Left) left);
        rightSideListeners = new RightSideListeners((Right) right);
    }
}
