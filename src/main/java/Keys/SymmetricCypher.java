package Keys;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SymmetricCypher {
    private static final String AES_CIPHER_ALGORITHM = "AES/CBC/PKCS5PADDING";

    public static SecretKey createSessionKey(){
        try{
            SecureRandom secureRandom = new SecureRandom();
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256, secureRandom);

            return keyGen.generateKey();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] createInitializationVector()
    {
        // Used with encryption
        byte[] initializationVector = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(initializationVector);
        return initializationVector;
    }

    public static byte[] encryptFile(File file, SecretKey sessionKey, byte [] initVector){

        try{
            Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initVector);
            cipher.init(Cipher.ENCRYPT_MODE, sessionKey, ivParameterSpec);

            return cipher.doFinal(Files.readAllBytes(file.toPath()));
        }catch (NoSuchAlgorithmException
                | NoSuchPaddingException
                | IOException
                | IllegalBlockSizeException
                | BadPaddingException
                | InvalidKeyException
                | InvalidAlgorithmParameterException e){
            e.printStackTrace();
        }

        return null;
    }

    public static byte [] decryptFile(byte [] encryptedFile, SecretKey sessionKey, byte [] initVector){
        try{

            Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initVector);
            cipher.init(Cipher.DECRYPT_MODE, sessionKey, ivParameterSpec);

            return cipher.doFinal(encryptedFile);
        }catch (NoSuchAlgorithmException
                | NoSuchPaddingException
                | IllegalBlockSizeException
                | BadPaddingException
                | InvalidKeyException
                | InvalidAlgorithmParameterException e){
            e.printStackTrace();
        }

        return null;
    }
}
