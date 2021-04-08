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


    public static byte[] encryptFile(File file, SecretKey sessionKey, byte [] initVector, String CIPHER_ALGORITHM, Bottom bottom){

        try{
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            if(initVector != null){
                IvParameterSpec ivParameterSpec = new IvParameterSpec(initVector);
                cipher.init(Cipher.ENCRYPT_MODE, sessionKey, ivParameterSpec);
            }else {
                cipher.init(Cipher.ENCRYPT_MODE, sessionKey);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream((int)file.length() + 16);

            byte[] plainBuf = new byte[8192];
            try (InputStream in = Files.newInputStream(file.toPath());
                 OutputStream out = new BufferedOutputStream(baos)) {

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
            byte [] encryptedBytes = baos.toByteArray();
            baos.close();
            return encryptedBytes;

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

    public static File decryptFile(byte [] encryptedFile, SecretKey sessionKey, byte [] initVector, String filename ,String CIPHER_ALGORITHM){
        try{

            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            if(initVector != null){
                IvParameterSpec ivParameterSpec = new IvParameterSpec(initVector);
                cipher.init(Cipher.DECRYPT_MODE, sessionKey, ivParameterSpec);
            }else{
                cipher.init(Cipher.DECRYPT_MODE, sessionKey);
            }


            File decryptedFile = new File(filename);
            FileOutputStream fos = new FileOutputStream(decryptedFile);

            byte[] plainBuf = new byte[8192];
            try (InputStream in = new BufferedInputStream(new ByteArrayInputStream(encryptedFile));
                 OutputStream out = new BufferedOutputStream(fos)) {
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
            fos.close();
            return decryptedFile;

        }catch (NoSuchAlgorithmException
                | NoSuchPaddingException
                | IllegalBlockSizeException
                | BadPaddingException
                | InvalidKeyException
                | IOException
                | InvalidAlgorithmParameterException e){
            e.printStackTrace();
        }

        return null;
    }
}