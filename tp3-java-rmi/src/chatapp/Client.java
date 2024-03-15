package chatapp;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        System.out.println("----------------------------------------------------------");
        System.out.println("Welcome to ChatApp v1.0");

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your username: ");
        String nameInput = sc.nextLine();

        try {
            IParticipant participant = new Participant(nameInput);

            Registry remoteRegistry = LocateRegistry.getRegistry("localhost", 9999);
            IChatRoom chatRoom = (IChatRoom) remoteRegistry.lookup("chatRoom");
            chatRoom.connect(participant);
            System.out.println("Connected to chat room: " + chatRoom.name());

            String message;
            System.out.println(participant.name() + "> ");

            do {
                message = sc.nextLine();
                chatRoom.send(participant, message);
            } while (message != "leave()");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
