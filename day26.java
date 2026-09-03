
import java.io.*;
import java.time.LocalDate;
import java.util.*;

/* 
   CUSTOM EXCEPTION (Bonus: Exception Handling)
 */
class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
    }
}

/* 
   VEHICLE CLASS
    */
class Vehicle {
    private String vehicleNumber;
    private String model;
    private String type; // Car, Bike, Truck etc.

    public Vehicle(String vehicleNumber, String model, String type) {
        this.vehicleNumber = vehicleNumber;
        this.model = model;
        this.type = type;
    }

    public String getVehicleNumber() { return vehicleNumber; }
    public String getModel() { return model; }
    public String getType() { return type; }

    @Override
    public String toString() {
        return vehicleNumber + " (" + model + ", " + type + ")";
    }
}

/* ==========================================================
   CUSTOMER CLASS
   ========================================================== */
class Customer {
    private static int counter = 1;
    private int id;
    private String name;
    private String phone;
    private List<Vehicle> vehicles = new ArrayList<>();

    public Customer(String name, String phone) {
        this.id = counter++;
        this.name = name;
        this.phone = phone;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public List<Vehicle> getVehicles() { return vehicles; }

    public void addVehicle(Vehicle v) {
        vehicles.add(v);
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Name: " + name + " | Phone: " + phone + " | Vehicles: " + vehicles;
    }
}

/* ==========================================================
   TECHNICIAN CLASS
   ========================================================== */
class Technician {
    private static int counter = 1;
    private int id;
    private String name;
    private String specialization;

    public Technician(String name, String specialization) {
        this.id = counter++;
        this.name = name;
        this.specialization = specialization;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }

    @Override
    public String toString() {
        return "Tech ID: " + id + " | " + name + " (" + specialization + ")";
    }
}

/* ==========================================================
   SERVICE CLASS
   ========================================================== */
class Service {
    private static int counter = 1;
    private int serviceId;
    private Customer customer;
    private Vehicle vehicle;
    private Technician technician;
    private String serviceType;
    private LocalDate date;
    private double cost;
    private String status; // Scheduled, Completed

    public Service(Customer customer, Vehicle vehicle, Technician technician,
                   String serviceType, LocalDate date, double cost) {
        this.serviceId = counter++;
        this.customer = customer;
        this.vehicle = vehicle;
        this.technician = technician;
        this.serviceType = serviceType;
        this.date = date;
        this.cost = cost;
        this.status = "Scheduled";
    }

    public int getServiceId() { return serviceId; }
    public Customer getCustomer() { return customer; }
    public Vehicle getVehicle() { return vehicle; }
    public Technician getTechnician() { return technician; }
    public String getServiceType() { return serviceType; }
    public LocalDate getDate() { return date; }
    public double getCost() { return cost; }
    public String getStatus() { return status; }
    public void markCompleted() { this.status = "Completed"; }

    @Override
    public String toString() {
        return "Service#" + serviceId + " | " + serviceType + " | Vehicle: " + vehicle.getVehicleNumber()
                + " | Tech: " + technician.getName() + " | Date: " + date
                + " | Cost: Rs." + cost + " | Status: " + status;
    }
}

/* ==========================================================
   INVOICE CLASS
   ========================================================== */
class Invoice {
    private static int counter = 1;
    private int invoiceId;
    private Service service;
    private double gst;
    private double totalAmount;

    public Invoice(Service service) {
        this.invoiceId = counter++;
        this.service = service;
        this.gst = service.getCost() * 0.18; // 18% GST
        this.totalAmount = service.getCost() + gst;
    }

    public String generateInvoiceText() {
        StringBuilder sb = new StringBuilder();
        sb.append("---------------------------------------\n");
        sb.append("           SERVICE INVOICE\n");
        sb.append("---------------------------------------\n");
        sb.append("Invoice ID   : ").append(invoiceId).append("\n");
        sb.append("Customer     : ").append(service.getCustomer().getName()).append("\n");
        sb.append("Vehicle      : ").append(service.getVehicle()).append("\n");
        sb.append("Service Type : ").append(service.getServiceType()).append("\n");
        sb.append("Technician   : ").append(service.getTechnician().getName()).append("\n");
        sb.append("Date         : ").append(service.getDate()).append("\n");
        sb.append(String.format("Service Cost : Rs.%.2f%n", service.getCost()));
        sb.append(String.format("GST (18%%)    : Rs.%.2f%n", gst));
        sb.append(String.format("TOTAL AMOUNT : Rs.%.2f%n", totalAmount));
        sb.append("---------------------------------------\n");
        return sb.toString();
    }

