
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

class InvalidInputException extends Exception {
    public InvalidInputException(String message) { super(message); }
}

class MembershipPlan {
    private String planName;
    private int durationMonths;
    private double price;

    public MembershipPlan(String planName, int durationMonths, double price) {
        this.planName = planName;
        this.durationMonths = durationMonths;
        this.price = price;
    }

    public String getPlanName() { return planName; }
    public int getDurationMonths() { return durationMonths; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return planName + " | " + durationMonths + " month(s) | Rs." + String.format("%.2f", price);
    }
}

class Bill {
    private static int counter = 1;
    private int billId;
    private String memberName;
    private MembershipPlan plan;
    private double amount;
    private LocalDate date;

    static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public Bill(String memberName, MembershipPlan plan) {
        this.billId = counter++;
        this.memberName = memberName;
        this.plan = plan;
        this.amount = plan.getPrice();
        this.date = LocalDate.now();
    }

    public String generateBillText() {
        StringBuilder sb = new StringBuilder();
        sb.append("---------------------------------------\n");
        sb.append("           MEMBERSHIP BILL\n");
        sb.append("---------------------------------------\n");
        sb.append("Bill ID    : ").append(billId).append("\n");
        sb.append("Member     : ").append(memberName).append("\n");
        sb.append("Plan       : ").append(plan.getPlanName()).append(" (").append(plan.getDurationMonths()).append(" month/s)\n");
        sb.append("Date       : ").append(date.format(FMT)).append("\n");
        sb.append(String.format("Amount Due : Rs.%.2f%n", amount));
        sb.append("---------------------------------------\n");
        return sb.toString();
    }

    public int getBillId() { return billId; }
}


class Member {
    private static int counter = 1;
    private int id;
    private String name;
    private String phone;
    private MembershipPlan plan;
    private LocalDate joinDate;
    private LocalDate expiryDate;
    private List<LocalDate> attendance = new ArrayList<>();

