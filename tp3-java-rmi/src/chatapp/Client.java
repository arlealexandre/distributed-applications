package chatapp;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        System.out.println("Welcome to ChatApp v1.0");

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your username: ");
        String nameInput = sc.nextLine();
        sc.close();
        
        try {
            IParticipant participant = new Participant(nameInput);
            Registry registry = LocateRegistry.createRegistry(9999);
            registry.bind("participant", participant);

        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
