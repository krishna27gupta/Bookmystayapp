import java.util.*;

/**
 * Custom exception for invalid booking operations
 */
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

/**
 * Represents a guest reservation
 */
class Reservation {
    private String guestName;
    private String roomType;
    private int nights;
    private String roomId;
    private boolean cancelled;

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
        this.cancelled = false;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public int getNights() { return nights; }
    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }
    public boolean isCancelled() { return cancelled; }
    public void cancel() { this.cancelled = true; }

    public void displayReservation() {
        System.out.println("Guest: " + guestName +
                ", Room Type: " + roomType +
                ", Nights: " + nights +
                (roomId != null ? ", Room ID: " + roomId : "") +
                (cancelled ? " [CANCELLED]" : ""));
    }
}

/**
 * Manages room inventory
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

    public void releaseRoom(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public void displayInventory() {
        System.out.println("\n--- Current Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue() + " rooms available");
        }
    }
}

/**
 * Use Case 10: Booking Cancellation & Inventory Rollback
 */
 class UseCase10BookingCancellation {
    public static void main(String[] args) {
        HotelInventory inventory = new HotelInventory();
        Stack<String> rollbackStack = new Stack<>();
        List<Reservation> confirmedBookings = new ArrayList<>();

        try {
            // Book a few reservations
            Reservation r1 = new Reservation("Alice", "Single", 2);
            inventory.allocateRoom(r1.getRoomType());
            r1.setRoomId("S100");
            confirmedBookings.add(r1);
            rollbackStack.push(r1.getRoomId());
            r1.displayReservation();

            Reservation r2 = new Reservation("Bob", "Double", 3);
            inventory.allocateRoom(r2.getRoomType());
            r2.setRoomId("D101");
            confirmedBookings.add(r2);
            rollbackStack.push(r2.getRoomId());
            r2.displayReservation();

            Reservation r3 = new Reservation("Charlie", "Suite", 1);
            inventory.allocateRoom(r3.getRoomType());
            r3.setRoomId("SU102");
            confirmedBookings.add(r3);
            rollbackStack.push(r3.getRoomId());
            r3.displayReservation();

        } catch (InvalidBookingException e) {
            System.out.println("Booking failed: " + e.getMessage());
        }

        // Display inventory before cancellation
        inventory.displayInventory();

        System.out.println("\n--- Performing Cancellations ---");

        // Cancel last booking using LIFO
        if (!rollbackStack.isEmpty()) {
            String lastRoomId = rollbackStack.pop();
            for (Reservation r : confirmedBookings) {
                if (r.getRoomId().equals(lastRoomId) && !r.isCancelled()) {
                    r.cancel();
                    inventory.releaseRoom(r.getRoomType());
                    System.out.println("Cancelled booking for Room ID: " + lastRoomId);
                    r.displayReservation();
                    break;
                }
            }
        }

        // Cancel first booking manually
        for (Reservation r : confirmedBookings) {
            if (r.getGuestName().equals("Alice") && !r.isCancelled()) {
                r.cancel();
                inventory.releaseRoom(r.getRoomType());
                System.out.println("Cancelled booking for Alice");
                r.displayReservation();
                break;
            }
        }

        // Display final inventory
        inventory.displayInventory();
    }
}