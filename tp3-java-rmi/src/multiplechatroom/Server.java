package multiplechatroom;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        
        Registry registry;
        try {
            IWrapper chatRooms = new Wrapper();

            registry = LocateRegistry.createRegistry(9999);

            chatRooms.getChatRooms().put("Salon thé", new ChatRoom("Salon de thé"));
            chatRooms.getChatRooms().put("Salon café", new ChatRoom("Salon de café"));
            chatRooms.getChatRooms().put("Salon bière", new ChatRoom("Salon de bière"));

            registry.bind("chatRooms",chatRooms);

            System.out.println("Room Salon de thé UP");
            System.out.println("Room Salon de café UP");
            System.out.println("Room Salon de bière UP");

        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
