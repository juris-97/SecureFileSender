package GUI.Center;

import javax.swing.*;
import java.awt.*;

public class Right extends JPanel {

    JFrame mainFrame;
    JList list;

    JScrollPane scrollPane;
    JLabel receivedLabel;

    // [TODO] will stores not only strings but whole File
    String[] receivedFiles = {"file1", "file2", "file3", "file4"};

    private static final int HEIGHT = 7 * 40; // 280
    private static final int WIDTH = 5 * 41; // 205

    public Right(JFrame mainFrame){
        this.mainFrame = mainFrame;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        list = new JList(receivedFiles);
        list.setVisibleRowCount(10);

        scrollPane = new JScrollPane(list);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        receivedLabel = new JLabel("Received files: ");
        receivedLabel.setBorder(BorderFactory.createEmptyBorder(0,80,5,0));

        this.add(receivedLabel);
        this.add(scrollPane);
        mainFrame.getContentPane().add(BorderLayout.EAST, this);
    }

    public JList getList() {
        return list;
    }
}
