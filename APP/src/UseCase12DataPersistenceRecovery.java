import java.io.*;
import java.util.*;

// Reservation class
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

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
        return reservationId + " | " + customerName + " | " + roomType;
    }
}

// Wrapper for full system state
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    List<Reservation> bookingHistory;
    Map<String, Integer> inventory;

    public SystemState(List<Reservation> bookingHistory, Map<String, Integer> inventory) {
        this.bookingHistory = bookingHistory;
        this.inventory = inventory;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    // Save state
    public static void saveState(SystemState state) {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            out.writeObject(state);
            System.out.println("\n[INFO] System state saved to file.");

        } catch (IOException e) {
            System.out.println("[ERROR] Failed to save state: " + e.getMessage());
        }
    }

    // Load state
    public static SystemState loadState() {
        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            System.out.println("[INFO] Previous system state loaded.");
            return (SystemState) in.readObject();

        } catch (FileNotFoundException e) {
            System.out.println("[INFO] No previous data found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[ERROR] Corrupted data. Starting with safe defaults.");
        }

        // Safe fallback
        return new SystemState(new ArrayList<>(), new HashMap<>());
    }
}

// Main Class
public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        // Step 1: Load previous state
        SystemState state = PersistenceService.loadState();

        List<Reservation> bookingHistory = state.bookingHistory;
        Map<String, Integer> inventory = state.inventory;

        // Step 2: Initialize inventory if first run
        if (inventory.isEmpty()) {
            inventory.put("DELUXE", 2);
            inventory.put("STANDARD", 2);
        }

        // Step 3: Simulate a booking
        String roomType = "DELUXE";
        String customerName = "Avinash";

        if (!inventory.containsKey(roomType)) {
            System.out.println("Invalid room type.");
        } else if (inventory.get(roomType) <= 0) {
            System.out.println("No rooms available.");
        } else {
            String reservationId = roomType.substring(0, 3) + "-" + (bookingHistory.size() + 1);

            Reservation newBooking = new Reservation(reservationId, customerName, roomType);

            bookingHistory.add(newBooking);
            inventory.put(roomType, inventory.get(roomType) - 1);

            System.out.println("Booking CONFIRMED: " + newBooking);
        }

        // Step 4: Display current state
        System.out.println("\n--- Booking History ---");
        for (Reservation r : bookingHistory) {
            System.out.println(r);
        }

        System.out.println("\n--- Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        // Step 5: Save state before shutdown
        PersistenceService.saveState(new SystemState(bookingHistory, inventory));
    }
}