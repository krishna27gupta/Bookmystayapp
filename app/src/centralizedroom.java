import java.util.HashMap;

class RoomInventory {

    private HashMap<String, Integer> inventory;

    // Constructor to initialize room availability
    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Method to get availability
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Method to update availability
    public void updateAvailability(String roomType, int count) {
        inventory.put(roomType, count);
    }

    // Display inventory
    public void displayInventory() {
        for (String room : inventory.keySet()) {
            System.out.println(room + " Available: " + inventory.get(room));
        }
    }
}

public class centralizedroom {
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();

        System.out.println("Current Room Inventory:");
        inventory.displayInventory();
    }
}
