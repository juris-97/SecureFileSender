package Connection;


import Connection.PubKeyExchange.Exchange;
import GUI.Sides.Bottom;
import Security.KeyHandler;
import Security.SymmetricCipher;

import javax.crypto.SecretKey;
import javax.swing.*;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.security.PublicKey;
import java.time.Duration;
import java.time.Instant;

public class Sender{

    Socket socket;

    OutputStream outStream;
    InputStream inputStream;

    DataOutputStream dOut;
    DataInputStream dIn;

    private boolean connected = false;
    private final Bottom bottom;

    public Sender(Bottom bottom){
        this.bottom = bottom;
    }

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

    public class FileSender implements Runnable{
        private final File file;
        private final String algorithmMod;
        private final SecretKey sessionKey;
        private final byte [] initVector;

        public FileSender(File file, String algorithmMod, SecretKey sessionKey, byte [] initVector){
            this.file = file;
            this.algorithmMod = algorithmMod;
            this.sessionKey = sessionKey;
            this.initVector = initVector;
        }
        @Override
        public void run() {
            sendFile(file, algorithmMod, sessionKey, initVector);
        }
    }

    public synchronized void sendFile(File file, String algorithmMod, SecretKey sessionKey, byte [] initVector){

        String algorithm = "AES/" + algorithmMod + "/PKCS5PADDING";

        // calculating encryption time
        Instant start = Instant.now();

        File encryptedFileTmp = SymmetricCipher.encryptFile(file, sessionKey, initVector, algorithm, bottom);

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        System.out.println("[" + algorithmMod +"] " + "Encryption time: " + timeElapsed.toMillis() * 0.001 +"s");
        System.out.println("File size: " + file.length() * 0.000001 + "MB");
        System.out.println("-----------------------------------------------------------\n");

        try{
            dOut.writeUTF("A"); // defining protocol
            dOut.writeUTF(file.getName());
            dOut.writeLong(encryptedFileTmp.length());
            dOut.writeUTF(algorithm);

            byte[] buffer = new byte[4096];
            int read = 0;
            int remaining = (int) file.length();

            FileInputStream fis = new FileInputStream(encryptedFileTmp);

            double loopTimes = (double) encryptedFileTmp.length() / buffer.length;
            double step = loopTimes != 0 ? (50 / loopTimes) : 100;
            double progress = bottom.getProgressBar().getValue();

            while ((read = fis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0){
                dOut.write(buffer, 0, read);

                progress += step;
                bottom.getProgressBar().setValue((int)progress);
            }

            bottom.getProgressBar().setValue(0);
            dOut.flush();
            fis.close();

            Files.delete(encryptedFileTmp.toPath());

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
