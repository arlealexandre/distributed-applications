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
        // Check if client is not already connected
        for (IParticipant par : this.participants) {
            if (par.equals(p)) {
                throw new Exception("Already connected!");
            }
        }

        this.participants.add(p);

        //Send all the messages missed if the client had already connected to this room
        for (int i = 0; i < this.exitedClient.size(); i++) {
            if (this.exitedClient.get(i).equals(p.name())) { // If he indeed leaved
                if (this.lastMessageReadByExitedClient.get(i) != this.lastMessageSend) { // If he missed at least one message
                    for (int j = this.lastMessageReadByExitedClient.get(i) - this.lastMessageReadByExitedClient.get(0); j < this.previousMessages.size(); j++) {
                        p.receive(this.previousMessageSenders.get(j), this.previousMessages.get(j));
                    }
                    if (i == 0) { // If it was the oldest person to leave
                        if (this.exitedClient.size() == 1) { // And the only one
                            this.previousMessages = new ArrayList<>();
                            this.previousMessageSenders = new ArrayList<>();
                        } else { // Not the only one
                            //We can update the saved message to optimize memory usage
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
    public void send(IParticipant p, String msg) throws Exception {
        boolean senderPresent = false;
        for (IParticipant par : this.participants) {
            if (par.equals(p)) {
                senderPresent = true;
            }
        }

        if (!senderPresent) {
            throw new Exception("Not connected!");
        }

        ArrayList<IParticipant> deadParticipant = new ArrayList<>();
        for (IParticipant pa : this.participants) {
            try {
                pa.receive(p.name(), msg);
            } catch (Exception e) {
                deadParticipant.add(pa);
            }
        }
        for (IParticipant pa : deadParticipant) {
            this.participants.remove(pa);
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
