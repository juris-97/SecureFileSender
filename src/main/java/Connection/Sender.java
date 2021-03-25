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
        dOut = new DataOutputStream(outStream);

        try{
            dOut.writeUTF("Hello");
            dOut.flush(); // send the message

        }catch (IOException e){
            e.getStackTrace();
        }
    }
}