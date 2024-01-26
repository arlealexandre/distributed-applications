package ProdCons;

import java.net.Socket;

public class Message {

    private Socket message;

    public Message(Socket s) {
        this.message = s;
    }

    public Socket getMessageObject() {
        return message;
    }
}
