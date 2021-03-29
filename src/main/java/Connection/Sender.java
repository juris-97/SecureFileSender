package Connection;


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

    public Sender(Top top, KeyManager keyManager){
        this.top = top;
        this.keyManager = keyManager;
    }

    public void establishConnection(){
        String ip = top.getIpField().getText();
        if(ip.equals("")) return;

        try{
            socket = new Socket(ip, 8181);
            outStream = socket.getOutputStream();
            System.out.println("Connection established..");
            sendPublicKey(keyManager.getPublicKey());

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

            System.out.println("Public Key Sent");

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