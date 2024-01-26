import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientFTP {

    private static final int SERVER_PORT = 4242;
    private static final String SERVER_HOST = "localhost";

    public static void main(String[] args) {

        try {
            Socket soc = new Socket(SERVER_HOST, SERVER_PORT); // Creation of client socket

            System.out.println("\033[0;32mYou're now connected to "+SERVER_HOST+":"+SERVER_PORT+" FTP server\033[0m");

            /* Get the filename from user input */
            System.out.println("Enter a filename: ");
            Scanner sc = new Scanner(System.in);
            String filename = sc.nextLine();

            /* Writing the filename into the socket stream to server */
            OutputStream os = soc.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);

            InputStream is = soc.getInputStream();
            DataInputStream dis = new DataInputStream(is);

            boolean requestOk = false;
            int nbLineFile = 0;

            while(!requestOk) {
                dos.writeUTF(filename);
                nbLineFile = dis.readInt();
                
                if (nbLineFile != -1) {
                    requestOk = true;
                } else {
                    System.out.println("Ce fichier n'existe pas.");
                    sc = new Scanner(System.in);
                    filename = sc.nextLine();
                }
            }

            
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}