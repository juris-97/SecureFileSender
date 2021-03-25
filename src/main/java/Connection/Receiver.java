package Connection;


import GUI.Center.Right;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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

            receive();
        }catch (IOException e){
            e.getStackTrace();
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
