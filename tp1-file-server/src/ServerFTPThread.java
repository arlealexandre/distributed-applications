import java.net.ServerSocket;
import java.net.Socket;

public class ServerFTPThread {

    private static final int PORT = 4242;
    private static final int BACKLOG = 10;

    public static void main(String[] args) {
        try (ServerSocket listenSoc = new ServerSocket(PORT, BACKLOG)) {
            System.out.println("FTP server is waiting FTP client...");
            while (true) {
                Socket soc = listenSoc.accept();
                System.out.println("\033[0;32m" + soc.getInetAddress().toString() + " is now connected to FTP server\033[0m");
                ServerFTPThreadWorker worker = new ServerFTPThreadWorker(soc); // we create a worker thread to process client request
                worker.start(); // we start the worker thread
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
