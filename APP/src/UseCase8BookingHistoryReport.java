import java.util.*;

// Reservation class
class Reservation {
    String reservationId;
    String customerName;
    String roomType;

    public Reservation(String reservationId, String customerName, String roomType) {
        this.reservationId = reservationId;
        this.customerName = customerName;
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Customer: " + customerName +
                ", Room Type: " + roomType;
    }
}

// Booking History (stores confirmed reservations)
class BookingHistory {
    private List<Reservation> history = new ArrayList<>();

    // Add reservation to history
    public void addReservation(Reservation reservation) {
        history.add(reservation);
    }

    // Get all reservations
    public List<Reservation> getAllReservations() {
        return history;
    }
}

// Report Service (generates reports)
class BookingReportService {

    // Display all bookings
    public static void displayAllBookings(List<Reservation> reservations) {
        System.out.println("\n--- Booking History ---");

        if (reservations.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }

    // Generate summary report (count by room type)
    public static void generateSummary(List<Reservation> reservations) {
        System.out.println("\n--- Booking Summary Report ---");

        Map<String, Integer> countByType = new HashMap<>();

        for (Reservation r : reservations) {
            countByType.put(r.roomType,
                    countByType.getOrDefault(r.roomType, 0) + 1);
        }

        for (String type : countByType.keySet()) {
            System.out.println(type + " Rooms Booked: " + countByType.get(type));
        }
    }
}

// Main class
public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        BookingHistory bookingHistory = new BookingHistory();

        // Simulate confirmed bookings (from Use Case 6)
        Reservation r1 = new Reservation("DEL-1", "Avinash", "DELUXE");
        Reservation r2 = new Reservation("SUI-2", "Ravi", "SUITE");
        Reservation r3 = new Reservation("STA-3", "Kiran", "STANDARD");

        // Add to booking history
        bookingHistory.addReservation(r1);
        bookingHistory.addReservation(r2);
        bookingHistory.addReservation(r3);

        // Admin views booking history
        List<Reservation> allBookings = bookingHistory.getAllReservations();

        BookingReportService.displayAllBookings(allBookings);

        // Generate report
        BookingReportService.generateSummary(allBookings);
    }
}