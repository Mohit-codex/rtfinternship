import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

class RoomNotAvailableException extends Exception {
    public RoomNotAvailableException(String message) {
        super(message);
    }
}

class BookingNotFoundException extends Exception {
    public BookingNotFoundException(String message) {
        super(message);
    }
}

class Room {
    private int roomNo;
    private String type;
    private double pricePerNight;
    private boolean available;

    public Room(int roomNo, String type, double pricePerNight, boolean available) {
        this.roomNo = roomNo;
        this.type = type;
        this.pricePerNight = pricePerNight;
        this.available = available;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public String getType() {
        return type;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        String status = available ? "Available" : "Occupied";
        return "Room " + roomNo + " | Type: " + type + " | Price/Night: " + pricePerNight + " | " + status;
    }
}

class Customer {
    private int id;
    private String name;
    private String phone;

    public Customer(int id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return "Customer ID: " + id + " | Name: " + name + " | Phone: " + phone;
    }
}

class Booking {
    private int bookingId;
    private int roomNo;
    private int customerId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private boolean checkedIn;
    private boolean checkedOut;
    private double bill;

    public Booking(int bookingId, int roomNo, int customerId, LocalDate checkInDate,
                   LocalDate checkOutDate, boolean checkedIn, boolean checkedOut, double bill) {
        this.bookingId = bookingId;
        this.roomNo = roomNo;
        this.customerId = customerId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.checkedIn = checkedIn;
        this.checkedOut = checkedOut;
        this.bill = bill;
    }

    public int getBookingId() {
        return bookingId;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public int getCustomerId() {
        return customerId;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public boolean isCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
    }

    public boolean isCheckedOut() {
        return checkedOut;
    }

    public void setCheckedOut(boolean checkedOut) {
        this.checkedOut = checkedOut;
    }

    public double getBill() {
        return bill;
    }

    public void setBill(double bill) {
        this.bill = bill;
    }

    public String toFileFormat() {
        String checkOutStr = (checkOutDate == null) ? "NULL" : checkOutDate.toString();
        return bookingId + "," + roomNo + "," + customerId + "," + checkInDate + "," +
                checkOutStr + "," + checkedIn + "," + checkedOut + "," + bill;
    }

    @Override
    public String toString() {
        String checkOutStr = (checkOutDate == null) ? "-" : checkOutDate.toString();
        return "Booking ID: " + bookingId + " | Room: " + roomNo + " | Customer ID: " + customerId +
                " | Check-in: " + checkInDate + " | Check-out: " + checkOutStr +
                " | CheckedIn: " + checkedIn + " | CheckedOut: " + checkedOut + " | Bill: " + bill;
    }
}

class Hotel {
    private ArrayList<Room> rooms = new ArrayList<>();
    private ArrayList<Customer> customers = new ArrayList<>();
    private ArrayList<Booking> bookings = new ArrayList<>();
    private int nextBookingId = 1;
    private static final String FILE_NAME = "bookings.txt";

    public Hotel() {
        loadFromFile();
    }

    public void addRoom(int roomNo, String type, double price) {
        rooms.add(new Room(roomNo, type, price, true));
    }

    public void addCustomer(int id, String name, String phone) {
        customers.add(new Customer(id, name, phone));
    }

    private Room findRoom(int roomNo) throws RoomNotAvailableException {
        for (Room r : rooms) {
            if (r.getRoomNo() == roomNo) {
                return r;
            }
        }
        throw new RoomNotAvailableException("No room found with number " + roomNo);
    }

    private Customer findCustomer(int customerId) {
        for (Customer c : customers) {
            if (c.getId() == customerId) {
                return c;
            }
        }
        return null;
    }

    private Booking findBookingById(int bookingId) throws BookingNotFoundException {
        for (Booking b : bookings) {
            if (b.getBookingId() == bookingId) {
                return b;
            }
        }
        throw new BookingNotFoundException("No booking found with ID " + bookingId);
    }

