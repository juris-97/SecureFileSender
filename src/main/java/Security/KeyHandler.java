package Security;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class KeyHandler {
    private PrivateKey privateKey;
    private PublicKey  publicKey;
    private SecretKey  sessionKey;
    private byte [] initialVector;

    private PublicKey receivedPublicKey;
    private SecretKey receivedSessionKey; // wrapped
    private byte []   receivedInitVector;

    private boolean sessionKeyExchange;

    public KeyHandler(){
        setNewPrivateAndPublicKey();
    }
    public void setNewPrivateAndPublicKey(){
        try{
            KeyPair pair = AsymmetricCipher.generateRSAKeyPair();
            this.privateKey = pair.getPrivate();
            this.publicKey  = pair.getPublic();
            this.sessionKeyExchange = false;
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }

    public void resetPrivateAndPublicKeys(){
        this.publicKey = null;
        this.privateKey = null;
    }

    public boolean isKeyPairInitialised(){
        return (privateKey != null) || (publicKey != null);
    }

    public void setSessionKey(SecretKey sessionKey) { this.sessionKey = sessionKey; }
    public void setReceivedSessionKey(SecretKey receivedSessionKey) { this.receivedSessionKey = receivedSessionKey; }
    public void setReceivedPublicKey(PublicKey receivedPublicKey) { this.receivedPublicKey = receivedPublicKey; }
    public void setInitialVector(byte[] initialVector) { this.initialVector = initialVector; }
    public void setReceivedInitVector(byte[] receivedInitVector) { this.receivedInitVector = receivedInitVector; }


    public PrivateKey getPrivateKey() {
        return privateKey;
    }
    public PublicKey  getPublicKey()  {
        return publicKey;
    }
    public PublicKey  getReceivedPublicKey() {
        return receivedPublicKey;
    }

    public SecretKey  getSessionKey() {
        return sessionKey;
    }
    public SecretKey  getReceivedSessionKey() {
        return receivedSessionKey;
    }

    public byte[] getInitialVector() {
        return initialVector;
    }
    public byte[] getReceivedInitVector() {
        return receivedInitVector;
    }

    public void setSessionKeyExchange(boolean sessionKeyExchange) {
        this.sessionKeyExchange = sessionKeyExchange;
    }
    public boolean isSessionKeyExchange() {
        return sessionKeyExchange;
    }
}
