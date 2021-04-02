package Connection;


import GUI.Center.Right;

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

    Socket socket;
    Right right;

    public Receiver(Right right){
        this.right = right;
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


    public synchronized void receivePublicKey(){
        /*PublicKey receivedPubKey = null;
        byte [] bytes = new byte[2048];

        try{
            dataInputStream.read(bytes);
            receivedPubKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(bytes));
            manager.setPartnerPublicKey(receivedPubKey);
            System.out.println("Public Key received");
            manager.saveKeyOnDisk(receivedPubKey, "receivedPubKey.pub");

        }catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException e){
            e.printStackTrace();
        }*/
    }

    public synchronized void receive(){

        while(true){
            try{

                String fileName = dataInputStream.readUTF();
                long fileSize = dataInputStream.readLong();
                byte [] buffer = new byte[4098];

                File file = new File(fileName);
                FileOutputStream out = new FileOutputStream(file);

                int read = 0;
                int totalRead = 0;
                int remaining = (int)fileSize;

                while((read = dataInputStream.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
                    totalRead += read;
                    remaining -= read;
                    System.out.println("read " + totalRead + " bytes.");
                    out.write(buffer, 0, read);
                }

                out.flush();
                out.close();
                buffer = null;

                System.out.println("File received..");
                right.getModel().addElement(file);

            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }
}