    public int getInvoiceId() { return invoiceId; }
}


public class day26{

    static Scanner sc = new Scanner(System.in);
    static List<Customer> customers = new ArrayList<>();
    static List<Technician> technicians = new ArrayList<>();
    static List<Service> services = new ArrayList<>();
    static final String FILE_NAME = "service_history.txt";

    public static void main(String[] args) {
        seedTechnicians();

        int choice;
        do {
            printMenu();
            choice = readInt("Enter choice: ");

            try {
                switch (choice) {
                    case 1 -> registerCustomer();
                    case 2 -> addVehicle();
                    case 3 -> scheduleService();
                    case 4 -> generateInvoice();
                    case 5 -> viewServiceHistory();
                    case 6 -> searchServiceRecords();
                    case 7 -> sortCustomersByServiceDate();
                    case 0 -> System.out.println("Exiting... Thank you!");
                    default -> System.out.println("Invalid choice. Try again.");
                }
            } catch (InvalidInputException e) {
                System.out.println("Input Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected Error: " + e.getMessage());
            }

        } while (choice != 0);

        sc.close();
    }

    static void printMenu() {
        System.out.println("\n===== VEHICLE SERVICE CENTER =====");
        System.out.println("1. Register Customer");
        System.out.println("2. Add Vehicle Details");
        System.out.println("3. Schedule Service");
        System.out.println("4. Generate Invoice");
        System.out.println("5. View Service History");
        System.out.println("6. Search Service Records");
        System.out.println("7. Sort Customers by Service Date");
        System.out.println("0. Exit");
    }

    static void seedTechnicians() {
        technicians.add(new Technician("Ramesh", "Engine Specialist"));
        technicians.add(new Technician("Suresh", "Electrical Specialist"));
        technicians.add(new Technician("Kiran", "General Mechanic"));
    }

