import java.util.*;

// Add-On Service class
class Service {
    String serviceName;
    double cost;

    public Service(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    @Override
    public String toString() {
        return serviceName + " (₹" + cost + ")";
    }
}

public class UseCase7AddOnServiceSelection {

    // Reservation ID -> List of Services
    private static Map<String, List<Service>> reservationServices = new HashMap<>();

    public static void main(String[] args) {

        // Assume these reservation IDs are already created in Use Case 6
        String res1 = "DEL-1";
        String res2 = "SUI-2";

        // Add services
        addService(res1, new Service("Breakfast", 200));
        addService(res1, new Service("Airport Pickup", 500));
        addService(res2, new Service("Extra Bed", 300));

        // Display services
        displayServices(res1);
        displayServices(res2);

        // Calculate cost
        calculateTotalCost(res1);
        calculateTotalCost(res2);
    }

    // Add service to reservation
    private static void addService(String reservationId, Service service) {
        reservationServices.putIfAbsent(reservationId, new ArrayList<>());
        reservationServices.get(reservationId).add(service);

        System.out.println("Service added to " + reservationId + ": " + service);
    }

    // Display services for a reservation
    private static void displayServices(String reservationId) {
        System.out.println("\nServices for Reservation " + reservationId + ":");

        List<Service> services = reservationServices.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No services selected.");
            return;
        }

        for (Service s : services) {
            System.out.println("- " + s);
        }
    }

    // Calculate total additional cost
    private static void calculateTotalCost(String reservationId) {
        List<Service> services = reservationServices.get(reservationId);

        double total = 0;

        if (services != null) {
            for (Service s : services) {
                total += s.cost;
            }
        }

        System.out.println("Total Add-On Cost for " + reservationId + ": ₹" + total);
    }
}