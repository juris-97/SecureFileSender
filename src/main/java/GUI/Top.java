package GUI;

import javax.swing.*;
import java.awt.*;

public class Top extends JPanel {

    private static final int HEIGHT = (int) 2.5 * 40; // 100px
    String path = "C:\\Program Files\\Java\\jdk1.8.0_212\\bin\\java.exe";

    JFrame mainFrame;
    JLabel toLabel;
    JLabel pathLabel;

    JTextField ipField;
    JButton chooseFileButton;

    public Top(JFrame frame){
        this.mainFrame = frame;
        toLabel = new JLabel("To: ");
        ipField = new JTextField();
        chooseFileButton = new JButton("Choose File");

        // [TODO] path should be changed depending on chosen file;
        pathLabel = new JLabel(path);
        pathLabel.setBorder(BorderFactory.createEmptyBorder(0,10,0,0));

        initPanel();
    }

    // creates new panel with margins
    public JPanel createSide(int top, int left, int bottom, int right){
        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setPreferredSize(new Dimension(this.getWidth(), HEIGHT / 2));
        panel.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));

        return panel;
    }

    public void initPanel(){
        this.setPreferredSize(new Dimension(mainFrame.getWidth(), HEIGHT));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel upper = createSide(10,5,10, (int)(mainFrame.getWidth() * 0.7));
        upper.add(toLabel);
        upper.add(ipField);

        JPanel lower = createSide(5,5,10, (int)(mainFrame.getWidth() * 0.6));
        lower.add(chooseFileButton);
        lower.add(pathLabel);

        this.add(upper);
        this.add(lower);
        mainFrame.getContentPane().add(BorderLayout.NORTH, this);
    }

    public JButton getChooseFileButton() {
        return chooseFileButton;
    }

    public JLabel getPathLabel() {
        return pathLabel;
    }

    public JLabel getToLabel() {
        return toLabel;
    }

    public JTextField getIpField() {
        return ipField;
    }
}