package Connection;


import GUI.Bottom;
import GUI.Top;
import Keys.KeyManager;

import java.io.*;
import java.net.Socket;
import java.security.PublicKey;


public class Sender {
    Socket socket;
    OutputStream outStream;
    DataOutputStream dOut;
    KeyManager keyManager;

    Top top;
    Bottom bottom;

    public Sender(Top top, Bottom bottom, KeyManager keyManager){
        this.top = top;
        this.bottom = bottom;
        this.keyManager = keyManager;
    }

    public void establishConnection(){
        String ip = top.getIpField().getText();
        if(ip.equals("")) return;

        try{
            socket = new Socket(ip, 8181);
            outStream = socket.getOutputStream();
            dOut = new DataOutputStream(outStream);
            System.out.println("Connection established..");
            //sendPublicKey(keyManager.getPublicKey());

        }catch (Exception e1){
            e1.printStackTrace();
        }
    }


    public void sendPublicKey(PublicKey pubKey){

        byte [] bytes = pubKey.getEncoded();

        try(BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(bytes))){
            dOut = new DataOutputStream(outStream);
            bis.read(bytes, 0, bytes.length);
            dOut.write(bytes, 0, bytes.length);
            dOut.flush();
            System.out.println("Public Key Sent");

        }catch (IOException e){
            e.getStackTrace();
        }
    }

    public void sendFile(){
        File file = top.getChosenFile();

        if(file == null) return;

        try(FileInputStream fis = new FileInputStream(file)){

            dOut.writeUTF(file.getName());
            dOut.writeLong(file.length());

            byte [] buffer = new byte[4096];

            int read = 0;
            int remaining = (int)file.length();

            while((read = fis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0)
                dOut.write(buffer, 0, read);

            System.out.println("File sent!");
            dOut.flush();

        }catch (IOException e1){
            System.out.println(e1.getMessage());
        }
    }
}