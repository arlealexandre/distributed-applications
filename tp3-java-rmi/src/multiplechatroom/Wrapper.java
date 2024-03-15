package multiplechatroom;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class Wrapper extends UnicastRemoteObject implements IWrapper {

    public HashMap<String, IChatRoom> chatRooms;

    protected Wrapper() throws RemoteException {
        super();
        this.chatRooms = new HashMap<>();
    }

    @Override
    public HashMap<String, IChatRoom> getChatRooms() {
        return chatRooms;
    }
}
