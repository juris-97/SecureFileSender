package Events;

import Connection.Sender;
import GUI.Sides.Center.Left;
import GUI.Sides.Center.LeftParts.LeftTop;
import Security.AsymmetricCipher;
import Security.KeyHandler;
import Security.SymmetricCipher;

import javax.crypto.SecretKey;
import javax.swing.*;
import javax.xml.bind.DatatypeConverter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static Events.ErrorHandler.showErrorDialog;

public class LeftSideListeners {
    private final LeftTop leftTop;
    private final KeyHandler keyHandler;
    private final Sender sender;

    public LeftSideListeners(Left left, KeyHandler keyHandler, Sender sender){
        this.leftTop = left.getLeftTop();
        this.keyHandler = keyHandler;
        this.sender = sender;

        this.leftTop.getGenerateButton().addActionListener(new GenerateSessionKeyListener());
        this.leftTop.getExchangeButton().addActionListener(new ExchangeSessionKeyListener());
    }

    class GenerateSessionKeyListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            keyHandler.setSessionKey(SymmetricCipher.createSessionKey());
            JOptionPane.showMessageDialog(
                    null,
                    "Session Key Generated!",
                    "InfoBox: " + "success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    class ExchangeSessionKeyListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            SecretKey secretKey = keyHandler.getSessionKey();
            if(secretKey == null){
                showErrorDialog("Before exchanging session key you should generate it!", "InfoBox: error while exchanging");
                return;
            }
            if(!sender.isConnection()){
                showErrorDialog("Before exchange you need to connect..", "InfoBox: error while exchanging");
                return;
            }
            System.out.println("[SIDE = 1] Session Key: " + DatatypeConverter.printHexBinary(keyHandler.getSessionKey().getEncoded()));
            byte [] wrappedSessionKey = AsymmetricCipher.wrapSessionKeyWithPublicKey(secretKey, keyHandler.getReceivedPublicKey());
            sender.sendWrappedSessionKey(wrappedSessionKey);
            keyHandler.setSessionKeyExchange(true);
        }
    }
}
