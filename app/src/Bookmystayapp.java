import java.util.*;

/**
 * Represents a guest's booking request.
 */
class Reservation {
    private String guestName;
    private String roomType;
    private int nights;

    public Reservation(String guestName, String roomType, int nights) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public int getNights() { return nights; }

    public void displayReservation() {
        System.out.println("Guest: " + guestName + ", Room Type: " + roomType + ", Nights: " + nights);
    }
}

/**
 * Manages room inventory counts.
 */
class HotelInventory {
    private Map<String, Integer> inventory;

    public HotelInventory() {
        inventory = new HashMap<>();
        inventory.put("Single", 3);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Decrement inventory safely
    public boolean allocateRoom(String roomType) {
        int available = getAvailability(roomType);
        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public void displayAvailableRooms() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " Rooms: " + entry.getValue() + " available");
        }
    }
}

/**
 * Maintains a FIFO queue for booking requests.
 */
class BookingRequestQueue {
    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    public void addRequest(Reservation reservation) {
        queue.add(reservation);
        System.out.println("Booking request received for guest: " + reservation.getGuestName());
    }

    public Reservation processNextRequest() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public void displayPendingRequests() {
        System.out.println("\nPending Booking Requests:");
        if (queue.isEmpty()) {
            System.out.println("No pending requests.");
        } else {
            for (Reservation r : queue) {
                r.displayReservation();
            }
        }
    }
}

/**
 * Handles booking confirmation and room allocation with uniqueness enforcement.
 */
class BookingService {
    private HotelInventory inventory;
    private Map<String, Set<String>> allocatedRoomIds; // RoomType -> Set of RoomIDs
    private int roomIdCounter;

    public BookingService(HotelInventory inventory) {
        this.inventory = inventory;
        allocatedRoomIds = new HashMap<>();
        roomIdCounter = 100; // Starting room ID
    }

    // Confirm booking from a reservation
    public void confirmReservation(Reservation reservation) {
        String roomType = reservation.getRoomType();
        if (inventory.allocateRoom(roomType)) {
            // Generate unique room ID
            String roomId = roomType.substring(0,1).toUpperCase() + roomIdCounter++;
            allocatedRoomIds.putIfAbsent(roomType, new HashSet<>());
            allocatedRoomIds.get(roomType).add(roomId);

            // Display confirmation
            System.out.println("Reservation confirmed for " + reservation.getGuestName() +
                    " | Room Type: " + roomType + " | Assigned Room ID: " + roomId);
        } else {
            System.out.println("Reservation failed for " + reservation.getGuestName() +
                    " | Room Type: " + roomType + " | No rooms available");
        }
    }

    // Display all allocated rooms
    public void displayAllocatedRooms() {
        System.out.println("\nAllocated Rooms:");
        for (Map.Entry<String, Set<String>> entry : allocatedRoomIds.entrySet()) {
            System.out.println(entry.getKey() + " Rooms: " + entry.getValue());
        }
    }
}

/**
 * Demonstrates Use Case 6: Reservation Confirmation & Room Allocation
 */
 class UseCase6RoomAllocationService {
    public static void main(String[] args) {
        HotelInventory inventory = new HotelInventory();
        BookingRequestQueue requestQueue = new BookingRequestQueue();
        BookingService bookingService = new BookingService(inventory);

        // Simulate booking requests
        requestQueue.addRequest(new Reservation("Alice", "Single", 2));
        requestQueue.addRequest(new Reservation("Bob", "Double", 3));
        requestQueue.addRequest(new Reservation("Charlie", "Suite", 1));
        requestQueue.addRequest(new Reservation("David", "Single", 1));
        requestQueue.addRequest(new Reservation("Eve", "Single", 2));
        requestQueue.addRequest(new Reservation("Frank", "Single", 1)); // Should fail if inventory exhausted

        requestQueue.displayPendingRequests();
        inventory.displayAvailableRooms();

        // Process requests in FIFO order
        System.out.println("\nProcessing Booking Requests...");
        while (!requestQueue.isEmpty()) {
            Reservation res = requestQueue.processNextRequest();
            bookingService.confirmReservation(res);
        }

        inventory.displayAvailableRooms();
        bookingService.displayAllocatedRooms();
    }
}