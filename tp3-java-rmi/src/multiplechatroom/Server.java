package multiplechatroom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    private static final String SAVE_FILE_NAME = "saveChatRooms";
    private static IWrapper chatRooms;

    public static void main(String[] args) {

        try {
            chatRooms = loadChatRooms();
            ChatRoom c = (ChatRoom) chatRooms.getChatRooms().get("Salon thé");
            c.participants.toString();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading chat rooms: " + e.getMessage());
            try {
                chatRooms = new Wrapper();
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
        }

        try {
            Registry registry = LocateRegistry.createRegistry(9999);
            registry.bind("chatRooms", chatRooms);
            System.out.println("Server is running...");
        } catch (RemoteException | AlreadyBoundException e) {
            System.out.println("Error starting server: " + e.getMessage());
        }
    }

    private static IWrapper loadChatRooms() throws IOException, ClassNotFoundException {
        File saveFile = new File(SAVE_FILE_NAME);

        if (saveFile.exists()) {
            try (FileInputStream fis = new FileInputStream(saveFile);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                return (IWrapper) ois.readObject();
            }
        } else {
            return initializeChatRooms();
        }
    }

    private static IWrapper initializeChatRooms() {
        try {
            chatRooms = new Wrapper();
            chatRooms.getChatRooms().put("Salon thé", new ChatRoom("Salon de thé"));
            chatRooms.getChatRooms().put("Salon café", new ChatRoom("Salon de café"));
            chatRooms.getChatRooms().put("Salon bière", new ChatRoom("Salon de bière"));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        try (FileOutputStream fos = new FileOutputStream(SAVE_FILE_NAME);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(chatRooms);
        } catch (IOException e) {
            System.out.println("Error saving chat rooms: " + e.getMessage());
        }

        return chatRooms;
    }
}
