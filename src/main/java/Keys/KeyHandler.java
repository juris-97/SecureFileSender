package Keys;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class KeyHandler {

    private PrivateKey privateKey;
    private volatile PublicKey publicKey;
    private PublicKey otherPublicKey;
    private volatile SecretKey generatedSessionKey;
    private byte [] receivedWrappedSessionKey;
    private byte [] InitialisedVector;
    private byte [] receivedInitVector;

    public KeyHandler() throws Exception{
        KeyPair pair = AsymmetricCypher.generateRSAKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    public void setReceivedInitVector(byte[] receivedInitVector) {
        this.receivedInitVector = receivedInitVector;
    }
    public void setInitialisedVector(byte [] initVector) { this.InitialisedVector = initVector; }
    public void setReceivedSessionKey(byte [] receivedSessionKey) { this.receivedWrappedSessionKey = receivedSessionKey; }
    public void setSessionKey(SecretKey sessionKey) {
        this.generatedSessionKey = sessionKey;
    }
    public void setOtherPublicKey(PublicKey otherPublicKey) {
        this.otherPublicKey = otherPublicKey;
    }

    public byte[] getReceivedInitVector() {
        return receivedInitVector;
    }
    public byte [] getReceivedWrappedSessionKey() {
        return receivedWrappedSessionKey;
    }
    public byte[] getInitialisedVector() {
        return InitialisedVector;
    }
    public PublicKey getOtherPublicKey() {
        return otherPublicKey;
    }
    public PublicKey getPublicKey() {
        return publicKey;
    }
    public PrivateKey getPrivateKey() {
        return privateKey;
    }
    public SecretKey getGeneratedSessionKey() {
        return generatedSessionKey;
    }
}
