package Connection;


import Connection.PubKeyExchange.Exchange;
import Security.KeyHandler;
import Security.SymmetricCipher;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.swing.*;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.Socket;
import java.security.PublicKey;

public class Sender {

    Socket socket;

    OutputStream outStream;
    InputStream inputStream;

    DataOutputStream dOut;
    DataInputStream dIn;

    private boolean connected = false;

    public void disconnect(){
        try{
            socket.close();
            outStream.close();
            inputStream.close();
            dOut.close();
            dIn.close();
            connected = false;
            System.out.println("Disconnected.");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void exchangePublicKey(PublicKey key, DataOutputStream dOut, DataInputStream dis, KeyHandler keyHandler){
        Exchange.sendPublicKey(key, dOut);
        Exchange.receivePublicKey(dis, keyHandler);

        //System.out.println("[Send in SIDE = 1] Public Key: " + DatatypeConverter.printHexBinary(keyHandler.getPublicKey().getEncoded()));
        //System.out.println("[Rece in SIDE = 1] Public Key: " + DatatypeConverter.printHexBinary(keyHandler.getReceivedPublicKey().getEncoded()));
    }

    public void sendWrappedSessionKey(byte [] wrappedSessionKey){
        try{
            dOut.writeUTF("C");
            dOut.writeInt(wrappedSessionKey.length);
            dOut.write(wrappedSessionKey);
            dOut.flush();
            System.out.println("[Send in SIDE = 1] Wrapped Session Key: " + DatatypeConverter.printHexBinary(wrappedSessionKey));

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendFile(File file, String algorithmMod, SecretKey sessionKey, byte [] initVector){

        String algorithm = "AES/" + algorithmMod + "/PKCS5PADDING";
        byte [] encryptedFile = SymmetricCipher.encryptFile(file, sessionKey, initVector, algorithm);

        try{
            dOut.writeUTF("A"); // defining protocol
            dOut.writeUTF(file.getName());
            dOut.writeLong(encryptedFile.length);
            dOut.writeUTF(algorithm);

            byte[] buffer = new byte[4096];
            int read = 0;
            int remaining = (int) file.length();

            ByteArrayInputStream bais = new ByteArrayInputStream(encryptedFile);

            while ((read = bais.read(buffer, 0, Math.min(buffer.length, remaining))) > 0)
                dOut.write(buffer, 0, read);

            System.out.println("[SIDE = 1] Encrypted File sent!");
            dOut.flush();
            bais.close();


        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public void sendInitVector(byte [] initVector){
        try{
            dOut.writeUTF("I");
            dOut.write(initVector);
            dOut.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public int establishConnection(PublicKey publicKey, String ip) {
        try{
            socket = new Socket(ip, 8181);
            outStream = socket.getOutputStream();
            inputStream = socket.getInputStream();

            dOut = new DataOutputStream(outStream);
            dIn = new DataInputStream(inputStream);

            connected = true;
            System.out.println("Connection established!");
            return 0;

        }catch (IOException e){
            JOptionPane.showMessageDialog(
                    null,
                    "Probably server is off..",
                    "InfoBox: " + "Something went wrong", JOptionPane.INFORMATION_MESSAGE);
            return -1;
        }
    }

    public DataInputStream getdIn() {
        return dIn;
    }
    public DataOutputStream getdOut() {
        return dOut;
    }
    public boolean isConnection() {
        return connected;
    }
}
