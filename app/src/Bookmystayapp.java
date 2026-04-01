import java.io.*;
import java.util.*;

/**
 * Represents a guest reservation (Serializable for persistence)
 */
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String guestName;
    private String roomType;
    private int nights;
    private String roomId;

    public Reservation(String guestName, String roomType, int nights) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public int getNights() { return nights; }
    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public void displayReservation() {
        System.out.println("Guest: " + guestName +
                ", Room Type: " + roomType +
                ", Nights: " + nights +
                (roomId != null ? ", Room ID: " + roomId : ""));
    }
}

/**
 * Manages room inventory (Serializable for persistence)
 */
class PersistentInventory implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Integer> inventory;

    public PersistentInventory() {
        inventory = new HashMap<>();
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    public void allocateRoom(String roomType) throws Exception {
        if (!inventory.containsKey(roomType)) {
            throw new Exception("Invalid room type: " + roomType);
        }
        int available = inventory.get(roomType);
        if (available <= 0) {
            throw new Exception("No rooms available for type: " + roomType);
        }
        inventory.put(roomType, available - 1);
    }

    public void releaseRoom(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public void displayInventory() {
        System.out.println("\n--- Current Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " rooms available");
        }
    }
}

/**
 * Handles persistence (saving and loading) of system state
 */
class PersistenceService {
    private static final String FILE_NAME = "booking_system_state.dat";

    public static void saveState(List<Reservation> bookings, PersistentInventory inventory) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(bookings);
            oos.writeObject(inventory);
            System.out.println("System state saved successfully.");
        } catch (IOException e) {
            System.out.println("Failed to save system state: " + e.getMessage());
        }
    }

    public static Object[] loadState() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("No saved state found. Starting fresh.");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            List<Reservation> bookings = (List<Reservation>) ois.readObject();
            PersistentInventory inventory = (PersistentInventory) ois.readObject();
            System.out.println("System state restored successfully.");
            return new Object[]{bookings, inventory};
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed to restore system state: " + e.getMessage());
            return null;
        }
    }
}

/**
 * Use Case 12: Data Persistence & System Recovery
 */
class UseCase12DataPersistenceRecovery {
    public static void main(String[] args) {
        List<Reservation> bookings;
        PersistentInventory inventory;

        // Attempt to restore previous state
        Object[] restoredState = PersistenceService.loadState();
        if (restoredState != null) {
            bookings = (List<Reservation>) restoredState[0];
            inventory = (PersistentInventory) restoredState[1];
        } else {
            bookings = new ArrayList<>();
            inventory = new PersistentInventory();
        }

        inventory.displayInventory();

        // Simulate new booking
        try {
            Reservation r1 = new Reservation("Alice", "Single", 2);
            inventory.allocateRoom(r1.getRoomType());
            r1.setRoomId("S100");
            bookings.add(r1);
            System.out.println("\nNew booking added:");
            r1.displayReservation();
        } catch (Exception e) {
            System.out.println("Booking failed: " + e.getMessage());
        }

        inventory.displayInventory();

        // Save system state before exit
        PersistenceService.saveState(bookings, inventory);
    }
}