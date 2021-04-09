package Security;

import GUI.Sides.Bottom;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.nio.file.Files;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SymmetricCipher {

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


    public static File encryptFile(File file, SecretKey sessionKey, byte [] initVector, String CIPHER_ALGORITHM, Bottom bottom){

        try{
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            if(initVector != null){
                IvParameterSpec ivParameterSpec = new IvParameterSpec(initVector);
                cipher.init(Cipher.ENCRYPT_MODE, sessionKey, ivParameterSpec);
            }else {
                cipher.init(Cipher.ENCRYPT_MODE, sessionKey);
            }


            File encryptedFile = new File("tmp");
            FileOutputStream fos = new FileOutputStream(encryptedFile);

            byte[] plainBuf = new byte[8192];
            try (InputStream in = Files.newInputStream(file.toPath());
                 OutputStream out = new BufferedOutputStream(fos)) {

                int nread;
                double loopTimes = (double) file.length() / plainBuf.length;
                double step = loopTimes != 0 ? (50 / loopTimes) : 50;
                double progress = 0;

                while ((nread = in.read(plainBuf)) > 0) {
                    byte[] enc = cipher.update(plainBuf, 0, nread);
                    progress += step;
                    bottom.getProgressBar().setValue((int) progress);
                    out.write(enc);
                }
                byte[] enc = cipher.doFinal();
                out.write(enc);
            }

            fos.close();
            return encryptedFile;

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

    public static File decryptFile(File file, SecretKey sessionKey, byte [] initVector, String filename ,String CIPHER_ALGORITHM){
        try{

            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            if(initVector != null){
                IvParameterSpec ivParameterSpec = new IvParameterSpec(initVector);
                cipher.init(Cipher.DECRYPT_MODE, sessionKey, ivParameterSpec);
            }else{
                cipher.init(Cipher.DECRYPT_MODE, sessionKey);
            }


            File decryptedFile = new File(filename);

            byte[] plainBuf = new byte[8192];
            try (InputStream in = new FileInputStream(file);
                 OutputStream out = new FileOutputStream(decryptedFile)) {
                int nread;
                while ((nread = in.read(plainBuf)) > 0) {
                    byte[] enc = cipher.update(plainBuf, 0, nread);
                    out.write(enc);
                }
                byte[] enc = cipher.doFinal();
                out.write(enc);
            }catch (IOException e){
                e.printStackTrace();
            }

            return decryptedFile;

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