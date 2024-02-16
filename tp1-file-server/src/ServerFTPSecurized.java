import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerFTPSecurized {

    private static final int PORT = 4242;
    private static final int BACKLOG = 10;
    private static final int CHUNK_SIZE = 512;

    public static void main(String[] args) {
        try {

            boolean transmittingFile = false;
            int currentTransmissionBlock = 0;
            int transmissionSize = 0;

            ServerSocket listenSoc = new ServerSocket(PORT, BACKLOG);
            /*Close the socket on shutdown */
            Runtime.getRuntime().addShutdownHook(new Thread(){public void run(){
                try { listenSoc.close(); } catch (IOException e) {}}});

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
                File f = null;
                String fileWanted = "";

                while (!fileSend) {
                    fileWanted = "./serverfiles/" + dis.readUTF();

                    f = new File(fileWanted);

                    if (f.exists()) {
                        fileSend = true;
                    }

                    if (!fileSend) {
                        System.out.println("Typo du client");
                        dos.writeInt(-1);
                    }
                }

                FileInputStream fis = new FileInputStream(fileWanted);
                byte[] content = fis.readAllBytes();

                /*Ecriture de la longeur en octet du fichier */
                dos.writeInt(content.length);

                int nbChunk = (content.length - content.length % CHUNK_SIZE)/CHUNK_SIZE;
                transmissionSize = nbChunk + (content.length % CHUNK_SIZE == 0 ? 0 : 1);

                transmittingFile = true;
                for (int i = 0; i < nbChunk; i++) {

                    /* Simulate a crash */
                    if (i == 3) {
                        os.close();
                    }

                    System.out.println(i);

                    dos.write(content, CHUNK_SIZE*i, CHUNK_SIZE);
                    currentTransmissionBlock++;
                }

                dos.write(content, CHUNK_SIZE * nbChunk, content.length % CHUNK_SIZE);
                transmittingFile = false;
                currentTransmissionBlock = 0;
                transmissionSize = 0;

                System.out.println("Fichier envoyer");
                
                fis.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
