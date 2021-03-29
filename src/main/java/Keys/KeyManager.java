package Keys;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.security.*;
import java.util.Base64;

public class KeyManager {
    private PublicKey  publicKey;
    private PrivateKey privateKey;
    private SecretKey  sessionKey;

    private PublicKey  partnerPublicKey;

    private KeyPairGenerator keyPairGen;
    private KeyGenerator keygen;

    private String publicKeyFile = "key.pub";

    public KeyManager(){
        genPairKeys();
        genSessionKey();
        savePublicKeyOnDisk(this.publicKey, "publicKey.pub");
    }

    private void genSessionKey(){
        try{
            keygen = KeyGenerator.getInstance("AES");
            sessionKey = keygen.generateKey();

            Base64.Encoder encoder;
            encoder = Base64.getEncoder();

            try{
                Writer out = new FileWriter("session.key");
                out.write(encoder.encodeToString(publicKey.getEncoded()));

                out.close();
            }catch (IOException e){
                e.getStackTrace();
            }

        }catch (NoSuchAlgorithmException e){
            e.getStackTrace();
        }
    }

    private void genPairKeys(){
        try{
            keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(2048);

            KeyPair keyPair = keyPairGen.generateKeyPair();
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();

        }catch (NoSuchAlgorithmException e){
            e.getStackTrace();
        }
    }

    public void savePublicKeyOnDisk(Key key, String filename){
        Base64.Encoder encoder;
        encoder = Base64.getEncoder();


        try{
            Writer out = new FileWriter(filename);
            out.write("-----BEGIN RSA PUBLIC KEY-----\n");
            out.write(encoder.encodeToString(key.getEncoded()));
            out.write("\n-----END RSA PUBLIC KEY-----\n");
            out.close();

        }catch (IOException e){
            e.getStackTrace();
        }

    }


    public void setPartnerPublicKey(PublicKey partnerPublicKey) {
        this.partnerPublicKey = partnerPublicKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
