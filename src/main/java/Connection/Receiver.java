package Connection;

import Connection.PubKeyExchange.Exchange;
import GUI.Sides.Center.Right;
import Security.AsymmetricCipher;
import Security.KeyHandler;
import Security.SymmetricCipher;

import javax.crypto.SecretKey;
import javax.swing.*;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver implements Runnable{

    private ServerSocket serverSocket;
    private KeyHandler keyHandler;
    private Right right;

    public Receiver(Right right, KeyHandler keyHandler){
        try{
            this.right = right;
            this.keyHandler = new KeyHandler();
            serverSocket = new ServerSocket(8181);
            new Thread(this).start();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Server awaiting connection...");
        try{
            while(!serverSocket.isClosed())
                new ClientHandler(serverSocket.accept()).start();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void closeClientConnection(Socket client, DataInputStream dis, DataOutputStream dOut){
        try{
            System.out.println("Client from: " + client + " disconnected from server.");
            client.close();
            dis.close();
            dOut.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private class ClientHandler extends Thread{
        private final Socket clientSocket;
        private DataInputStream dis;
        private DataOutputStream dOut;

        public ClientHandler(Socket socket){
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try{
                System.out.println("Client connected " + clientSocket);
                dis = new DataInputStream(clientSocket.getInputStream());
                dOut = new DataOutputStream(clientSocket.getOutputStream());

                publicKeyExchange();

                while(!clientSocket.isClosed())
                    receiveOption();

            }catch ( IOException e){
                closeClientConnection(clientSocket, dis, dOut);
            }
        }

        // received file size is long, but after it is downcast to int
        // so if the file is more than 2GB will be the exception.
        public void receiveData(){

            try{
                String receivedFileName  = dis.readUTF();
                long receivedEncryptedFileSize = dis.readLong();
                String receivedAlgorithmMethod = dis.readUTF();

                byte [] buffer = new byte[4098];

                // contains encrypted file
                ByteArrayOutputStream byteOut = new ByteArrayOutputStream((int)receivedEncryptedFileSize);

                int read = 0;
                int totalRead = 0;
                int remaining = (int)receivedEncryptedFileSize;

                while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
                    totalRead += read;
                    remaining -= read;
                    System.out.println("read " + totalRead + " bytes.");
                    byteOut.write(buffer, 0, read);
                }

                File decryptedFile = SymmetricCipher.decryptFile(
                        byteOut.toByteArray(),
                        keyHandler.getReceivedSessionKey(),
                        keyHandler.getReceivedInitVector(),
                        receivedFileName,
                        receivedAlgorithmMethod);

                right.addFileToPanel(decryptedFile);

                byteOut.close();

            }catch (IOException e){
                e.printStackTrace();
            }

            keyHandler.setReceivedInitVector(null);
            keyHandler.setReceivedSessionKey(null);
        }

        public void publicKeyExchange(){
            Exchange.receivePublicKey(dis, keyHandler);
            Exchange.sendPublicKey(keyHandler.getPublicKey(), dOut);

            //System.out.println("[Rece in SIDE = 2] Public Key: " + DatatypeConverter.printHexBinary(keyHandler.getReceivedPublicKey().getEncoded()));
            //System.out.println("[Send in SIDE = 2] Public Key: " + DatatypeConverter.printHexBinary(keyHandler.getPublicKey().getEncoded()));
        }

        public void receivedInitVector(){
            try{
                byte [] receivedInitVector = new byte [16];
                dis.read(receivedInitVector);
                keyHandler.setReceivedInitVector(receivedInitVector);

            }catch (IOException e){
                e.printStackTrace();
            }
        }

        public void receiveSessionKey(){
            try{
                int sessionKeyLength = dis.readInt();
                byte [] wrappedSessionKey = new byte[sessionKeyLength];
                dis.read(wrappedSessionKey);

                // unwrap received session key
                SecretKey unwrappedSessionKey = AsymmetricCipher.unwrapSessionKeyWithPrivateKey(wrappedSessionKey, keyHandler.getPrivateKey());
                keyHandler.setReceivedSessionKey(unwrappedSessionKey);

                JOptionPane.showMessageDialog(null, "Session Key received", "InfoBox: Success", JOptionPane.INFORMATION_MESSAGE);

            }catch (IOException e){
                closeClientConnection(clientSocket, dis, dOut);
            }
        }

        public void receiveOption(){
            try{
                String protocol = dis.readUTF();
                switch (protocol) {
                    case "A":
                        receiveData();
                        break;
                    case "I":
                        receivedInitVector();
                        break;
                    case "C":
                        receiveSessionKey();
                }
            }catch (IOException e){
                closeClientConnection(clientSocket, dis, dOut);
            }
        }
    }
}
