package Connection;


import GUI.Center.Right;
import Keys.AsymmetricCypher;
import Keys.KeyHandler;
import Keys.SymmetricCypher;

import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;


public class Receiver implements Runnable{

    private DataInputStream dataInputStream;
    private InputStream inputStream;

    private ServerSocket serverSocket;
    private Socket clientSocket;

    private KeyHandler keyHandler;
    private Socket socket;
    private Right right;


    public Receiver(Right right, KeyHandler keyHandler){
        this.right = right;
        this.keyHandler = keyHandler;
        new Thread(this).start();
    }

    public void startReceiving(){
        new Thread(new DataReceiver()).start();
    }

    @Override
    public void run() {
        try{
            serverSocket = new ServerSocket(8181);
            System.out.println("ServerSocket awaiting connection...");

            clientSocket = serverSocket.accept();
            System.out.println("Connection from " + clientSocket + "!");

            inputStream = clientSocket.getInputStream();
            dataInputStream = new DataInputStream(inputStream);
            startReceiving();

        }catch (IOException e){
            e.getStackTrace();
        }
    }

    public class DataReceiver implements Runnable{
        @Override
        public void run() {

            while (true) {
                try {
                    String protocol = dataInputStream.readUTF();

                    switch (protocol) {
                        case "A":
                            receiveData();
                            break;

                        case "B":
                            receivePublicKey();
                            break;

                        case "C":
                            receiveSessionKey();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void receiveData(){
            try{
                if(keyHandler.getReceivedWrappedSessionKey() == null){
                    System.out.println("No session key received");
                    return;
                }

                String filename = dataInputStream.readUTF();
                long fileSize = dataInputStream.readLong();
                byte [] buffer = new byte[4098];

                // contains encrypted file
                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

                int read = 0;
                int totalRead = 0;
                int remaining = (int)fileSize;

                while((read = dataInputStream.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
                    totalRead += read;
                    remaining -= read;
                    System.out.println("read " + totalRead + " bytes.");
                    byteOut.write(buffer, 0, read);
                }

                byte [] bytes = byteOut.toByteArray();

                SecretKey unwrappedSessionKey = AsymmetricCypher.unwrapSessionKeyWithPrivateKey(
                        keyHandler.getReceivedWrappedSessionKey(), keyHandler.getPrivateKey());

                byte [] decryptedFile = SymmetricCypher.decryptFile(bytes, unwrappedSessionKey, keyHandler.getReceivedInitVector());

                File receivedDecryptedFile = new File("received_"+filename);
                try (FileOutputStream stream = new FileOutputStream(receivedDecryptedFile)) {
                    stream.write(decryptedFile);
                }

                right.getModel().addElement(receivedDecryptedFile);

            }catch (IOException e){
                //e.printStackTrace();
                System.out.printf("Waiting for receiving Data..");
            }
        }

        public void receivePublicKey(){
            PublicKey receivedPubKey = null;
            byte [] bytes = new byte[2048];

            try{
                dataInputStream.read(bytes);
                receivedPubKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(bytes));
                keyHandler.setOtherPublicKey(receivedPubKey);

                System.out.println("Public Key received");

            }catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException e){
                //e.printStackTrace();
                System.out.println("Waiting for receiving public key..");
            }
        }

        public void receiveSessionKey(){
            try{

                int initVectorLength = dataInputStream.readInt();
                int wrappedSessionKeyLength = dataInputStream.readInt();

                byte [] initVector = new byte[initVectorLength];
                byte [] wrappedSessionKey = new byte[wrappedSessionKeyLength];

                dataInputStream.read(initVector);
                dataInputStream.read(wrappedSessionKey);

                keyHandler.setReceivedInitVector(initVector);
                keyHandler.setReceivedSessionKey(wrappedSessionKey);

                System.out.println("Wrapped Session Key received");

            }catch (IOException e){
                //e.printStackTrace();
                System.out.printf("Waiting for receiving Session key..");
            }
        }

    }


    public void disconnect(){
        try{
            inputStream.close();
            dataInputStream.close();
            socket.close();
            serverSocket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
