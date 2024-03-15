package chatapp;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ChatRoom extends UnicastRemoteObject implements IChatRoom {

    String name;
    ArrayList<IParticipant> participants;

    protected ChatRoom(String name) throws RemoteException {
        super();
        this.name = name;
    }

    @Override
    public void connect(IParticipant p) throws Exception, RemoteException {
        for (int i = 0; i < this.participants.size(); i++) {
            if (this.participants.get(i) == p) {
                throw new Exception("Already connected!");
            }
        }
        this.participants.add(p);
    }

    @Override
    public void leave(IParticipant p) throws Exception, RemoteException {

        boolean removed = false;
        for (int i = 0; i < this.participants.size(); i++) {
            if (this.participants.get(i) == p) {
                this.participants.remove(i);
                removed = true;
            }
        }
        if (!removed) {
            throw new Exception("Not connected!");
        }
        
    }

    @Override
    public String[] who() throws RemoteException {
        String[] participantsName = new String[this.participants.size()];

        for (int i = 0; i < this.participants.size(); i++) {
            participantsName[i] = this.participants.get(i).name();
        }

        return participantsName;
    }

    @Override
    public void send(IParticipant p, String msg) throws RemoteException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'send'");
    }

    @Override
    public String name() throws RemoteException {
        return this.name;
    }

}