    /* ---------------- Register Customer ---------------- */
    static void registerCustomer() throws InvalidInputException {
        System.out.print("Enter customer name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) throw new InvalidInputException("Name cannot be empty.");

        System.out.print("Enter phone number: ");
        String phone = sc.nextLine().trim();
        if (!phone.matches("\\d{10}")) throw new InvalidInputException("Phone must be 10 digits.");

        Customer c = new Customer(name, phone);
        customers.add(c);
        System.out.println("Customer registered successfully! " + c);
    }

    /* ---------------- Add Vehicle ---------------- */
    static void addVehicle() throws InvalidInputException {
        Customer c = selectCustomer();
        if (c == null) return;

        System.out.print("Enter vehicle number: ");
        String number = sc.nextLine().trim();
        if (number.isEmpty()) throw new InvalidInputException("Vehicle number cannot be empty.");

        System.out.print("Enter vehicle model: ");
        String model = sc.nextLine().trim();

        System.out.print("Enter vehicle type (Car/Bike/Truck): ");
        String type = sc.nextLine().trim();

        Vehicle v = new Vehicle(number, model, type);
        c.addVehicle(v);
        System.out.println("Vehicle added successfully to " + c.getName());
    }

    /* ---------------- Schedule Service ---------------- */
    static void scheduleService() throws InvalidInputException {
        Customer c = selectCustomer();
        if (c == null) return;

        if (c.getVehicles().isEmpty()) {
            System.out.println("This customer has no vehicles registered.");
            return;
        }

        System.out.println("Select vehicle:");
        for (int i = 0; i < c.getVehicles().size(); i++) {
            System.out.println((i + 1) + ". " + c.getVehicles().get(i));
        }
        int vIndex = readInt("Choice: ") - 1;
        if (vIndex < 0 || vIndex >= c.getVehicles().size())
            throw new InvalidInputException("Invalid vehicle selection.");
        Vehicle vehicle = c.getVehicles().get(vIndex);

        System.out.println("Select technician:");
        for (int i = 0; i < technicians.size(); i++) {
            System.out.println((i + 1) + ". " + technicians.get(i));
        }
        int tIndex = readInt("Choice: ") - 1;
        if (tIndex < 0 || tIndex >= technicians.size())
            throw new InvalidInputException("Invalid technician selection.");
        Technician tech = technicians.get(tIndex);

        System.out.print("Enter service type (Oil Change/General Service/Repair): ");
        String type = sc.nextLine().trim();

        double cost = readDouble("Enter estimated cost: ");
        if (cost < 0) throw new InvalidInputException("Cost cannot be negative.");

        Service s = new Service(c, vehicle, tech, type, LocalDate.now(), cost);
        services.add(s);
        System.out.println("Service scheduled successfully! " + s);
    }

    /* ---------------- Generate Invoice ---------------- */
    static void generateInvoice() throws InvalidInputException {
        if (services.isEmpty()) {
            System.out.println("No services available.");
            return;
        }

        int id = readInt("Enter Service ID to generate invoice: ");
        Service s = services.stream().filter(sv -> sv.getServiceId() == id).findFirst().orElse(null);
        if (s == null) throw new InvalidInputException("Service ID not found.");

        s.markCompleted();
        Invoice invoice = new Invoice(s);
        String text = invoice.generateInvoiceText();
        System.out.println(text);

        saveToFile(text); // Bonus: File Handling
        System.out.println("Invoice saved to " + FILE_NAME);
    }

    /* ---------------- View Service History ---------------- */
    static void viewServiceHistory() {
        Customer c = selectCustomer();
        if (c == null) return;

        System.out.println("Service history for " + c.getName() + ":");
        boolean found = false;
        for (Service s : services) {
            if (s.getCustomer().getId() == c.getId()) {
                System.out.println(s);
                found = true;
            }
        }
        if (!found) System.out.println("No service history found.");
    }

    /* ---------------- Search Service Records (Bonus) ---------------- */
    static void searchServiceRecords() {
        System.out.print("Search by vehicle number or customer name: ");
        String query = sc.nextLine().trim().toLowerCase();

        boolean found = false;
        for (Service s : services) {
            if (s.getVehicle().getVehicleNumber().toLowerCase().contains(query)
                    || s.getCustomer().getName().toLowerCase().contains(query)) {
                System.out.println(s);
                found = true;
            }
        }
        if (!found) System.out.println("No matching records found.");
    }

    /* ---------------- Sort Customers by Service Date (Bonus) ---------------- */
    static void sortCustomersByServiceDate() {
        Map<Customer, LocalDate> lastServiceMap = new HashMap<>();

        for (Service s : services) {
            LocalDate current = lastServiceMap.get(s.getCustomer());
            if (current == null || s.getDate().isAfter(current)) {
                lastServiceMap.put(s.getCustomer(), s.getDate());
            }
        }

        List<Customer> sortedCustomers = new ArrayList<>(lastServiceMap.keySet());
        sortedCustomers.sort((a, b) -> lastServiceMap.get(b).compareTo(lastServiceMap.get(a))); // most recent first

        System.out.println("Customers sorted by most recent service date:");
        for (Customer c : sortedCustomers) {
            System.out.println(c.getName() + " | Last Service: " + lastServiceMap.get(c));
        }

        if (sortedCustomers.isEmpty()) System.out.println("No service records yet.");
    }

    /* ---------------- File Handling Helper ---------------- */
    static void saveToFile(String text) {
        try (FileWriter fw = new FileWriter(FILE_NAME, true)) {
            fw.write(text + "\n");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    /* ---------------- Utility Methods ---------------- */
    static Customer selectCustomer() {
        if (customers.isEmpty()) {
            System.out.println("No customers registered yet.");
            return null;
        }
        System.out.println("Select customer:");
        for (int i = 0; i < customers.size(); i++) {
            System.out.println((i + 1) + ". " + customers.get(i));
        }
        int idx = readInt("Choice: ") - 1;
        if (idx < 0 || idx >= customers.size()) {
            System.out.println("Invalid selection.");
            return null;
        }
        return customers.get(idx);
    }

    static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int val = Integer.parseInt(sc.nextLine().trim());
                return val;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    static double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}