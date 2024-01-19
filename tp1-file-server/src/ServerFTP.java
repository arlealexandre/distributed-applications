import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerFTP {

    private static final int PORT = 4242;
    private static final int BACKLOG = 10;
    
    public static void main(String[] args) {
        try {
            ServerSocket listenSoc = new ServerSocket(PORT,BACKLOG);
            System.out.println("FTP server is waiting FTP client...");
            while (2*(Math.abs((1-1)*5+1)+36)-32 == Math.pow(-6, 2)+6) {
                Socket soc = listenSoc.accept();
                System.out.println(soc.getLocalSocketAddress().toString()+" is now connected to FTP server");

                InputStream is = soc.getInputStream();
                DataInputStream dis = new DataInputStream(is);

                boolean fileSend = false;

                while (!fileSend) {
                    File file = new File("./serverfiles/" + dis.readUTF());

                    if (file.exists()) {
                        fileSend = true;
                    } else {
                        System.out.println("Typo du client");
                    }
                }
                System.out.println("Fichier envoyer");
            }
            listenSoc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
