import java.io.IOException;
import java.net.Socket;

public class ClientFTP {

    private static final int SERVER_PORT = 4320;
    private static final String SERVER_HOST = "localhost";

    public static void main(String[] args) {

        try {
            Socket soc = new Socket(SERVER_HOST, SERVER_PORT);
            System.out.println("You're now connected to "+SERVER_HOST+":"+SERVER_PORT+" FTP server");
            
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}