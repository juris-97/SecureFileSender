package Events;

import Connection.Sender;
import GUI.Center.Left;
import Keys.AsymmetricCypher;
import Keys.KeyHandler;
import Keys.SymmetricCypher;

import javax.crypto.SecretKey;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.KeyPair;

public class LeftSideListeners {

    private final Left leftSide;
    Sender sender;
    KeyHandler keyHandler;
    //Enumeration<AbstractButton> radioButtons;

    public LeftSideListeners(Left leftSide, Sender sender, KeyHandler keyHandler){
        this.leftSide = leftSide;

        this.leftSide.getLeftTop().getGenerateButton().addActionListener(new GenerateSessionKeyListener());
        this.leftSide.getLeftTop().getExchangeButton().addActionListener(new ExchangeSessionKeyListener());

        this.keyHandler = keyHandler;
        this.sender = sender;
        //radioButtons = leftSide.getLeftBottom().getButtonGroup().getElements();
    }


    class GenerateSessionKeyListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            keyHandler.setSessionKey(SymmetricCypher.createSessionKey());
            keyHandler.setInitialisedVector(SymmetricCypher.createInitializationVector());
            System.out.println("GENERATE SESSION KEY CLICKED");
        }
    }

    class ExchangeSessionKeyListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(keyHandler.getGeneratedSessionKey() != null && keyHandler.getInitialisedVector() != null){

                // wrap session key with publicKey of user B
                byte [] wrappedSessionKey
                        = AsymmetricCypher.wrapSessionKeyWithPublicKey(keyHandler.getGeneratedSessionKey(),
                        keyHandler.getOtherPublicKey());
                // send wrapped session key
                sender.sendWrappedSessionKeyAndInitVector();
                System.out.println("Wrapped Session Key Send");
            }
            System.out.println("EXCHANGE SESSION KEY CLICKED");
        }
    }
}
