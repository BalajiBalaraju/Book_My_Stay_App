// Version 4.1 - Room Search & Availability Check

import java.util.*;

// Abstract Room Class
abstract class Room {
    private int beds;
    private double size;
    private double price;

    public Room(int beds, double size, double price) {
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public int getBeds() {
        return beds;
    }

    public double getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public abstract String getRoomType();

    public void displayDetails() {
        System.out.println("Room Type: " + getRoomType());
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sq.ft");
        System.out.println("Price: ₹" + price);
    }
}

// Concrete Room Types
class SingleRoom extends Room {
    public SingleRoom() {
        super(1, 200, 1500);
    }

    public String getRoomType() {
        return "Single Room";
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super(2, 350, 2500);
    }

    public String getRoomType() {
        return "Double Room";
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super(3, 600, 5000);
    }

    public String getRoomType() {
        return "Suite Room";
    }
}

// Inventory Class (Read-only usage here)
class RoomInventory {
    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 10);
        inventory.put("Double Room", 0); // unavailable
        inventory.put("Suite Room", 2);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public Set<String> getAllRoomTypes() {
        return inventory.keySet();
    }
}

// Search Service (READ ONLY)
class RoomSearchService {

    public void searchAvailableRooms(RoomInventory inventory, List<Room> rooms) {

        System.out.println("=== Available Rooms ===\n");

        for (Room room : rooms) {
            int available = inventory.getAvailability(room.getRoomType());

            // Show only available rooms
            if (available > 0) {
                room.displayDetails();
                System.out.println("Available: " + available + "\n");
            }
        }

        System.out.println("=== End of Search ===");
    }
}

// Main Class (Your Structure)
public class BookMyStayApp {
    public static void main(String[] args) {

        // Room list
        List<Room> rooms = new ArrayList<>();
        rooms.add(new SingleRoom());
        rooms.add(new DoubleRoom());
        rooms.add(new SuiteRoom());

        // Inventory
        RoomInventory inventory = new RoomInventory();

        // Search Service
        RoomSearchService searchService = new RoomSearchService();

        // Perform search (NO state change)
        searchService.searchAvailableRooms(inventory, rooms);
    }
}