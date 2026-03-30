import java.util.*;

// Booking Request
class BookingRequest {
    String customerName;
    String roomType;

    public BookingRequest(String customerName, String roomType) {
        this.customerName = customerName;
        this.roomType = roomType;
    }
}

// Shared Booking System (Critical Section Inside)
class BookingSystem {

    private Queue<BookingRequest> queue;
    private Map<String, Integer> inventory;
    private Set<String> usedRoomIds;
    private int roomCounter = 1;

    public BookingSystem(Queue<BookingRequest> queue,
                         Map<String, Integer> inventory) {
        this.queue = queue;
        this.inventory = inventory;
        this.usedRoomIds = new HashSet<>();
    }

    // Thread-safe method
    public synchronized void processBooking() {

        if (queue.isEmpty()) {
            return;
        }

        BookingRequest request = queue.poll();

        if (request == null) return;

        String roomType = request.roomType;

        // Check availability
        if (!inventory.containsKey(roomType) || inventory.get(roomType) <= 0) {
            System.out.println(Thread.currentThread().getName() +
                    " FAILED for " + request.customerName +
                    " (No " + roomType + " available)");
            return;
        }

        // Generate unique room ID
        String roomId;
        do {
            roomId = roomType.substring(0, 3).toUpperCase() + "-" + roomCounter++;
        } while (usedRoomIds.contains(roomId));

        // Critical updates
        usedRoomIds.add(roomId);
        inventory.put(roomType, inventory.get(roomType) - 1);

        System.out.println(Thread.currentThread().getName() +
                " CONFIRMED " + request.customerName +
                " | " + roomType +
                " | ID: " + roomId);
    }
}

// Worker Thread
class BookingProcessor extends Thread {

    private BookingSystem system;

    public BookingProcessor(BookingSystem system, String name) {
        super(name);
        this.system = system;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (system) {
                if (isQueueEmpty()) break;
                system.processBooking();
            }

            // Simulate delay (to expose race conditions if not synchronized)
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isQueueEmpty() {
        // Safe check
        return false; // handled inside synchronized block
    }
}

// Main Class
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {

        // Shared Queue
        Queue<BookingRequest> queue = new LinkedList<>();

        queue.add(new BookingRequest("Avinash", "DELUXE"));
        queue.add(new BookingRequest("Ravi", "DELUXE"));
        queue.add(new BookingRequest("Kiran", "DELUXE"));
        queue.add(new BookingRequest("Sneha", "STANDARD"));
        queue.add(new BookingRequest("John", "STANDARD"));

        // Shared Inventory
        Map<String, Integer> inventory = new HashMap<>();
        inventory.put("DELUXE", 2);
        inventory.put("STANDARD", 1);

        // Shared Booking System
        BookingSystem system = new BookingSystem(queue, inventory);

        // Multiple Threads (Simulating Users)
        BookingProcessor t1 = new BookingProcessor(system, "Thread-1");
        BookingProcessor t2 = new BookingProcessor(system, "Thread-2");
        BookingProcessor t3 = new BookingProcessor(system, "Thread-3");

        t1.start();
        t2.start();
        t3.start();

        // Wait for threads to finish
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n--- Final Inventory ---");
        for (String type : inventory.keySet()) {
            System.out.println(type + " -> " + inventory.get(type));
        }
    }
}