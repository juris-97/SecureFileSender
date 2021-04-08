package Events;


import Connection.Sender;
import GUI.Sides.Bottom;
import GUI.Sides.Center.LeftParts.LeftBottom;
import GUI.Sides.Top;
import Security.KeyHandler;
import Security.SymmetricCipher;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import static Events.ErrorHandler.*;

public class BottomListeners {

    private final Top top;
    private final Bottom bottom;
    private final Sender sender;
    private final KeyHandler keHandler;
    private final LeftBottom leftBottom;

    public BottomListeners(Bottom bottom, Top top, LeftBottom leftBottom , KeyHandler keyHandler, Sender sender){
        this.top = top;
        this.bottom = bottom;
        this.leftBottom = leftBottom;
        this.keHandler = keyHandler;
        this.sender = sender;
        this.bottom.getSendButton().addActionListener(new SendButtonListener());
    }

    public class SendButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {

            // check if client connected
            if(!connectionCheck(sender.isConnection())) return;

            // check if algorithm method is selected
            JRadioButton selectedAlgorithm =  leftBottom.getSelectedButton();
            if(!selectedAlgorithmCheck(selectedAlgorithm)) return;

            // check if file for sending is selected
            File fileToSend = top.getChosenFile();
            if(!fileToSendChosenCheck(fileToSend)) return;

            // check if session key was generated
            if(!sessionKeyGeneratedCheck(keHandler.getSessionKey())) return;

            // check if session key was exchanged
            if(!sessionKeyExchangeCheck(keHandler.isSessionKeyExchange())) return;

            String algorithmMod = selectedAlgorithm.getText();
            if(!algorithmMod.equals("ECB")){
                byte [] initVector = SymmetricCipher.createInitializationVector();
                keHandler.setInitialVector(initVector);
                sender.sendInitVector(initVector);
            }
            sender.sendFile(fileToSend, selectedAlgorithm.getText(), keHandler.getSessionKey(), keHandler.getInitialVector());

            keHandler.setSessionKeyExchange(false);
            keHandler.setSessionKey(null);
            keHandler.setInitialVector(null);
        }

    }
}
