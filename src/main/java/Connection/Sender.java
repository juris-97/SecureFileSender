package Connection;


import GUI.Bottom;
import GUI.Top;
import Keys.AsymmetricCypher;
import Keys.KeyHandler;
import Keys.SymmetricCypher;

import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.Socket;
import java.security.PublicKey;


public class Sender {
    Socket socket;
    OutputStream outStream;
    DataOutputStream dOut;

    Top top;
    Bottom bottom;
    boolean connected;
    KeyHandler keyHandler;

    public Sender(Top top, Bottom bottom, KeyHandler keyHandler){
        this.top = top;
        this.bottom = bottom;
        this.keyHandler = keyHandler;
    }

    public void establishConnection(PublicKey publicKey){
        String ip = top.getIpField().getText();
        if(ip.equals("")) return;

        try{
            socket = new Socket(ip, 8181);
            outStream = socket.getOutputStream();
            dOut = new DataOutputStream(outStream);
            connected = true;

            sendPublicKey();
            System.out.println("Connection established..");

        }catch (Exception e1){
            e1.printStackTrace();
        }
    }

    public void disconnect(){
        try{
            if(socket != null) socket.close();
            if(outStream != null) outStream.close();
            if(dOut != null) dOut.close();

            connected = false;
            top.getConnectButton().setText("Connect");

            System.out.println("Disconnected");
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void sendPublicKey(){

        PublicKey publicKey = keyHandler.getPublicKey();
        byte [] bytes = publicKey.getEncoded();
        //System.out.println("Public key client A: " + DatatypeConverter.printHexBinary(pubKey.getEncoded()));

        try(BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(bytes))){

            dOut.writeUTF("B"); // defining protocol

            bis.read(bytes, 0, bytes.length);
            dOut.write(bytes, 0, bytes.length);
            dOut.flush();
            System.out.println("Public Key: " + DatatypeConverter.printHexBinary(publicKey.getEncoded()) + "\nSEND!");

        }catch (IOException e){
            e.getStackTrace();
        }
    }

    public void sendWrappedSessionKeyAndInitVector(){

        System.out.println("Session Key: " + DatatypeConverter.printHexBinary(keyHandler.getGeneratedSessionKey().getEncoded()));

        byte [] initVector = keyHandler.getInitialisedVector();
        byte [] wrappedSessionKey = AsymmetricCypher.wrapSessionKeyWithPublicKey(
                keyHandler.getGeneratedSessionKey(),
                keyHandler.getPublicKey());

        try{
            dOut.writeUTF("C"); // defining protocol
            dOut.writeInt(initVector.length);
            dOut.writeInt(wrappedSessionKey.length);

            dOut.write(initVector);
            dOut.write(wrappedSessionKey);
            dOut.flush();

            System.out.println("Init vector" + DatatypeConverter.printHexBinary(initVector) + " with size: " + initVector.length + "\nSEND");
            System.out.println("Wrapped Session Key: " + DatatypeConverter.printHexBinary(wrappedSessionKey) + " with size: " + wrappedSessionKey.length + " \nSEND");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendFile() {
        File file = top.getChosenFile();
        try (FileInputStream fis = new FileInputStream(file)) {

            byte[] encryptedFile = SymmetricCypher.encryptFile(file,
                    keyHandler.getGeneratedSessionKey(),
                    keyHandler.getInitialisedVector());

            dOut.writeUTF("A"); // defining protocol
            dOut.writeUTF(file.getName());
            dOut.writeLong(encryptedFile.length);

            byte[] buffer = new byte[4096];

            ByteArrayInputStream bais = new ByteArrayInputStream(encryptedFile);

            int read = 0;
            int remaining = (int) file.length();

            while ((read = bais.read(buffer, 0, Math.min(buffer.length, remaining))) > 0)
                dOut.write(buffer, 0, read);

            System.out.println("Encrypted File sent!");
            bais.close();
            dOut.flush();

        } catch (IOException e1) {
            System.out.println(e1.getMessage());
        }
    }
    public boolean isConnected() {
        return connected;
    }
}