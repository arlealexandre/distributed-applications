import java.net.ServerSocket;
import java.net.Socket;

import ProdCons.Message;
import ProdCons.ProdConsBuffer;
import ProdCons.ProdConsBufferSemaphore;

public class ServerFTPThreadProdCons {

    private static final int PORT = 4242;
    private static final int BACKLOG = 10;
    private static final int NB_THREADS = 5;
    private static final int BUFFER_SIZE = 10;

    public static void main(String[] args) {
        try (ServerSocket listenSoc = new ServerSocket(PORT, BACKLOG)) {

            System.out.println("FTP server is waiting FTP client...");

            ProdConsBuffer clientsBuffer = new ProdConsBufferSemaphore(BUFFER_SIZE);
            Socket soc;

            for (int i=0; i<NB_THREADS; i++) {
                ServerFTPThreadProdConsWorker worker = new ServerFTPThreadProdConsWorker(clientsBuffer);
                worker.start();
            }

            while (true) {
                soc = listenSoc.accept();
                clientsBuffer.put(new Message(soc));
                System.out.println("\033[0;32m" + soc.getInetAddress().toString() + " is now connected to FTP server\033[0m");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
