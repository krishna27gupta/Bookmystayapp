// Abstract class
abstract class Room {
    String type;
    int beds;
    double price;

    Room(String type, int beds, double price) {
        this.type = type;
        this.beds = beds;
        this.price = price;
    }

    void display() {
        System.out.println(type + " | Beds: " + beds + " | Price: $" + price);
    }
}

// Child classes
class SingleRoom extends Room {
    SingleRoom() {
        super("Single Room", 1, 50);
    }
}

class DoubleRoom extends Room {
    DoubleRoom() {
        super("Double Room", 2, 80);
    }
}

class SuiteRoom extends Room {
    SuiteRoom() {
        super("Suite Room", 3, 150);
    }
}

// Main application
public class ROOMTYPE {
    public static void main(String[] args) {

        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        single.display();
        System.out.println("Available: " + singleAvailable);

        doubleRoom.display();
        System.out.println("Available: " + doubleAvailable);

        suite.display();
        System.out.println("Available: " + suiteAvailable);
    }
}