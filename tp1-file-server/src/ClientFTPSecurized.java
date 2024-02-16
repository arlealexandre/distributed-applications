import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientFTPSecurized {

    private static final int CHUNK_SIZE = 512;
    private static final int SERVER_PORT = 4242;
    private static final String SERVER_HOST = "localhost";

    public static void main(String[] args) {

        try {

            boolean transmittingFile = false;
            int currentTransmissionBlock = 0;
            int transmissionSize = 0;

            Socket soc = new Socket(SERVER_HOST, SERVER_PORT); // Creation of client socket

            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("\033[0;32mYou're now connected to "+SERVER_HOST+":"+SERVER_PORT+" FTP server\033[0m");

            Scanner sc = new Scanner(System.in);;
            String filename = "";

            /* Writing the filename into the socket stream to server */
            OutputStream os = soc.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);

            InputStream is = soc.getInputStream();
            DataInputStream dis = new DataInputStream(is);

            boolean requestOk = false;
            int nbByteFile = 0;

            while(!requestOk) {
                System.out.println("Enter a filename: ");
                filename = sc.nextLine();
                
                /*Envoie du nom du fichier souhaiter au serveur*/
                dos.writeUTF(filename);

                /*Reception du nombre de byte du fichier souhaiter (-1 = fichier inexistant) */
                nbByteFile = dis.readInt();
                
                if (nbByteFile != -1) {
                    requestOk = true;
                    sc.close();
                } else {
                    System.out.println("Message from server: the file named '"+filename+"' does not exists.");
                    System.out.println("------------");
                }

                
            }

            if (requestOk) {
                File tempFile = new File("./localfiles/"+filename);
                byte[] fileBytes = null;

                int nbChunk = (nbByteFile - nbByteFile % CHUNK_SIZE)/CHUNK_SIZE;
                transmissionSize = nbChunk + (nbByteFile % CHUNK_SIZE == 0 ? 0 : 1);

                FileOutputStream fo = new FileOutputStream(tempFile);

                transmittingFile = true;
                for (int i = 0; i < nbChunk; i++) {
                    try {
                        fileBytes = dis.readNBytes(CHUNK_SIZE);
                    } catch (IOException e) {
                        System.out.println(i);
                    }
                    currentTransmissionBlock++;
                    fo.write(fileBytes);
                }

                fileBytes = dis.readNBytes(nbByteFile % CHUNK_SIZE);
                fo.write(fileBytes);
                transmittingFile = false;
                currentTransmissionBlock = 0;
                transmissionSize = 0;
                
                
                if (tempFile.exists()) {
                    System.out.println(tempFile.getName()+" has been created");
                } else {
                    System.err.println("Error while creating " + tempFile.getName());
                }

                fo.close();
                soc.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}