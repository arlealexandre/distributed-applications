package multiplechatroom;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        
        Registry registry;
        try {
            Wrapper chatRooms = new Wrapper();

            registry = LocateRegistry.createRegistry(9999);

            chatRooms.chatRooms.put("Salon thé", new ChatRoom("Salon thé"));
            chatRooms.chatRooms.put("Salon café", new ChatRoom("Salon café"));
            chatRooms.chatRooms.put("Salon bière", new ChatRoom("Salon bière"));

            registry.bind("chatRooms",chatRooms);
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