    public int bookRoom(int roomNo, int customerId) throws RoomNotAvailableException {
        Room room = findRoom(roomNo);
        if (!room.isAvailable()) {
            throw new RoomNotAvailableException("Room " + roomNo + " is not available.");
        }
        if (findCustomer(customerId) == null) {
            throw new RoomNotAvailableException("No customer found with ID " + customerId);
        }

        room.setAvailable(false);
        Booking booking = new Booking(nextBookingId++, roomNo, customerId,
                LocalDate.now(), null, false, false, 0.0);
        bookings.add(booking);
        saveToFile();
        return booking.getBookingId();
    }

    public void checkIn(int bookingId) throws BookingNotFoundException {
        Booking booking = findBookingById(bookingId);
        if (booking.isCheckedIn()) {
            System.out.println("Booking " + bookingId + " is already checked in.");
            return;
        }
        booking.setCheckedIn(true);
        saveToFile();
    }

    public double checkOut(int bookingId) throws BookingNotFoundException, RoomNotAvailableException {
        Booking booking = findBookingById(bookingId);

        if (!booking.isCheckedIn()) {
            throw new RoomNotAvailableException("Booking " + bookingId + " has not checked in yet.");
        }
        if (booking.isCheckedOut()) {
            throw new RoomNotAvailableException("Booking " + bookingId + " is already checked out.");
        }

        Room room = findRoom(booking.getRoomNo());
        LocalDate checkOutDate = LocalDate.now();
        long nights = ChronoUnit.DAYS.between(booking.getCheckInDate(), checkOutDate);
        if (nights <= 0) {
            nights = 1;
        }

        double bill = nights * room.getPricePerNight();

        booking.setCheckOutDate(checkOutDate);
        booking.setCheckedOut(true);
        booking.setBill(bill);
        room.setAvailable(true);

        saveToFile();
        return bill;
    }

    public void displayAvailableRooms() {
        boolean found = false;
        for (Room r : rooms) {
            if (r.isAvailable()) {
                System.out.println(r);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No rooms available.");
        }
    }

    public void displayAllRooms() {
        if (rooms.isEmpty()) {
            System.out.println("No rooms in the hotel.");
            return;
        }
        for (Room r : rooms) {
            System.out.println(r);
        }
    }

    public void sortRoomsByPrice() {
        rooms.sort((a, b) -> Double.compare(a.getPricePerNight(), b.getPricePerNight()));
        System.out.println("Rooms sorted by price (lowest first):");
        displayAllRooms();
    }

    public void searchBookingById(int bookingId) {
        try {
            System.out.println(findBookingById(bookingId));
        } catch (BookingNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void searchBookingsByCustomer(int customerId) {
        boolean found = false;
        for (Booking b : bookings) {
            if (b.getCustomerId() == customerId) {
                System.out.println(b);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No bookings found for customer ID " + customerId);
        }
    }

    public void generateBill(int bookingId) throws BookingNotFoundException {
        Booking booking = findBookingById(bookingId);
        if (!booking.isCheckedOut()) {
            System.out.println("Bill not finalized yet. Please check out first.");
            return;
        }

        Customer customer = findCustomer(booking.getCustomerId());
        System.out.println("----- HOTEL BILL -----");
        System.out.println("Booking ID: " + booking.getBookingId());
        System.out.println("Room No: " + booking.getRoomNo());
        System.out.println("Customer: " + (customer == null ? "Unknown" : customer.getName()));
        System.out.println("Check-in: " + booking.getCheckInDate());
        System.out.println("Check-out: " + booking.getCheckOutDate());
        System.out.println("Total Amount: " + booking.getBill());
    }

    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Booking b : bookings) {
                writer.println(b.toFileFormat());
            }
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int maxId = 0;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split(",");
                int bookingId = Integer.parseInt(parts[0]);
                int roomNo = Integer.parseInt(parts[1]);
                int customerId = Integer.parseInt(parts[2]);
                LocalDate checkIn = LocalDate.parse(parts[3]);
                LocalDate checkOut = parts[4].equals("NULL") ? null : LocalDate.parse(parts[4]);
                boolean checkedIn = Boolean.parseBoolean(parts[5]);
                boolean checkedOut = Boolean.parseBoolean(parts[6]);
                double bill = Double.parseDouble(parts[7]);

                bookings.add(new Booking(bookingId, roomNo, customerId, checkIn, checkOut,
                        checkedIn, checkedOut, bill));

                if (bookingId > maxId) {
                    maxId = bookingId;
                }
            }
            nextBookingId = maxId + 1;
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }
}

public class day23 {
    private static Hotel hotel = new Hotel();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            printMenu();
            int choice;

            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    addRoom();
                    break;
                case 2:
                    addCustomer();
                    break;
                case 3:
                    bookRoom();
                    break;
                case 4:
                    checkIn();
                    break;
                case 5:
                    checkOut();
                    break;
                case 6:
                    hotel.displayAvailableRooms();
                    break;
                case 7:
                    hotel.sortRoomsByPrice();
                    break;
                case 8:
                    searchBooking();
                    break;
                case 9:
                    generateBill();
                    break;
                case 10:
                    running = false;
                    System.out.println("Exiting program.");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("===== HOTEL MANAGEMENT SYSTEM =====");
        System.out.println("1. Add Room");
        System.out.println("2. Add Customer");
        System.out.println("3. Book Room");
        System.out.println("4. Check-in");
        System.out.println("5. Check-out");
        System.out.println("6. Display Available Rooms");
        System.out.println("7. Sort Rooms by Price");
        System.out.println("8. Search Booking (by ID or Customer)");
        System.out.println("9. Generate Bill");
        System.out.println("10. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addRoom() {
        try {
            System.out.print("Enter Room No: ");
            int roomNo = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter Room Type: ");
            String type = scanner.nextLine().trim();
            System.out.print("Enter Price per Night: ");
            double price = Double.parseDouble(scanner.nextLine().trim());

            hotel.addRoom(roomNo, type, price);
            System.out.println("Room added successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Room No and Price must be numeric.");
        }
    }

