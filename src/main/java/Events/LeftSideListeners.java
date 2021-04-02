package Events;

import Connection.Sender;
import GUI.Center.Left;
import Keys.AsymmetricCypher;
import Keys.SymmetricCypher;

import javax.crypto.SecretKey;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LeftSideListeners {

    private final Left leftSide;
    private SecretKey sessionKey;
    Sender sender;
    //Enumeration<AbstractButton> radioButtons;

    public LeftSideListeners(Left leftSide, Sender sender){
        this.leftSide = leftSide;

        this.leftSide.getLeftTop().getGenerateButton().addActionListener(new GenerateSessionKeyListener());
        this.leftSide.getLeftTop().getExchangeButton().addActionListener(new ExchangeSessionKeyListener());

        this.sender = sender;
        //radioButtons = leftSide.getLeftBottom().getButtonGroup().getElements();
    }


    class GenerateSessionKeyListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            sessionKey = SymmetricCypher.createSessionKey();
            System.out.println("GENERATE SESSION KEY CLICKED");
        }
    }

    class ExchangeSessionKeyListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(sessionKey != null){

                // wrap session key with publicKey of user B
                //byte [] wrappedSessionKey = AsymmetricCypher.wrapSessionKeyWithPublicKey(sessionKey, receivedPublicKey)
                // send wrapped session key

            }
            System.out.println("EXCHANGE SESSION KEY CLICKED");
        }
    }
}
