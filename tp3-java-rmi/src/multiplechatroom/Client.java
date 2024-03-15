package multiplechatroom;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

        welcome();

        Scanner sc = new Scanner(System.in);
        String nameInput = getUsername(sc);

        try {
            IParticipant participant = new Participant(nameInput);

            Registry registry = LocateRegistry.getRegistry("localhost", 9999);
            IWrapper chatRoomsWrapper = (IWrapper) registry.lookup("chatRooms");
            HashMap<String, IChatRoom> chatRooms = chatRoomsWrapper.getChatRooms();
            HashMap<Integer, String> chatRoomsIndex = getIndexMap(chatRooms);

            String selectedRoomIndex = chooseChatRoom(chatRoomsIndex, sc);

            IChatRoom selectedChatRoom = chatRooms.get(selectedRoomIndex);


            selectedChatRoom.connect(participant);
            System.out.println("Connected to chat room: " + selectedChatRoom.name());

            String message;
            System.out.println(participant.name() + "> ");

            do {
                message = sc.nextLine();
                selectedChatRoom.send(participant, message);
            } while (message != "leave()");

            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void welcome() {
        System.out.println("----------------------------------------------------------");
        System.out.println("Welcome to ChatApp v2.0\n");
        System.out.println("\t1. enter your username");
        System.out.println("\t2. select a chat room\n");
        System.out.println("\tCommand:");
        System.out.println("\t\texit() -> leave current chat room");
        System.out.println("----------------------------------------------------------");
    }

    private static String getUsername(Scanner sc) {
        System.out.println("Enter your username: ");
        return sc.nextLine();
    }

    private static String chooseChatRoom(HashMap<Integer,String> rooms, Scanner sc) {
        System.out.println("----------------------------------------------------------");
        System.out.println("Current chat rooms:");
        for (Map.Entry<Integer, String> set : rooms.entrySet()) {
            System.out.println("\t"+set.getKey()+" - "+set.getValue());
        }
        int selectedRoom;
        do {
            System.out.println("Select a chat room (1 to "+rooms.size()+"): ");
            selectedRoom = sc.nextInt();
        } while (selectedRoom < 1 || selectedRoom > rooms.size());
        return rooms.get(selectedRoom);
    }

    private static HashMap<Integer, String> getIndexMap(HashMap<String, IChatRoom> rooms) {
        int i=1;
        HashMap<Integer, String> map = new HashMap<>();
        for (Map.Entry<String, IChatRoom> set : rooms.entrySet()) {
            map.put(i++, set.getKey());
        }
        return map;
    }
}
