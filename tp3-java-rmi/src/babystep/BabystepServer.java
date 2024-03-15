package babystep;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class BabystepServer {
    
    public static void main(String[] args) {
        System.out.println("Babystep server running...");
        IPrinter printer;
        Registry registry;
        try {
            printer = new Printer();
            registry = LocateRegistry.createRegistry(9999);
            registry.bind("printer",printer);
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
