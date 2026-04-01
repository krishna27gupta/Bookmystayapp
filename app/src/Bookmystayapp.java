import java.util.*;
import java.util.concurrent.*;

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
 * Thread-safe room inventory management
 */
class ConcurrentHotelInventory {
    private final Map<String, Integer> inventory;

    public ConcurrentHotelInventory() {
        inventory = new HashMap<>();
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    // synchronized allocation ensures thread safety
    public synchronized void allocateRoom(Reservation reservation) throws InvalidBookingException {
        String roomType = reservation.getRoomType();
        if (!inventory.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
        int available = inventory.get(roomType);
        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + roomType);
        }
        inventory.put(roomType, available - 1);
        reservation.setRoomId(generateRoomId(roomType, available));
    }

    private String generateRoomId(String roomType, int available) {
        return roomType.substring(0, 1).toUpperCase() + (available + 1 + 100);
    }

    public synchronized void displayInventory() {
        System.out.println("\n--- Current Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue() + " rooms available");
        }
    }
}

/**
 * Booking processor running in multiple threads
 */
class BookingTask implements Runnable {
    private Reservation reservation;
    private ConcurrentHotelInventory inventory;

    public BookingTask(Reservation reservation, ConcurrentHotelInventory inventory) {
        this.reservation = reservation;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        try {
            inventory.allocateRoom(reservation);
            System.out.println("Booking successful for " + reservation.getGuestName());
            reservation.displayReservation();
        } catch (InvalidBookingException e) {
            System.out.println("Booking failed for " + reservation.getGuestName() + ": " + e.getMessage());
        }
    }
}

/**
 * Use Case 11: Concurrent Booking Simulation
 */
class UseCase11ConcurrentBookingSimulation {
    public static void main(String[] args) throws InterruptedException {
        ConcurrentHotelInventory inventory = new ConcurrentHotelInventory();

        // Create reservations
        List<Reservation> reservations = new ArrayList<>();
        try {
            reservations.add(new Reservation("Alice", "Single", 2));
            reservations.add(new Reservation("Bob", "Single", 1));
            reservations.add(new Reservation("Charlie", "Double", 3));
            reservations.add(new Reservation("Dana", "Double", 2));
            reservations.add(new Reservation("Evan", "Suite", 1));
            reservations.add(new Reservation("Fiona", "Suite", 1)); // Should fail due to limited inventory
        } catch (InvalidBookingException e) {
            System.out.println("Error creating reservation: " + e.getMessage());
        }

        // Use a fixed thread pool to simulate concurrent booking
        ExecutorService executor = Executors.newFixedThreadPool(4);
        for (Reservation r : reservations) {
            executor.execute(new BookingTask(r, inventory));
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        // Display final inventory
        inventory.displayInventory();
    }
}