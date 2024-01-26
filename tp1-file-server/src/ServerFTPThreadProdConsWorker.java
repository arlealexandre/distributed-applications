import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import ProdCons.ProdConsBuffer;

public class ServerFTPThreadProdConsWorker extends Thread {

    private DataInputStream dis;
    private InputStream is;
    private OutputStream os;
    private DataOutputStream dos;
    private ProdConsBuffer clientsBuffer;

    public ServerFTPThreadProdConsWorker(ProdConsBuffer clientsBuffer) throws Exception {
        this.clientsBuffer = clientsBuffer;
    }

    public void run() {
        try {
            while (true) {
                Socket soc = clientsBuffer.get().getMessageObject();
                this.is = soc.getInputStream();
                this.dis = new DataInputStream(is);
                this.os = soc.getOutputStream();
                this.dos = new DataOutputStream(os);

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

                /* Ecriture de la longeur en octet du fichier */
                dos.writeInt(content.length);

                /* Ecriture du fichier sur la socket */
                dos.write(content);

                System.out.println("File sended successfully");

                fis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
