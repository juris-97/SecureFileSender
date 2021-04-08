package Connection.PubKeyExchange;

import Security.KeyHandler;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class Exchange {

    public static void sendPublicKey(PublicKey publicKey, DataOutputStream dOut){
        byte [] pubKeyBytes = publicKey.getEncoded();

        try(BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(pubKeyBytes))){

            int read = bis.read(pubKeyBytes);
            dOut.write(pubKeyBytes, 0, read);
            dOut.flush();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void receivePublicKey(DataInputStream dis, KeyHandler keyHandler){
        PublicKey receivedPublicKey = null;
        byte [] recPubBytes = new byte[2048];

        try{
            dis.read(recPubBytes);
            receivedPublicKey = KeyFactory.getInstance("RSA")
                    .generatePublic(new X509EncodedKeySpec(recPubBytes));

            keyHandler.setReceivedPublicKey(receivedPublicKey);
        }catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e){
            e.printStackTrace();
        }
    }
}
