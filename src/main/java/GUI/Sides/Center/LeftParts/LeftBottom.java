package GUI.Sides.Center.LeftParts;

import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;

public class LeftBottom extends JPanel {

    JLabel cypherMethodLabel;

    private static final int WIDTH  = (int) (41 * 9.5);     // 390
    private static final int HEIGHT = (int) (40 * 7 / 2.0); // 140

    JRadioButton ECB;
    JRadioButton CBC;
    JRadioButton CFB;
    JRadioButton OFB;

    ButtonGroup buttonGroup;

    public LeftBottom(){
        cypherMethodLabel = new JLabel("Choose cypher method: ");

        ECB = new JRadioButton("ECB");
        CBC = new JRadioButton("CBC");
        CFB = new JRadioButton("CFB");
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
        buttonGroup = new ButtonGroup();

        buttonGroup.add(ECB);
        buttonGroup.add(CBC);
        buttonGroup.add(CFB);
        buttonGroup.add(OFB);

        lower.add(ECB);
        lower.add(CBC);
        lower.add(CFB);
        lower.add(OFB);


        this.add(upper);
        this.add(lower);
    }

    public JRadioButton getSelectedButton(){
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                return (JRadioButton) button;
            }
        }

        return null;
    }
}
