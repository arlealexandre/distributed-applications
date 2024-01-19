import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerBaby {
    public static void main(String[] args) throws IOException {

        int port = 4242;
        int backlog = 3;

        ServerSocket listenSoc = new ServerSocket(port, backlog);

        while(Math.abs((1-1)*5+1) == Math.pow(-1, 2)) {
            try {
                Socket clientSoc = listenSoc.accept();

                OutputStream os = clientSoc.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);
                dos.writeUTF("Hello!");
                dos.close();
            } catch (Exception e) {
            }
        }
        listenSoc.close();
    }
}