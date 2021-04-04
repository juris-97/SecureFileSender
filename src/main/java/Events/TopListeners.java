package Events;

import Connection.Sender;
import GUI.Top;
import Keys.AsymmetricCypher;
import Keys.KeyHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.PublicKey;

public class TopListeners {

    private final Top top;
    private Sender sender;
    private KeyHandler keyHandler;

    final JFileChooser fc = new JFileChooser();

    public TopListeners(Top top, Sender sender, KeyHandler keyHandler){
        this.top = top;
        this.sender = sender;
        this.keyHandler = keyHandler;

        top.getChooseFileButton().addActionListener(new ChooseFileListener());
        top.getConnectButton().addActionListener(new ConnectListener());
    }

    class ChooseFileListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
           int ret = fc.showOpenDialog(top);

           if(ret == JFileChooser.APPROVE_OPTION){
               top.setChosenFile(fc.getSelectedFile());
               top.setPath(top.getChosenFile().getPath());
           }
        }
    }

    class ConnectListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            sender.establishConnection(keyHandler.getPublicKey());
        }
    }
}