package Events;

import javax.crypto.SecretKey;
import javax.swing.*;
import java.io.File;

public class ErrorHandler {

    public static boolean sessionKeyExchangeCheck(boolean exchanged){
        if(!exchanged){
            showErrorDialog("You should  exchange session key first for save file transfer",
                    "InfoBox: error while sending");
            return false;
        }
        return true;
    }

    public static boolean sessionKeyGeneratedCheck(SecretKey sessionKey){
        if(sessionKey == null){
            showErrorDialog("You should generate session key first for save file transfer",
                    "InfoBox: error while sending");
            return false;
        }
        return true;
    }

    public static boolean fileToSendChosenCheck(File fileToSend){
        if(fileToSend == null){
            showErrorDialog("No file selected", "InfoBox: error while sending");
            return false;
        }
        return true;
    }

    public static boolean selectedAlgorithmCheck(JRadioButton selectedAlgorithm){
        if(selectedAlgorithm == null){
            showErrorDialog("No selected method of cipher", "InfoBox: error while sending");
            return false;
        }
        return true;
    }

    public static boolean connectionCheck(boolean connection){
        if(!connection){
            showErrorDialog("Cannot send file, no connection..", "InfoBox: error while sending");
            return false;
        }
        return true;
    }
    public static void showErrorDialog(String msg, String info){
        JOptionPane.showMessageDialog(null, msg, info, JOptionPane.INFORMATION_MESSAGE);
    }
}
