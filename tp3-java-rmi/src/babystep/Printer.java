import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Printer extends UnicastRemoteObject implements IPrinter {

  

    protected Printer() throws RemoteException {
        super();
    }

    @Override
    public void print(String s) throws RemoteException {
        System.out.println("Printed value: "+s);
    }
    
}
