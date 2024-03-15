package babystep;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class BabystepClient {
    
    public static void main(String[] args) {
        System.out.println("Babystep client running...");
        Registry registry;
        IPrinter printer;
        try {
            registry = LocateRegistry.getRegistry("localhost",9999);
            printer = (IPrinter) registry.lookup("printer");
            printer.print("Hello world!");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }
}
