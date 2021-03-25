package Connection;


import GUI.Top;

import java.io.*;
import java.net.Socket;


public class Sender {
    Socket socket;
    OutputStream outStream;
    DataOutputStream dOut;
    Top top;

    public Sender(Top top){
        this.top = top;
    }

    public void establishConnection(){
        String ip = top.getIpField().getText();
        if(ip.equals("")) return;

        try{
            socket = new Socket(ip, 8181);
            outStream = socket.getOutputStream();
            System.out.println("Connection established..");

        }catch (Exception e1){
            e1.printStackTrace();
        }
    }


    public void send(){

        try{
            dOut.writeUTF("Hello");
            dOut.flush(); // send the message

        }catch (IOException e){
            e.getStackTrace();
        }
    }

    public void sendFile(){
        File file = top.getChosenFile();

        if(file == null) return;

        byte [] fileBytes = new byte[(int) file.length()];

        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))){

            dOut = new DataOutputStream(outStream);
            bis.read(fileBytes, 0, fileBytes.length);

            dOut.writeUTF(file.getName());
            dOut.writeLong(file.length());
            dOut.write(fileBytes, 0, fileBytes.length);
            System.out.println("File sent!");

            dOut.flush();
            fileBytes = null;

        }catch (IOException e){
            e.getStackTrace();
        }

    }
}