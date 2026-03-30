// Version 6.0 - Room Allocation Service (New Class)

import java.util.*;

// Booking Request
class BookingRequest {
    private String guestName;
    private String roomType;

    public BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Inventory Service
class RoomInventory {
    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrement(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }
}

// Booking Service (Core Logic)
class BookingService {

    private Queue<BookingRequest> queue;
    private RoomInventory inventory;

    private Set<String> allocatedRoomIds = new HashSet<>();
    private HashMap<String, Set<String>> allocations = new HashMap<>();

    private int counter = 1;

    public BookingService(Queue<BookingRequest> queue, RoomInventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    public void processBookings() {

        System.out.println("\n=== Processing Bookings ===");

        while (!queue.isEmpty()) {

            BookingRequest req = queue.poll();
            String type = req.getRoomType();

            if (inventory.getAvailability(type) > 0) {

                String roomId = type.replace(" ", "") + "-" + counter++;

                if (!allocatedRoomIds.contains(roomId)) {

                    allocatedRoomIds.add(roomId);

                    allocations.putIfAbsent(type, new HashSet<>());
                    allocations.get(type).add(roomId);

                    inventory.decrement(type);

                    System.out.println("Confirmed → " + req.getGuestName()
                            + " | " + type + " | ID: " + roomId);
                }

            } else {
                System.out.println("Failed → " + req.getGuestName()
                        + " | " + type + " (No Availability)");
            }
        }
    }
}

// Main Class
public class UseCase6RoomAllocationService {

    public static void main(String[] args) {

        // Queue (FIFO)
        Queue<BookingRequest> queue = new LinkedList<>();

        queue.offer(new BookingRequest("Jerin", "Single Room"));
        queue.offer(new BookingRequest("Arun", "Single Room"));
        queue.offer(new BookingRequest("Priya", "Single Room"));
        queue.offer(new BookingRequest("Kumar", "Suite Room"));

        // Inventory
        RoomInventory inventory = new RoomInventory();

        // Booking Service
        BookingService service = new BookingService(queue, inventory);

        // Process bookings
        service.processBookings();
    }
}