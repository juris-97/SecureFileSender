package GUI.Sides.Center;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Right extends JPanel {

    private static final int HEIGHT = 7 * 40; // 280
    private static final int WIDTH = 5 * 41; // 205

    private final DefaultListModel model;
    private final JScrollPane scrollPane;
    private final JLabel receivedLabel;
    private final JFrame mainFrame;
    private final JList list;

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
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        receivedLabel = new JLabel("Received files: ");
        receivedLabel.setBorder(BorderFactory.createEmptyBorder(0,40,5,0));

        this.add(receivedLabel);
        this.add(scrollPane);
        mainFrame.getContentPane().add(BorderLayout.EAST, this);
    }
    public JList<?> getList() {
        return list;
    }

    public void addFileToPanel(File file){
        this.model.addElement(file);
    }
}
