package multiplechatroom;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface IWrapper extends Remote {
    
    HashMap<String, IChatRoom> getChatRooms() throws RemoteException;
}
