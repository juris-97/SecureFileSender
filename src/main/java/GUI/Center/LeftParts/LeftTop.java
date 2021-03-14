package GUI.Center.LeftParts;

import javax.swing.*;
import java.awt.*;

public class LeftTop extends JPanel {

    JLabel sessionKeyLabel;
    JLabel getSessionKeyLabel;
    JButton generateButton;
    JButton exchangeButton;

    private static final int WIDTH   = (int) (41 * 9.5);      // 390
    private static final int HEIGHT  = (int) (40 * 7 / 2.0);  // 140

    public LeftTop(){

        sessionKeyLabel = new JLabel("Session key: ");
        getSessionKeyLabel = new JLabel("");
        generateButton = new JButton("Generate");
        exchangeButton = new JButton("Exchange");

        setPanel();
    }

    public JPanel createPanel(int marginRight){
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT / 2));
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(5,5,0,marginRight));
        return panel;
    }

    public void setPanel(){
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT / 2));

        JPanel upper = createPanel(300);
        upper.add(sessionKeyLabel);
        upper.add(getSessionKeyLabel);

        JPanel lower = createPanel(200);
        lower.add(generateButton);
        lower.add(Box.createRigidArea(new Dimension(5, 0)));
        lower.add(exchangeButton);

        this.add(upper);
        this.add(lower);
    }
}
