package multiplechatroom;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Participant extends UnicastRemoteObject implements IParticipant, Serializable {

    private String name;

    protected Participant(String name) throws RemoteException {
        super();
        this.name = name;
    }

    @Override
    public String name() throws RemoteException {
        return this.name;
    }

    @Override
    public void receive(String name, String msg) throws RemoteException {
        System.out.println(name+"> "+msg);
    }
    
}
