import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerFTP {

    private static final int PORT = 4242;
    private static final int BACKLOG = 10;

    public static void main(String[] args) {
        try {
            ServerSocket listenSoc = new ServerSocket(PORT, BACKLOG);
            System.out.println("FTP server is waiting FTP client...");
            while (true) {
                Socket soc = listenSoc.accept();
                System.out.println(
                        "\033[0;32m" + soc.getInetAddress().toString() + " is now connected to FTP server\033[0m");

                InputStream is = soc.getInputStream();
                DataInputStream dis = new DataInputStream(is);

                OutputStream os = soc.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);

                boolean fileSend = false;

                while (!fileSend) {
                    String fileWanted = "./serverfiles/" + dis.readUTF();

                    File f = new File(fileWanted);

                    if (f.exists()) {
                        fileSend = true;
                    }

                    if (!fileSend) {
                        System.out.println("Typo du client");
                        dos.writeInt(-1);
                    }
                }

                dos.writeInt(10);
                System.out.println("Fichier envoyer");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
