package GUI.Sides;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Top extends JPanel {

    private static final int HEIGHT = (int) 2.5 * 40; // 100px

    private final JLabel toLabel;
    private final JLabel pathLabel;
    private final JFrame mainFrame;

    private final JButton chooseFileButton;
    private final JButton connectButton;

    File chosenFile;
    JTextField ipField;

    public Top(JFrame frame){
        this.mainFrame = frame;

        toLabel = new JLabel("To: ");
        ipField = new JTextField();

        chooseFileButton = new JButton("Choose File");
        connectButton = new JButton("Connect");

        pathLabel = new JLabel();
        pathLabel.setBorder(BorderFactory.createEmptyBorder(
                0, 10,0,40
        ));

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
        lower.add(connectButton);
        lower.add(Box.createRigidArea(new Dimension(5, 0)));
        lower.add(chooseFileButton);
        lower.add(pathLabel);

        this.add(upper);
        this.add(lower);
        mainFrame.getContentPane().add(BorderLayout.NORTH, this);
    }

    public JButton getChooseFileButton() {
        return chooseFileButton;
    }
    public JTextField getIpField() {
        return ipField;
    }
    public void setPath(String path) {
        pathLabel.setText(path);
    }
    public JButton getConnectButton() {
        return connectButton;
    }
    public void setChosenFile(File chosenFile) {
        this.chosenFile = chosenFile;
    }
    public File getChosenFile() {
        return chosenFile;
    }
}
