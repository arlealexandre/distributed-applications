package multiplechatroom;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ChatRoom extends UnicastRemoteObject implements IChatRoom {

    String name;
    ArrayList<IParticipant> participants;
    String[] previousMessages;
    String[] previousMessageSenders;
    IParticipant[] exitedClient;
    int[] lastMessageReadByExitedClient;
    
    int lastMessageSend;

    protected ChatRoom(String name) throws RemoteException {
        super();
        this.name = name;
        this.participants = new ArrayList<>();
        this.previousMessages = new String[0];
        this.previousMessageSenders = new String[0];
        this.lastMessageReadByExitedClient = new int[0];
        this.exitedClient = new IParticipant[0];
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

        for (int i = 0; i < this.exitedClient.length; i++) {
            if (this.exitedClient[i].equals(p)) {
                if (this.lastMessageReadByExitedClient[i] != this.lastMessageSend) {
                    //Envoyer tous les messages du dernier lu par le client jusqu'au dernier envoyer
                    for (int j = (this.previousMessages.length - 1) -this.lastMessageSend - this.lastMessageReadByExitedClient[i]; j < this.previousMessages.length; j++) {
                        p.receive(this.previousMessageSenders[j], this.previousMessages[j]);
                    }

                    if (i == 0) {
                        this.removeSavedMessages(this.lastMessageReadByExitedClient[1]-this.lastMessageReadByExitedClient[0]);
                    }
                    this.removeExited(i);
                }
            }
        }
    }

    public void removeSavedMessages (int n) {
        String[] tempMsg = new String[this.previousMessages.length - n];
        String[] tempSen = new String[this.previousMessageSenders.length - n];

        for (int i = n; i < this.previousMessages.length; i++) {
            tempMsg[i-n] = this.previousMessages[i];
            tempSen[i-n] = this.previousMessageSenders[i];
        }

        this.previousMessageSenders = tempSen;
        this.previousMessages = tempMsg;
    }

    public void removeExited (int client) {
        int[] tempInt = new int[this.lastMessageReadByExitedClient.length-1];
        IParticipant[] tempPart = new IParticipant[this.exitedClient.length-1];

        int offset = 0;
        for (int i = 0; i < this.exitedClient.length; i++) {
            if (i == client) {
                offset = 1;
            } else {
                tempInt[i-offset] = this.lastMessageReadByExitedClient[i];
                tempPart[i-offset] = this.exitedClient[i];
            }
        }
        this.lastMessageReadByExitedClient = tempInt;
        this.exitedClient = tempPart;
    }

    public void updateLeave (IParticipant p) {
        int[] tempInt = new int[this.lastMessageReadByExitedClient.length + 1];
        IParticipant[] tempPart = new IParticipant[this.exitedClient.length + 1];

        for (int i = 0; i < this.exitedClient.length; i++) {
            tempInt[i] = this.lastMessageReadByExitedClient[i];
            tempPart[i] = this.exitedClient[i];
        }
        tempInt[this.lastMessageReadByExitedClient.length] = this.lastMessageSend;
        tempPart[this.exitedClient.length] = p;

        this.lastMessageReadByExitedClient = tempInt;
        this.exitedClient = tempPart;
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
        this.updateLeave(p);
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

    public void updateSend (String msg, String client) {
        String[] tempMsg = new String[this.previousMessages.length+1];
        String[] tempSen = new String[this.previousMessageSenders.length+1];
        for (int i = 0; i < this.previousMessageSenders.length; i++) {
            tempMsg[i] = this.previousMessages[i];
            tempSen[i] = this.previousMessageSenders[i];
        }

        tempMsg[this.previousMessages.length] = msg;
        tempSen[this.previousMessageSenders.length] = client;

        this.previousMessageSenders = tempSen;
        this.previousMessages = tempMsg;
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
        updateSend(msg, p.name());
    }

    @Override
    public String name() throws RemoteException {
        return this.name;
    }

}
