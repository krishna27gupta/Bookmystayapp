import java.util.*;

/**
 * Represents a guest's booking reservation.
 */
class Reservation {
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
 * Maintains the booking history in chronological order.
 */
class BookingHistory {
    private List<Reservation> confirmedBookings;

    public BookingHistory() {
        confirmedBookings = new ArrayList<>();
    }

    // Add a confirmed reservation
    public void addBooking(Reservation reservation) {
        confirmedBookings.add(reservation);
        System.out.println("Booking added to history: " + reservation.getGuestName());
    }

    // Retrieve all bookings
    public List<Reservation> getAllBookings() {
        return Collections.unmodifiableList(confirmedBookings); // Prevent modification
    }

    // Display all bookings
    public void displayAllBookings() {
        System.out.println("\n--- Booking History ---");
        if (confirmedBookings.isEmpty()) {
            System.out.println("No bookings recorded yet.");
        } else {
            for (Reservation r : confirmedBookings) {
                r.displayReservation();
            }
        }
    }
}

/**
 * Generates reports from booking history.
 */
class BookingReportService {
    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    // Simple summary report: total bookings and breakdown by room type
    public void generateSummaryReport() {
        System.out.println("\n--- Booking Summary Report ---");
        List<Reservation> bookings = history.getAllBookings();
        if (bookings.isEmpty()) {
            System.out.println("No bookings to report.");
            return;
        }

        Map<String, Integer> roomTypeCount = new HashMap<>();
        for (Reservation r : bookings) {
            roomTypeCount.put(r.getRoomType(), roomTypeCount.getOrDefault(r.getRoomType(), 0) + 1);
        }

        System.out.println("Total Bookings: " + bookings.size());
        for (Map.Entry<String, Integer> entry : roomTypeCount.entrySet()) {
            System.out.println("Room Type: " + entry.getKey() + " | Bookings: " + entry.getValue());
        }
    }
}

/**
 * Demonstrates Use Case 8: Booking History & Reporting
 */
 class UseCase8BookingHistoryReport {
    public static void main(String[] args) {
        BookingHistory bookingHistory = new BookingHistory();
        BookingReportService reportService = new BookingReportService(bookingHistory);

        // Sample reservations
        Reservation r1 = new Reservation("Alice", "Single", 2);
        Reservation r2 = new Reservation("Bob", "Double", 3);
        Reservation r3 = new Reservation("Charlie", "Suite", 1);

        r1.setRoomId("S100");
        r2.setRoomId("D101");
        r3.setRoomId("SU102");

        // Add confirmed reservations to history
        bookingHistory.addBooking(r1);
        bookingHistory.addBooking(r2);
        bookingHistory.addBooking(r3);

        // Display all bookings
        bookingHistory.displayAllBookings();

        // Generate a summary report
        reportService.generateSummaryReport();
    }
}