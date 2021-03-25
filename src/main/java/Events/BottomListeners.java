package Events;

import Connection.Receiver;
import Connection.Sender;
import GUI.Bottom;
import GUI.Center.Left;
import GUI.Center.Right;
import GUI.Top;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class BottomListeners {

    private final Top top;
    private final Bottom bottom;
    private final Right rightSide;
    private final Left leftSide;
    private final Sender sender;
    private final Receiver receiver;

    public BottomListeners(Bottom bottom, Top top, Left leftSide, Right rightSide, Sender sender, Receiver receiver){
        this.bottom = bottom;
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.top = top;
        this.sender = sender;
        this.receiver = receiver;
        bottom.getSend().addActionListener(new SendButtonListener());
    }

    class SendButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            //System.out.print("SEND CLICKED - TO: ");
            //System.out.println(top.getIpField().getText());
            try{
                //sender.sendFile(top.getChosenFile());
                sender.send();
            }catch (Exception e1){
                e1.getStackTrace();
            }

            bottom.getProgressBar().setValue(100);
        }
    }
}