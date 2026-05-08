import java.util.*;

class Expense {
    double amount;
    String category;
    String date;

    Expense(double amount, String category, String date) {
        this.amount = amount;
        this.category = category;
        this.date = date;
    }
}

public class Dayfive {
    static List<Expense> expenses = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("\n--- Expense Tracker ---");
            System.out.println("1. Add Expense");
            System.out.println("2. View All Expenses");
            System.out.println("3. Total Spending");
            System.out.println("4. Category-wise Spending");
            System.out.println("5. Highest Expense");
            System.out.println("6. Monthly Summary");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> addExpense();
                case 2 -> viewAll();
                case 3 -> totalSpending();
                case 4 -> categoryWise();
                case 5 -> highestExpense();
                case 6 -> monthlySummary();
                case 7 -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid choice!");
            }
        } while (choice != 7);
    }

    static void addExpense() {
        System.out.print("Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Category: ");
        String category = sc.nextLine();
        System.out.print("Date (dd-mm-yyyy): ");
        String date = sc.nextLine();
        expenses.add(new Expense(amount, category, date));
        System.out.println("Expense Added!");
    }

    static void viewAll() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }
        System.out.println("Amount  | Category  | Date");
        System.out.println("------------------------------");
        for (Expense e : expenses) {
            System.out.println(e.amount + "  | " + e.category + "  | " + e.date);
        }
    }

    static void totalSpending() {
        double total = 0;
        for (Expense e : expenses) total += e.amount;
        System.out.println("Total Spending: " + total);
    }

    static void categoryWise() {
        Map<String, Double> map = new HashMap<>();
        for (Expense e : expenses) {
            map.put(e.category, map.getOrDefault(e.category, 0.0) + e.amount);
        }
        System.out.println("Category-wise Spending:");
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    static void highestExpense() {
        Expense highest = null;
        for (Expense e : expenses) {
            if (highest == null || e.amount > highest.amount)
                highest = e;
        }
        if (highest != null)
            System.out.println("Highest: " + highest.amount + " | " + highest.category + " | " + highest.date);
        else
            System.out.println("No expenses found.");
    }

    static void monthlySummary() {
        Map<String, Double> map = new HashMap<>();
        for (Expense e : expenses) {
            // date format: dd-mm-yyyy, extract mm-yyyy as key
            String month = e.date.substring(3); // "mm-yyyy"
            map.put(month, map.getOrDefault(month, 0.0) + e.amount);
        }
        System.out.println("Monthly Summary:");
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}
