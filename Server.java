
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public void go(){
        try{
            ServerSocket serverSocket = new ServerSocket(4242);

            while (true){
                Socket sock = serverSocket.accept();

                PrintWriter writer = new PrintWriter(sock.getOutputStream());
                String test = "testing from receiver";
                writer.println(test);
                writer.close();
                System.out.println(test);
            }
        }catch (IOException e){
            e.getStackTrace();
        }
    }

    public static void main(String [] args){
        Server server = new Server();
        server.go();
    }

}