    private static void addCustomer() {
        try {
            System.out.print("Enter Customer ID: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter Name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Enter Phone: ");
            String phone = scanner.nextLine().trim();

            hotel.addCustomer(id, name, phone);
            System.out.println("Customer added successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Customer ID must be numeric.");
        }
    }

    private static void bookRoom() {
        try {
            System.out.print("Enter Room No: ");
            int roomNo = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter Customer ID: ");
            int customerId = Integer.parseInt(scanner.nextLine().trim());

            int bookingId = hotel.bookRoom(roomNo, customerId);
            System.out.println("Room booked successfully. Booking ID: " + bookingId);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. IDs must be numeric.");
        } catch (RoomNotAvailableException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void checkIn() {
        try {
            System.out.print("Enter Booking ID: ");
            int bookingId = Integer.parseInt(scanner.nextLine().trim());

            hotel.checkIn(bookingId);
            System.out.println("Check-in successful.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Booking ID must be numeric.");
        } catch (BookingNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void checkOut() {
        try {
            System.out.print("Enter Booking ID: ");
            int bookingId = Integer.parseInt(scanner.nextLine().trim());

            double bill = hotel.checkOut(bookingId);
            System.out.println("Check-out successful. Total Bill: " + bill);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Booking ID must be numeric.");
        } catch (BookingNotFoundException | RoomNotAvailableException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void searchBooking() {
        System.out.print("Search by (1) Booking ID or (2) Customer ID: ");
        String option = scanner.nextLine().trim();

        try {
            if (option.equals("1")) {
                System.out.print("Enter Booking ID: ");
                int bookingId = Integer.parseInt(scanner.nextLine().trim());
                hotel.searchBookingById(bookingId);
            } else if (option.equals("2")) {
                System.out.print("Enter Customer ID: ");
                int customerId = Integer.parseInt(scanner.nextLine().trim());
                hotel.searchBookingsByCustomer(customerId);
            } else {
                System.out.println("Invalid option.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. ID must be numeric.");
        }
    }

    private static void generateBill() {
        try {
            System.out.print("Enter Booking ID: ");
            int bookingId = Integer.parseInt(scanner.nextLine().trim());
            hotel.generateBill(bookingId);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Booking ID must be numeric.");
        } catch (BookingNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}