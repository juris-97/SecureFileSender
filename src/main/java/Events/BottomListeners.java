package Events;

import GUI.Bottom;
import GUI.Center.Left;
import GUI.Center.Right;
import GUI.Top;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BottomListeners {

    private final Top top;
    private final Bottom bottom;
    private final Right rightSide;
    private final Left leftSide;

    public BottomListeners(Bottom bottom, Top top, Left leftSide, Right rightSide){
        this.bottom = bottom;
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.top = top;
        bottom.getSend().addActionListener(new SendButtonListener());
    }

    class SendButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            System.out.print("SEND CLICKED - TO: ");
            System.out.println(top.getIpField().getText());
            bottom.getProgressBar().setValue(100);
        }
    }
}