    static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public Member(String name, String phone, MembershipPlan plan) {
        this.id = counter++;
        this.name = name;
        this.phone = phone;
        this.plan = plan;
        this.joinDate = LocalDate.now();
        this.expiryDate = joinDate.plusMonths(plan.getDurationMonths());
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public MembershipPlan getPlan() { return plan; }
    public LocalDate getJoinDate() { return joinDate; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public List<LocalDate> getAttendance() { return attendance; }

    public void setPlan(MembershipPlan plan) { this.plan = plan; }

    public void markAttendance(LocalDate date) { attendance.add(date); }

    public void renew(MembershipPlan newPlan) {
        this.plan = newPlan;
        LocalDate base = expiryDate.isAfter(LocalDate.now()) ? expiryDate : LocalDate.now();
        this.expiryDate = base.plusMonths(newPlan.getDurationMonths());
    }

    public boolean isActive() { return !LocalDate.now().isAfter(expiryDate); }

    @Override
    public String toString() {
        return "ID: " + id + " | " + name + " | " + phone
                + " | Plan: " + plan.getPlanName()
                + " | Joined: " + joinDate.format(FMT)
                + " | Expires: " + expiryDate.format(FMT)
                + " | Status: " + (isActive() ? "ACTIVE" : "EXPIRED");
    }
}

public class day28 {

    static Scanner sc = new Scanner(System.in);
    static List<Member> members = new ArrayList<>();
    static List<MembershipPlan> plans = new ArrayList<>();
    static final String LOG_FILE = "gym_log.txt";

    public static void main(String[] args) {
        seedPlans();

        int choice;
        do {
            printMenu();
            choice = readInt("Enter choice: ");

            try {
                switch (choice) {
                    case 1 -> registerMember();
                    case 2 -> managePlans();
                    case 3 -> trackAttendance();
                    case 4 -> generateBill();
                    case 5 -> renewMembership();
                    case 6 -> displayMemberRecords();
                    case 7 -> searchMembers();
                    case 8 -> sortMembers();
                    case 0 -> System.out.println("Goodbye! Stay fit!");
                    default -> System.out.println("Invalid choice. Try again.");
                }
            } catch (InvalidInputException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected Error: " + e.getMessage());
            }

        } while (choice != 0);

        sc.close();
    }

    static void printMenu() {
        System.out.println("\n===== GYM MEMBERSHIP MANAGEMENT SYSTEM =====");
        System.out.println("1. Register Member");
        System.out.println("2. Manage Membership Plans");
        System.out.println("3. Track Attendance");
        System.out.println("4. Generate Membership Bill");
        System.out.println("5. Renewal Management");
        System.out.println("6. Display Member Records");
        System.out.println("7. Search Members");
        System.out.println("8. Sort Members");
        System.out.println("0. Exit");
    }

    static void seedPlans() {
        plans.add(new MembershipPlan("Basic", 1, 1000));
        plans.add(new MembershipPlan("Quarterly", 3, 2700));
        plans.add(new MembershipPlan("Half-Yearly", 6, 5000));
        plans.add(new MembershipPlan("Annual", 12, 9000));
    }

    /* ---------------- Register Member ---------------- */
    static void registerMember() throws InvalidInputException {
        System.out.print("Enter member name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) throw new InvalidInputException("Name cannot be empty.");

        System.out.print("Enter phone number: ");
        String phone = sc.nextLine().trim();
        if (!phone.matches("\\d{10}")) throw new InvalidInputException("Phone must be 10 digits.");

        MembershipPlan plan = choosePlan();
        if (plan == null) return;

        Member m = new Member(name, phone, plan);
        members.add(m);
        log("New member registered: " + m.getName() + " (" + plan.getPlanName() + ")");
        System.out.println("Member registered successfully! " + m);
    }

    /* ---------------- Manage Membership Plans ---------------- */
    static void managePlans() throws InvalidInputException {
        System.out.println("1. View Plans");
        System.out.println("2. Add New Plan");
        int c = readInt("Choice: ");

        if (c == 1) {
            System.out.println("Available Plans:");
            for (MembershipPlan p : plans) System.out.println("- " + p);
        } else if (c == 2) {
            System.out.print("Enter plan name: ");
            String name = sc.nextLine().trim();
            if (name.isEmpty()) throw new InvalidInputException("Plan name cannot be empty.");

            int duration = readInt("Enter duration in months: ");
            if (duration <= 0) throw new InvalidInputException("Duration must be positive.");

            double price = readDouble("Enter price: ");
            if (price < 0) throw new InvalidInputException("Price cannot be negative.");

            plans.add(new MembershipPlan(name, duration, price));
            System.out.println("Plan added successfully!");
        } else {
            System.out.println("Invalid choice.");
        }
    }

    /* ---------------- Track Attendance ---------------- */
    static void trackAttendance() throws InvalidInputException {
        Member m = selectMember();
        if (m == null) return;

        if (!m.isActive()) {
            System.out.println("Warning: Membership expired on " + m.getExpiryDate().format(Member.FMT));
        }

        LocalDate today = LocalDate.now();
        if (m.getAttendance().contains(today)) {
            System.out.println("Attendance already marked for today.");
            return;
        }

        m.markAttendance(today);
        log("Attendance marked: " + m.getName() + " on " + today.format(Member.FMT));
        System.out.println("Attendance marked for " + m.getName() + " on " + today.format(Member.FMT));
        System.out.println("Total visits this membership: " + m.getAttendance().size());
    }

    /* ---------------- Generate Membership Bill ---------------- */
    static void generateBill() throws InvalidInputException {
        Member m = selectMember();
        if (m == null) return;

        Bill bill = new Bill(m.getName(), m.getPlan());
        String text = bill.generateBillText();
        System.out.println(text);

        saveToFile(text);
        System.out.println("Bill saved to gym_bills.txt");
    }

    /* ---------------- Renewal Management ---------------- */
    static void renewMembership() throws InvalidInputException {
        Member m = selectMember();
        if (m == null) return;

        System.out.println("Current plan: " + m.getPlan());
        System.out.println("Current expiry: " + m.getExpiryDate().format(Member.FMT));

        MembershipPlan newPlan = choosePlan();
        if (newPlan == null) return;

        m.renew(newPlan);
        log("Membership renewed: " + m.getName() + " -> " + newPlan.getPlanName());
        System.out.println("Membership renewed successfully!");
        System.out.println("New expiry date: " + m.getExpiryDate().format(Member.FMT));

        Bill bill = new Bill(m.getName(), newPlan);
        saveToFile(bill.generateBillText());
        System.out.println("Renewal bill saved to gym_bills.txt");
    }

    /* ---------------- Display Member Records ---------------- */
    static void displayMemberRecords() {
        if (members.isEmpty()) {
            System.out.println("No members registered yet.");
            return;
        }
        System.out.println("All Member Records:");
        for (Member m : members) {
            System.out.println(m);
        }
    }

    /* ---------------- Search Members (Bonus) ---------------- */
    static void searchMembers() {
        System.out.print("Search by name, phone, or ID: ");
        String query = sc.nextLine().trim().toLowerCase();

        boolean found = false;
        for (Member m : members) {
            if (m.getName().toLowerCase().contains(query)
                    || m.getPhone().contains(query)
                    || String.valueOf(m.getId()).equals(query)) {
                System.out.println(m);
                found = true;
            }
        }
        if (!found) System.out.println("No matching members found.");
    }

    /* ---------------- Sort Members (Bonus) ---------------- */
    static void sortMembers() {
        if (members.isEmpty()) {
            System.out.println("No members registered yet.");
            return;
        }

        System.out.println("Sort by: 1. Name  2. Join Date  3. Expiry Date");
        int c = readInt("Choice: ");

        List<Member> sorted = new ArrayList<>(members);
        switch (c) {
            case 1 -> sorted.sort(Comparator.comparing(Member::getName, String.CASE_INSENSITIVE_ORDER));
            case 2 -> sorted.sort(Comparator.comparing(Member::getJoinDate));
            case 3 -> sorted.sort(Comparator.comparing(Member::getExpiryDate));
            default -> {
                System.out.println("Invalid choice.");
                return;
            }
        }

        System.out.println("Sorted Member List:");
        for (Member m : sorted) System.out.println(m);
    }

    /* ---------------- File Handling Helpers (Bonus) ---------------- */
    static void saveToFile(String text) {
        try (FileWriter fw = new FileWriter("gym_bills.txt", true)) {
            fw.write(text + "\n");
        } catch (IOException e) {
            System.out.println("Error writing bill to file: " + e.getMessage());
        }
    }

    static void log(String message) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true)) {
            fw.write(LocalDate.now().format(Member.FMT) + " - " + message + "\n");
        } catch (IOException e) {
            System.out.println("Error writing to log file: " + e.getMessage());
        }
    }

    /* ---------------- Utility Methods ---------------- */
    static Member selectMember() {
        if (members.isEmpty()) {
            System.out.println("No members registered yet.");
            return null;
        }
        System.out.println("Select member:");
        for (int i = 0; i < members.size(); i++) {
            System.out.println((i + 1) + ". " + members.get(i));
        }
        int idx = readInt("Choice: ") - 1;
        if (idx < 0 || idx >= members.size()) {
            System.out.println("Invalid selection.");
            return null;
        }
        return members.get(idx);
    }

    static MembershipPlan choosePlan() {
        if (plans.isEmpty()) {
            System.out.println("No plans available.");
            return null;
        }
        System.out.println("Select a plan:");
        for (int i = 0; i < plans.size(); i++) {
            System.out.println((i + 1) + ". " + plans.get(i));
        }
        int idx = readInt("Choice: ") - 1;
        if (idx < 0 || idx >= plans.size()) {
            System.out.println("Invalid plan selection.");
            return null;
        }
        return plans.get(idx);
    }

    static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(sc.nextLine().trim());
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