package GUI.Center.LeftParts;

import javax.swing.*;
import java.awt.*;

public class LeftBottom extends JPanel {

    JLabel cypherMethodLabel;

    private static final int WIDTH  = (int) (41 * 9.5);     // 390
    private static final int HEIGHT = (int) (40 * 7 / 2.0); // 140

    JRadioButton ECB;
    JRadioButton CFB;
    JRadioButton OCF;
    JRadioButton OFB;


    public LeftBottom(){
        cypherMethodLabel = new JLabel("Choose cypher method: ");

        ECB = new JRadioButton("ECB");
        CFB = new JRadioButton("CFB");
        OCF = new JRadioButton("OCF");
        OFB = new JRadioButton("OFB");
        setPanel();
    }

    public void setPanel(){

        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        this.setBorder(BorderFactory.createEmptyBorder(20, 5, 10, 240));

        JPanel upper = new JPanel();
        upper.setLayout(new BoxLayout(upper, BoxLayout.X_AXIS));
        upper.setPreferredSize(new Dimension(WIDTH, HEIGHT/2));
        upper.add(cypherMethodLabel);

        JPanel lower = new JPanel();
        GridLayout grid = new GridLayout(2,2);
        lower.setLayout(grid);
        lower.setBorder(BorderFactory.createEmptyBorder(5, 5, 60, 0));
        ButtonGroup group = new ButtonGroup();

        group.add(ECB);
        group.add(CFB);
        group.add(OCF);
        group.add(OFB);

        lower.add(ECB);
        lower.add(CFB);
        lower.add(OCF);
        lower.add(OFB);


        this.add(upper);
        this.add(lower);
    }
}
