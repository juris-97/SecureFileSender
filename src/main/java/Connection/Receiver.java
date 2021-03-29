package Connection;


import GUI.Center.Right;
import Keys.KeyManager;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class Receiver implements Runnable{

    ServerSocket serverSocket;
    InputStream inputStream;
    DataInputStream dataInputStream;
    KeyManager manager;
    Socket socket;
    Right right;

    public Receiver(Right right, KeyManager manager){
        this.right = right;
        this.manager = manager;
    }

    @Override
    public void run() {
        try{
            serverSocket = new ServerSocket(8181);
            System.out.println("ServerSocket awaiting connection...");
            socket = serverSocket.accept();
            System.out.println("Connection from " + socket + "!");
            inputStream = socket.getInputStream();
            dataInputStream = new DataInputStream(inputStream);

            receivePublicKey();
            receive();
        }catch (IOException e){
            e.getStackTrace();
        }

    }

    public void receivePublicKey(){
        PublicKey receivedPubKey = null;
        byte [] bytes = new byte[2048];

        try{
            dataInputStream.read(bytes);
            receivedPubKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(bytes));
            manager.setPartnerPublicKey(receivedPubKey);
            System.out.println("Public Key received");
            manager.savePublicKeyOnDisk(receivedPubKey, "receivedPubKey.pub");

        }catch (IOException
                | InvalidKeySpecException
                | NoSuchAlgorithmException e1){
            e1.getStackTrace();
        }
    }

    public void receive(){

        while(true){
            try{

                String fileName = dataInputStream.readUTF();
                long fileSize = dataInputStream.readLong();
                byte [] bytes = new byte[(int) fileSize];
                dataInputStream.read(bytes);

                File file = new File(fileName);
                OutputStream fileStream = new FileOutputStream(file);
                fileStream.write(bytes);

                System.out.println("File received..");
                right.getModel().addElement(file);

                fileStream.flush();
                fileStream.close();

            }catch (IOException e){
                e.getStackTrace();
            }
        }
    }
}
