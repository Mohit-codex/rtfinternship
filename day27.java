
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


class InvalidInputException extends Exception {
    public InvalidInputException(String message) { super(message); }
}

class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) { super(message); }
}


class Transaction {
    private static int counter = 1;
    private int transactionId;
    private String type;      // DEPOSIT, WITHDRAW, TRANSFER-OUT, TRANSFER-IN
    private double amount;
    private double balanceAfter;
    private LocalDateTime timestamp;
    private String remarks;

    static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public Transaction(String type, double amount, double balanceAfter, String remarks) {
        this.transactionId = counter++;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = LocalDateTime.now();
        this.remarks = remarks;
    }

    public int getTransactionId() { return transactionId; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public double getBalanceAfter() { return balanceAfter; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "TXN#" + transactionId + " | " + timestamp.format(FMT) + " | " + type
                + " | Rs." + String.format("%.2f", amount)
                + " | Balance: Rs." + String.format("%.2f", balanceAfter)
                + " | " + remarks;
    }
}


class Account {
    private static int counter = 100001;
    private String accountNumber;
    private String accountType; // Savings, Current
    private double balance;
    private double interestRate; // annual %, used for Savings
    private List<Transaction> transactions = new ArrayList<>();

    public Account(String accountType, double openingBalance, double interestRate) {
        this.accountNumber = "AC" + (counter++);
        this.accountType = accountType;
        this.balance = openingBalance;
        this.interestRate = interestRate;
        if (openingBalance > 0) {
            transactions.add(new Transaction("DEPOSIT", openingBalance, balance, "Opening balance"));
        }
    }

    public String getAccountNumber() { return accountNumber; }
    public String getAccountType() { return accountType; }
    public double getBalance() { return balance; }
    public double getInterestRate() { return interestRate; }
    public List<Transaction> getTransactions() { return transactions; }

    public void deposit(double amount, String remarks) {
        balance += amount;
        transactions.add(new Transaction("DEPOSIT", amount, balance, remarks));
    }

    public void withdraw(double amount, String remarks) throws InsufficientFundsException {
        if (amount > balance) {
            throw new InsufficientFundsException("Insufficient balance in account " + accountNumber);
        }
        balance -= amount;
        transactions.add(new Transaction("WITHDRAW", amount, balance, remarks));
    }

    @Override
    public String toString() {
        return accountNumber + " (" + accountType + ") | Balance: Rs." + String.format("%.2f", balance);
    }
}


class Customer {
    private static int counter = 1;
    private int customerId;
    private String name;
    private String phone;
    private List<Account> accounts = new ArrayList<>();

    public Customer(String name, String phone) {
        this.customerId = counter++;
        this.name = name;
        this.phone = phone;
    }

    public int getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public List<Account> getAccounts() { return accounts; }

    public void addAccount(Account acc) { accounts.add(acc); }

    @Override
    public String toString() {
        return "ID: " + customerId + " | " + name + " | " + phone + " | Accounts: " + accounts.size();
    }
}

public class day27 {

    static Scanner sc = new Scanner(System.in);
    static List<Customer> customers = new ArrayList<>();
    static final String FILE_NAME = "transaction_log.txt";

    public static void main(String[] args) {
        int choice;
        do {
            printMenu();
            choice = readInt("Enter choice: ");

            try {
                switch (choice) {
                    case 1 -> registerCustomer();
                    case 2 -> openAccount();
                    case 3 -> fundTransfer();
                    case 4 -> miniStatement();
                    case 5 -> transactionHistory();
                    case 6 -> accountSummary();
                    case 7 -> interestCalculator();
                    case 8 -> searchByAccountNumber();
                    case 0 -> System.out.println("Thank you for banking with us!");
                    default -> System.out.println("Invalid choice. Try again.");
                }
            } catch (InvalidInputException | InsufficientFundsException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected Error: " + e.getMessage());
            }

        } while (choice != 0);

        sc.close();
    }

    static void printMenu() {
        System.out.println("\n===== DIGITAL BANKING DASHBOARD =====");
        System.out.println("1. Customer Registration");
        System.out.println("2. Account Management (Open New Account)");
        System.out.println("3. Fund Transfer");
        System.out.println("4. Mini Statement (Last 5 Transactions)");
        System.out.println("5. Transaction History (Full)");
        System.out.println("6. Account Summary");
        System.out.println("7. Interest Calculator");
        System.out.println("8. Search Customer by Account Number");
        System.out.println("0. Exit");
    }

    /* ---------------- Customer Registration ---------------- */
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

    /* ---------------- Account Management ---------------- */
    static void openAccount() throws InvalidInputException {
        Customer c = selectCustomer();
        if (c == null) return;

        System.out.print("Account type (Savings/Current): ");
        String type = sc.nextLine().trim();
        if (type.isEmpty()) throw new InvalidInputException("Account type cannot be empty.");

        double opening = readDouble("Enter opening balance: ");
        if (opening < 0) throw new InvalidInputException("Opening balance cannot be negative.");

        double rate = type.equalsIgnoreCase("Savings") ? 4.0 : 0.0; // default interest rate

        Account acc = new Account(type, opening, rate);
        c.addAccount(acc);
        log("Account opened: " + acc.getAccountNumber() + " for " + c.getName());
        System.out.println("Account created successfully! " + acc);
    }

