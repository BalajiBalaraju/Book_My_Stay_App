// Version 5.1 - Booking Request Queue (FIFO)

import java.util.*;

// Reservation (Booking Request)
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

    public void displayRequest() {
        System.out.println("Guest: " + guestName + " | Room Type: " + roomType);
    }
}

// Queue Manager
class BookingRequestQueue {

    private Queue<BookingRequest> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add request (enqueue)
    public void addRequest(BookingRequest request) {
        queue.offer(request);
        System.out.println("Request added for " + request.getGuestName());
    }

    // View all requests (FIFO order)
    public void displayQueue() {
        System.out.println("\n=== Booking Request Queue ===");

        for (BookingRequest req : queue) {
            req.displayRequest();
        }

        System.out.println("=== End of Queue ===\n");
    }
}

// Main Class
class UseCase5BookingRequestQueue {

    public static void main(String[] args) {

        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Adding requests (arrival order)
        bookingQueue.addRequest(new BookingRequest("Jerin", "Single Room"));
        bookingQueue.addRequest(new BookingRequest("Arun", "Double Room"));
        bookingQueue.addRequest(new BookingRequest("Priya", "Suite Room"));

        // Display queue (FIFO)
        bookingQueue.displayQueue();
    }
}
