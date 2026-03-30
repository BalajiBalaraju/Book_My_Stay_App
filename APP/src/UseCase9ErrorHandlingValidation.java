import java.util.*;

// Custom Exception
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Booking Request class
class BookingRequest {
    String customerName;
    String roomType;

    public BookingRequest(String customerName, String roomType) {
        this.customerName = customerName;
        this.roomType = roomType;
    }
}

// Validator class
class Validator {

    // Validate booking input and system state
    public static void validate(BookingRequest request, Map<String, Integer> inventory)
            throws InvalidBookingException {

        // Check null or empty input
        if (request.customerName == null || request.customerName.isEmpty()) {
            throw new InvalidBookingException("Customer name cannot be empty.");
        }

        if (request.roomType == null || request.roomType.isEmpty()) {
            throw new InvalidBookingException("Room type cannot be empty.");
        }

        // Validate room type exists
        if (!inventory.containsKey(request.roomType)) {
            throw new InvalidBookingException("Invalid room type: " + request.roomType);
        }

        // Check availability
        if (inventory.get(request.roomType) <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + request.roomType);
        }
    }
}

public class UseCase9ErrorHandlingValidation {

    private static Map<String, Integer> inventory = new HashMap<>();
    private static Set<String> usedRoomIds = new HashSet<>();
    private static int roomCounter = 1;

    public static void main(String[] args) {

        // Initialize inventory
        inventory.put("DELUXE", 1);
        inventory.put("SUITE", 0);   // intentionally 0 to trigger error
        inventory.put("STANDARD", 2);

        // Booking requests (some invalid)
        List<BookingRequest> requests = Arrays.asList(
                new BookingRequest("Avinash", "DELUXE"),
                new BookingRequest("", "STANDARD"),          // invalid name
                new BookingRequest("Ravi", "SUITE"),         // no availability
                new BookingRequest("Kiran", "PENTHOUSE"),    // invalid type
                new BookingRequest("Sneha", "STANDARD")
        );

        // Process bookings safely
        for (BookingRequest request : requests) {
            try {
                processBooking(request);
            } catch (InvalidBookingException e) {
                System.out.println("Booking FAILED: " + e.getMessage());
            }
        }
    }

    private static void processBooking(BookingRequest request)
            throws InvalidBookingException {

        // Step 1: Validate (Fail-Fast)
        Validator.validate(request, inventory);

        // Step 2: Generate unique room ID
        String roomId;
        do {
            roomId = request.roomType.substring(0, 3).toUpperCase() + "-" + roomCounter++;
        } while (usedRoomIds.contains(roomId));

        // Step 3: Safe allocation (only after validation)
        usedRoomIds.add(roomId);

        // Step 4: Update inventory safely
        int current = inventory.get(request.roomType);
        if (current - 1 < 0) {
            throw new InvalidBookingException("Inventory cannot go negative!");
        }
        inventory.put(request.roomType, current - 1);

        // Step 5: Confirm booking
        System.out.println("Booking CONFIRMED for " + request.customerName +
                " | Room: " + request.roomType +
                " | ID: " + roomId);
    }
}