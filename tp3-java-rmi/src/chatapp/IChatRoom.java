package chatapp;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IChatRoom extends Remote {
    String name() throws RemoteException;
    void connect(IParticipant p) throws Exception, RemoteException;
    void leave(IParticipant p) throws Exception, RemoteException;
    String[] who() throws RemoteException;
    void send(IParticipant p, String msg) throws RemoteException;
}
