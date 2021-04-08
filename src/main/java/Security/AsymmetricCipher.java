package Security;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.*;

public class AsymmetricCipher {
    private static final String RSA = "RSA";

    // Generating public and private keys using RSA algorithm.
    public static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        SecureRandom secureRandom = new SecureRandom();
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
        keyPairGenerator.initialize(2048, secureRandom);

        return keyPairGenerator.generateKeyPair();
    }

    public static byte [] wrapSessionKeyWithPublicKey(SecretKey sessionKey, PublicKey publicKey){
        Cipher cipher = null;
        try{
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.WRAP_MODE, publicKey);
            return cipher.wrap(sessionKey);
        }catch (NoSuchAlgorithmException | InvalidKeyException
                | IllegalBlockSizeException
                | NoSuchPaddingException e){
            e.printStackTrace();
        }
        return null;
    }

    public static SecretKey unwrapSessionKeyWithPrivateKey(byte [] wrappedKey, PrivateKey privateKey){
        Cipher cipher = null;
        try{
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.UNWRAP_MODE, privateKey);
            return (SecretKey) cipher.unwrap(wrappedKey, "AES", Cipher.SECRET_KEY);

        }catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException e){
            e.printStackTrace();
        }
        return null;
    }
}
