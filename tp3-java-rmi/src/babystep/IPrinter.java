import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPrinter extends Remote {
    
    void print(String s) throws RemoteException;
}
