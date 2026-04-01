import java.util.*;

/**
 * Represents a guest's booking reservation.
 */
class Reservation {
    private String guestName;
    private String roomType;
    private int nights;
    private String roomId; // Assigned during allocation

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
 * Represents an optional add-on service.
 */
class AddOnService {
    private String name;
    private double price;

    public AddOnService(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }

    public void displayService() {
        System.out.println(name + " ($" + price + ")");
    }
}

/**
 * Manages add-on services for reservations.
 */
class AddOnServiceManager {
    private Map<String, List<AddOnService>> reservationServices;

    public AddOnServiceManager() {
        reservationServices = new HashMap<>();
    }

    // Attach a service to a reservation
    public void addServiceToReservation(String roomId, AddOnService service) {
        reservationServices.putIfAbsent(roomId, new ArrayList<>());
        reservationServices.get(roomId).add(service);
        System.out.println("Added service '" + service.getName() + "' to Reservation ID: " + roomId);
    }

    // Calculate total add-on cost for a reservation
    public double calculateTotalAddOnCost(String roomId) {
        double total = 0.0;
        if (reservationServices.containsKey(roomId)) {
            for (AddOnService s : reservationServices.get(roomId)) {
                total += s.getPrice();
            }
        }
        return total;
    }

    // Display all add-on services for a reservation
    public void displayServices(String roomId) {
        System.out.println("\nServices for Reservation ID: " + roomId);
        if (!reservationServices.containsKey(roomId) || reservationServices.get(roomId).isEmpty()) {
            System.out.println("No add-on services selected.");
        } else {
            for (AddOnService s : reservationServices.get(roomId)) {
                s.displayService();
            }
            System.out.println("Total Add-On Cost: $" + calculateTotalAddOnCost(roomId));
        }
    }
}

/**
 * Demonstrates Use Case 7: Add-On Service Selection
 */
class UseCase7AddOnServiceSelection {
    public static void main(String[] args) {
        // Sample reservations with allocated room IDs
        Reservation r1 = new Reservation("Alice", "Single", 2);
        Reservation r2 = new Reservation("Bob", "Double", 3);

        r1.setRoomId("S100");
        r2.setRoomId("D101");

        // Initialize add-on service manager
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Define available services
        AddOnService breakfast = new AddOnService("Breakfast", 20.0);
        AddOnService spa = new AddOnService("Spa Access", 50.0);
        AddOnService airportPickup = new AddOnService("Airport Pickup", 30.0);

        // Guests select services
        serviceManager.addServiceToReservation(r1.getRoomId(), breakfast);
        serviceManager.addServiceToReservation(r1.getRoomId(), spa);

        serviceManager.addServiceToReservation(r2.getRoomId(), breakfast);
        serviceManager.addServiceToReservation(r2.getRoomId(), airportPickup);

        // Display reservations
        System.out.println("\n--- Reservations ---");
        r1.displayReservation();
        r2.displayReservation();

        // Display add-on services
        serviceManager.displayServices(r1.getRoomId());
        serviceManager.displayServices(r2.getRoomId());
    }
}