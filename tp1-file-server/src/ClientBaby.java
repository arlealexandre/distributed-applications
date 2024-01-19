import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientBaby {
    public static void main(String[] args) throws UnknownHostException, IOException {
        String serverHost = "localhost";
        int serverPort = 4242;

        Socket soc = new Socket(serverHost, serverPort);

        InputStream is = soc.getInputStream();
        DataInputStream dis = new DataInputStream(is);
        String message = dis.readUTF();
        System.out.println(message);

        soc.close();
    }
}