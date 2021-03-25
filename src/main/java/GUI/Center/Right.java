package GUI.Center;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class Right extends JPanel {

    JFrame mainFrame;
    JList list;

    JScrollPane scrollPane;
    JLabel receivedLabel;
    DefaultListModel model;



    private static final int HEIGHT = 7 * 40; // 280
    private static final int WIDTH = 5 * 41; // 205

    public Right(JFrame mainFrame){
        this.mainFrame = mainFrame;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        model = new DefaultListModel();
        list = new JList(model);
        list.setVisibleRowCount(10);

        scrollPane = new JScrollPane(list);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        receivedLabel = new JLabel("Received files: ");
        receivedLabel.setBorder(BorderFactory.createEmptyBorder(0,40,5,0));

        this.add(receivedLabel);
        this.add(scrollPane);
        mainFrame.getContentPane().add(BorderLayout.EAST, this);
    }

    public JList getList() {
        return list;
    }

    public DefaultListModel getModel() {
        return model;
    }
}
