import java.util.HashMap;
import java.util.Map;

/**
 * Custom exception to handle invalid bookings.
 */
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

/**
 * Represents a guest reservation.
 */
class Reservation {
    private String guestName;
    private String roomType;
    private int nights;
    private String roomId;

    public Reservation(String guestName, String roomType, int nights) throws InvalidBookingException {
        if (guestName == null || guestName.isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }
        if (roomType == null || roomType.isEmpty()) {
            throw new InvalidBookingException("Room type must be specified.");
        }
        if (nights <= 0) {
            throw new InvalidBookingException("Number of nights must be positive.");
        }

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
 * Manages room availability and allocation.
 * Renamed from RoomInventory to HotelInventory
 */
class HotelInventory {
    private Map<String, Integer> inventory;

    public HotelInventory() {
        inventory = new HashMap<>();
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    public void allocateRoom(String roomType) throws InvalidBookingException {
        if (!inventory.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
        int available = inventory.get(roomType);
        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + roomType);
        }
        inventory.put(roomType, available - 1);
    }

    public void displayInventory() {
        System.out.println("\n--- Current Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue() + " rooms available");
        }
    }
}

/**
 * Use Case 9: Error Handling & Validation
 */
class UseCase9ErrorHandlingValidation {
    public static void main(String[] args) {
        HotelInventory inventory = new HotelInventory();  // Updated here

        // Display initial inventory
        inventory.displayInventory();

        try {
            // Valid booking
            Reservation r1 = new Reservation("Alice", "Single", 2);
            inventory.allocateRoom(r1.getRoomType());
            r1.setRoomId("S100");
            r1.displayReservation();

            // Invalid booking: negative nights
            Reservation r2 = new Reservation("Bob", "Double", -3);
            inventory.allocateRoom(r2.getRoomType());
            r2.setRoomId("D101");
            r2.displayReservation();

        } catch (InvalidBookingException e) {
            System.out.println("Booking failed: " + e.getMessage());
        }

        try {
            // Invalid room type
            Reservation r3 = new Reservation("Charlie", "Triple", 1);
            inventory.allocateRoom(r3.getRoomType());
            r3.setRoomId("T102");
            r3.displayReservation();
        } catch (InvalidBookingException e) {
            System.out.println("Booking failed: " + e.getMessage());
        }

        try {
            // Exhaust inventory for Suite
            Reservation r4 = new Reservation("Dana", "Suite", 1);
            inventory.allocateRoom(r4.getRoomType());
            r4.setRoomId("SU103");
            r4.displayReservation();

            // Another Suite booking should fail
            Reservation r5 = new Reservation("Evan", "Suite", 2);
            inventory.allocateRoom(r5.getRoomType());
            r5.setRoomId("SU104");
            r5.displayReservation();

        } catch (InvalidBookingException e) {
            System.out.println("Booking failed: " + e.getMessage());
        }

        // Display final inventory
        inventory.displayInventory();
    }
}