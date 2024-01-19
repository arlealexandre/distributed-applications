import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerFTP {

    private static final int PORT = 4320;
    private static final int BACKLOG = 10;
    
    public static void main(String[] args) {

        try {
            ServerSocket listenSoc = new ServerSocket(PORT,BACKLOG);
            System.out.println("FTP server is waiting FTP client...");
            while (true) {
                Socket soc = listenSoc.accept();
                System.out.println(soc.getLocalSocketAddress().toString()+" is now connected to FTP server");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
