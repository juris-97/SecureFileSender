package Events;

import Connection.Sender;
import GUI.Sides.Top;
import Security.KeyHandler;

import javax.swing.*;
import javax.xml.bind.DatatypeConverter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TopListeners {

    private final Top top;
    private final JFileChooser fc;
    private final Sender sender;
    private KeyHandler keyHandler;

    public TopListeners(Top top, Sender sender, KeyHandler keyHandler){
        this.top = top;
        this.sender = sender;
        this.keyHandler = keyHandler;
        this.fc  = new JFileChooser();

        top.getChooseFileButton().addActionListener(new ChooseFileListener());
        top.getConnectButton().addActionListener(new ConnectListener());
    }

    class ChooseFileListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int ret = fc.showOpenDialog(top);

            if(ret == JFileChooser.APPROVE_OPTION){
                top.setChosenFile(fc.getSelectedFile());
                top.setPath(top.getChosenFile().getPath());
            }
        }
    }

    class ConnectListener implements ActionListener{

        public boolean checkIpValidation(String ip){

            try{
                if ( ip == null || ip.isEmpty()) return false;


                String[] parts = ip.split( "\\." );
                if ( parts.length != 4 ) return false;


                for ( String s : parts ) {
                    int i = Integer.parseInt( s );
                    if ( (i < 0) || (i > 255) ) return false;
                }

                if ( ip.endsWith(".") ) return false;

            }catch (NumberFormatException  e){
                e.printStackTrace();
            }

            return true;
        }

        public void actionPerformed(ActionEvent e) {

            String ipTo = top.getIpField().getText();

            if(!checkIpValidation(ipTo)){
                JOptionPane.showMessageDialog(
                        null,
                        "IP address you put is not valid",
                        "InfoBox: " + "IP ERROR", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if(!keyHandler.isKeyPairInitialised())
                keyHandler.setNewPrivateAndPublicKey();

            if(!sender.isConnection()){
                String ip = top.getIpField().getText();
                if(sender.establishConnection(null, ip) > -1){
                    top.getConnectButton().setText("Disconnect");
                    sender.exchangePublicKey(
                            keyHandler.getPublicKey(),
                            sender.getdOut(),
                            sender.getdIn(),
                            keyHandler);

                    //keyHandler.setReceivedPublicKey(sender.receivePublicKeyFromOther());
                }
            } else{
                sender.disconnect();
                keyHandler.resetPrivateAndPublicKeys();
                top.getConnectButton().setText("Connect");
            }
        }
    }
}
