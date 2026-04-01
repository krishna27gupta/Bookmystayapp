import java.util.HashMap;
import java.util.Map;

/**
 * Abstract HotelRoom class representing the domain model for all room types.
 */
abstract class HotelRoom {
    protected String type;
    protected int beds;
    protected double price;

    public HotelRoom(String type, int beds, double price) {
        this.type = type;
        this.beds = beds;
        this.price = price;
    }

    public String getType() { return type; }
    public int getBeds() { return beds; }
    public double getPrice() { return price; }

    public void displayInfo() {
        System.out.println("Room Type: " + type + ", Beds: " + beds + ", Price: $" + price);
    }
}

class SingleHotelRoom extends HotelRoom {
    public SingleHotelRoom() { super("Single", 1, 100.0); }
}

class DoubleHotelRoom extends HotelRoom {
    public DoubleHotelRoom() { super("Double", 2, 150.0); }
}

class SuiteHotelRoom extends HotelRoom {
    public SuiteHotelRoom() { super("Suite", 3, 250.0); }
}

/**
 * Manages room availability as a centralized inventory.
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

    public void displayAvailableRooms() {
        System.out.println("Available Rooms:");
        System.out.println("-------------------------");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            if (entry.getValue() > 0) {
                System.out.println(entry.getKey() + " Rooms: " + entry.getValue() + " available");
            }
        }
        System.out.println("-------------------------");
    }
}

/**
 * Handles read-only search logic.
 */
class SearchService {
    private HotelInventory inventory;
    private HotelRoom[] roomTypes;

    public SearchService(HotelInventory inventory) {
        this.inventory = inventory;
        roomTypes = new HotelRoom[] { new SingleHotelRoom(), new DoubleHotelRoom(), new SuiteHotelRoom() };
    }

    public void displaySearchResults() {
        System.out.println("Guest initiates room search...\n");
        boolean anyAvailable = false;
        for (HotelRoom room : roomTypes) {
            if (inventory.getAvailability(room.getType()) > 0) {
                room.displayInfo();
                System.out.println("Available Count: " + inventory.getAvailability(room.getType()));
                System.out.println("-------------------------");
                anyAvailable = true;
            }
        }
        if (!anyAvailable) {
            System.out.println("No rooms available at the moment.");
        }
    }
}

/**
 * Main application class: BookMyStayApp
 */
 class BookMyStayApp {
    public static void main(String[] args) {
        HotelInventory inventory = new HotelInventory();
        SearchService searchService = new SearchService(inventory);

        inventory.displayAvailableRooms();
        searchService.displaySearchResults();
    }
}