    /* ---------------- Fund Transfer ---------------- */
    static void fundTransfer() throws InvalidInputException, InsufficientFundsException {
        System.out.print("Enter SOURCE account number: ");
        String fromNum = sc.nextLine().trim();
        Account from = findAccount(fromNum);
        if (from == null) throw new InvalidInputException("Source account not found.");

        System.out.print("Enter DESTINATION account number: ");
        String toNum = sc.nextLine().trim();
        Account to = findAccount(toNum);
        if (to == null) throw new InvalidInputException("Destination account not found.");

        if (from == to) throw new InvalidInputException("Source and destination cannot be the same.");

        double amount = readDouble("Enter amount to transfer: ");
        if (amount <= 0) throw new InvalidInputException("Amount must be positive.");

        from.withdraw(amount, "Transfer to " + to.getAccountNumber());
        to.deposit(amount, "Transfer from " + from.getAccountNumber());

        log("Transfer: Rs." + amount + " from " + from.getAccountNumber() + " to " + to.getAccountNumber());
        System.out.println("Transfer successful!");
        System.out.println("New balance (" + from.getAccountNumber() + "): Rs." + String.format("%.2f", from.getBalance()));
    }

    /* ---------------- Mini Statement ---------------- */
    static void miniStatement() throws InvalidInputException {
        Account acc = selectAccount();
        if (acc == null) return;

        List<Transaction> txns = acc.getTransactions();
        System.out.println("Mini Statement for " + acc.getAccountNumber() + " (last 5 transactions):");
        int start = Math.max(0, txns.size() - 5);
        for (int i = txns.size() - 1; i >= start; i--) {
            System.out.println(txns.get(i));
        }
        if (txns.isEmpty()) System.out.println("No transactions yet.");
    }

    /* ---------------- Transaction History ---------------- */
    static void transactionHistory() throws InvalidInputException {
        Account acc = selectAccount();
        if (acc == null) return;

        System.out.println("Full Transaction History for " + acc.getAccountNumber() + ":");
        if (acc.getTransactions().isEmpty()) {
            System.out.println("No transactions yet.");
        }
        for (Transaction t : acc.getTransactions()) {
            System.out.println(t);
        }
    }

    /* ---------------- Account Summary ---------------- */
    static void accountSummary() throws InvalidInputException {
        Customer c = selectCustomer();
        if (c == null) return;

        System.out.println("Account Summary for " + c.getName() + ":");
        if (c.getAccounts().isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }
        double total = 0;
        for (Account a : c.getAccounts()) {
            System.out.println(a + " | Transactions: " + a.getTransactions().size());
            total += a.getBalance();
        }
        System.out.println(String.format("Total balance across all accounts: Rs.%.2f", total));
    }

    /* ---------------- Interest Calculator (Bonus) ---------------- */
    static void interestCalculator() throws InvalidInputException {
        Account acc = selectAccount();
        if (acc == null) return;

        if (acc.getInterestRate() <= 0) {
            System.out.println("This account type does not earn interest.");
            return;
        }

        int months = readInt("Enter number of months to calculate interest for: ");
        if (months <= 0) throw new InvalidInputException("Months must be positive.");

        double interest = acc.getBalance() * (acc.getInterestRate() / 100.0) * (months / 12.0);
        System.out.println(String.format("Estimated interest @ %.1f%% p.a. for %d month(s): Rs.%.2f",
                acc.getInterestRate(), months, interest));
        System.out.println(String.format("Projected balance: Rs.%.2f", acc.getBalance() + interest));
    }

    /* ---------------- Search Customer by Account Number (Bonus) ---------------- */
    static void searchByAccountNumber() {
        System.out.print("Enter account number to search: ");
        String num = sc.nextLine().trim();

        for (Customer c : customers) {
            for (Account a : c.getAccounts()) {
                if (a.getAccountNumber().equalsIgnoreCase(num)) {
                    System.out.println("Match found!");
                    System.out.println("Customer: " + c);
                    System.out.println("Account : " + a);
                    return;
                }
            }
        }
        System.out.println("No customer found with account number: " + num);
    }

    /* ---------------- File Handling Helper (Bonus) ---------------- */
    static void log(String message) {
        try (FileWriter fw = new FileWriter(FILE_NAME, true)) {
            fw.write(LocalDateTime.now().format(Transaction.FMT) + " - " + message + "\n");
        } catch (IOException e) {
            System.out.println("Error writing to log file: " + e.getMessage());
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

    static Account selectAccount() throws InvalidInputException {
        Customer c = selectCustomer();
        if (c == null) return null;

        if (c.getAccounts().isEmpty()) {
            System.out.println("This customer has no accounts.");
            return null;
        }

        System.out.println("Select account:");
        for (int i = 0; i < c.getAccounts().size(); i++) {
            System.out.println((i + 1) + ". " + c.getAccounts().get(i));
        }
        int idx = readInt("Choice: ") - 1;
        if (idx < 0 || idx >= c.getAccounts().size())
            throw new InvalidInputException("Invalid account selection.");
        return c.getAccounts().get(idx);
    }

    static Account findAccount(String accountNumber) {
        for (Customer c : customers) {
            for (Account a : c.getAccounts()) {
                if (a.getAccountNumber().equalsIgnoreCase(accountNumber)) return a;
            }
        }
        return null;
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