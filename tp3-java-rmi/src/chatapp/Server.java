package chatapp;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        IChatRoom chatRoom;
        Registry registry;
        try {
            chatRoom = new ChatRoom("Salon thé");
            registry = LocateRegistry.createRegistry(9999);
            registry.bind("chatRoom",chatRoom);
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
