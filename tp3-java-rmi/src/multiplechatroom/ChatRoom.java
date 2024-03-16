package multiplechatroom;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ChatRoom extends UnicastRemoteObject implements IChatRoom, Serializable {

    String name;
    public ArrayList<IParticipant> participants;
    ArrayList<String> previousMessages;
    ArrayList<String> previousMessageSenders;
    ArrayList<String> exitedClient;
    ArrayList<Integer> lastMessageReadByExitedClient;
    
    int lastMessageSend;

    protected ChatRoom(String name) throws RemoteException {
        super();
        this.name = name;
        this.participants = new ArrayList<>();
        this.previousMessages = new ArrayList<>();
        this.previousMessageSenders = new ArrayList<>();
        this.lastMessageReadByExitedClient = new ArrayList<>();
        this.exitedClient = new ArrayList<>();
        this.lastMessageSend = 0;
    }

    @Override
    public void connect(IParticipant p) throws Exception, RemoteException {
        for (IParticipant par : this.participants) {
            if (par.equals(p)) {
                throw new Exception("Already connected!");
            }
        }
        this.participants.add(p);

        for (int i = 0; i < this.exitedClient.size(); i++) {
            if (this.exitedClient.get(i).equals(p.name())) {
                if (this.lastMessageReadByExitedClient.get(i) != this.lastMessageSend) {
                    for (int j = this.lastMessageReadByExitedClient.get(i) - this.lastMessageReadByExitedClient.get(0); j < this.previousMessages.size(); j++) {
                        p.receive(this.previousMessageSenders.get(j), this.previousMessages.get(j));
                    }
                    if (i == 0) {
                        if (this.exitedClient.size() == 1) {
                            this.previousMessages = new ArrayList<>();
                            this.previousMessageSenders = new ArrayList<>();
                        } else {
                            this.removeSavedMessages(this.lastMessageReadByExitedClient.get(1)-this.lastMessageReadByExitedClient.get(0));
                        }
                    }
                    this.lastMessageReadByExitedClient.remove(i);
                    this.exitedClient.remove(i);
                }
            }
        }
    }

    public void removeSavedMessages (int n) {
        for (int i = 0; i < n; i++) {
            this.previousMessages.remove(0);
            this.previousMessageSenders.remove(0);
        }
    }

    @Override
    public void leave(IParticipant p) throws Exception, RemoteException {
        boolean removed = false;
        for (int i = 0; i < this.participants.size(); i++) {
            if (this.participants.get(i).equals(p)) {
                this.participants.remove(i);
                removed = true;
            }
        }
        if (!removed) {
            throw new Exception("Not connected!");
        }
        this.lastMessageReadByExitedClient.add(this.lastMessageSend);
        this.exitedClient.add(p.name());
    }

    @Override
    public String[] who() throws RemoteException {
        String[] participantsName = new String[this.participants.size()];
        int index = 0;

        for (IParticipant par : this.participants) {
            participantsName[index++] = par.name();
        }

        return participantsName;
    }

    @Override
    public void send(IParticipant p, String msg) throws RemoteException {
        for (IParticipant par : this.participants) {
            if (par.equals(p)) {
                for (IParticipant pa : this.participants) {
                    pa.receive(p.name(), msg);
                }
            }
        }
        this.lastMessageSend++;

        // We save the message if at least one person leaved
        if (this.exitedClient.size() > 0) {
            this.previousMessageSenders.add(p.name());
            this.previousMessages.add(msg);
        }
    }

    @Override
    public String name() throws RemoteException {
        return this.name;
    }

}
