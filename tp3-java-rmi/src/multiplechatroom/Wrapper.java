package multiplechatroom;

import java.util.HashMap;

public class Wrapper implements IWrapper {
    public HashMap<String, IChatRoom> chatRooms;
    Wrapper() {
        this.chatRooms = new HashMap<>();
    }
}
