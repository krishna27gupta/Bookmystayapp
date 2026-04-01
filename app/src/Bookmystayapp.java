import java.util.LinkedList;
import java.util.Queue;

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
 * Manages incoming booking requests in a first-come-first-served manner.
 */
class BookingRequestQueue {
    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add a booking request to the queue (FIFO)
    public void addRequest(Reservation reservation) {
        queue.add(reservation);
        System.out.println("Booking request received for guest: " + reservation.getGuestName());
    }

    // Peek at the next request without removing it
    public Reservation peekNextRequest() {
        return queue.peek();
    }

    // Process the next request (for future allocation)
    public Reservation processNextRequest() {
        return queue.poll();
    }

    // Display all pending requests
    public void displayPendingRequests() {
        System.out.println("\nCurrent Booking Request Queue:");
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
 * Demonstrates Use Case 5: Booking Request Intake (FIFO)
 */class UseCase5BookingRequestQueue {
    public static void main(String[] args) {
        BookingRequestQueue requestQueue = new BookingRequestQueue();

        // Simulate multiple guest booking requests
        Reservation r1 = new Reservation("Alice", "Single", 2);
        Reservation r2 = new Reservation("Bob", "Double", 3);
        Reservation r3 = new Reservation("Charlie", "Suite", 1);

        // Add requests to the queue
        requestQueue.addRequest(r1);
        requestQueue.addRequest(r2);
        requestQueue.addRequest(r3);

        // Display pending requests
        requestQueue.displayPendingRequests();

        // Peek at the next request
        Reservation next = requestQueue.peekNextRequest();
        System.out.println("\nNext request to process:");
        if (next != null) next.displayReservation();
    }
}