import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class BookNotFoundException extends Exception {
    public BookNotFoundException(String message) {
        super(message);
    }
}

class BookNotAvailableException extends Exception {
    public BookNotAvailableException(String message) {
        super(message);
    }
}

class Student {
    private int id;
    private String name;

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Student ID: " + id + " | Name: " + name;
    }
}

class Book {
    private int id;
    private String title;
    private String author;
    private boolean issued;
    private int issuedToId;

    public Book(int id, String title, String author, boolean issued, int issuedToId) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.issued = issued;
        this.issuedToId = issuedToId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isIssued() {
        return issued;
    }

    public void setIssued(boolean issued) {
        this.issued = issued;
    }

    public int getIssuedToId() {
        return issuedToId;
    }

    public void setIssuedToId(int issuedToId) {
        this.issuedToId = issuedToId;
    }

    public String toFileFormat() {
        return id + "," + title + "," + author + "," + issued + "," + issuedToId;
    }

    @Override
    public String toString() {
        String status = issued ? "Issued (Student ID: " + issuedToId + ")" : "Available";
        return "ID: " + id + " | Title: " + title + " | Author: " + author + " | Status: " + status;
    }
}

class Library {
    private ArrayList<Book> books = new ArrayList<>();
    private ArrayList<Student> students = new ArrayList<>();
    private static final String FILE_NAME = "books.txt";

    public Library() {
        loadFromFile();
    }

    public void addBook(int id, String title, String author) {
        books.add(new Book(id, title, author, false, -1));
        saveToFile();
    }

    public void addStudent(int id, String name) {
        students.add(new Student(id, name));
    }

    private Book findById(int id) throws BookNotFoundException {
        for (Book b : books) {
            if (b.getId() == id) {
                return b;
            }
        }
        throw new BookNotFoundException("No book found with ID " + id);
    }

    public void issueBook(int bookId, int studentId) throws BookNotFoundException, BookNotAvailableException {
        Book book = findById(bookId);
        if (book.isIssued()) {
            throw new BookNotAvailableException("Book \"" + book.getTitle() + "\" is already issued.");
        }
        book.setIssued(true);
        book.setIssuedToId(studentId);
        saveToFile();
    }

    public void returnBook(int bookId) throws BookNotFoundException, BookNotAvailableException {
        Book book = findById(bookId);
        if (!book.isIssued()) {
            throw new BookNotAvailableException("Book \"" + book.getTitle() + "\" was not issued.");
        }
        book.setIssued(false);
        book.setIssuedToId(-1);
        saveToFile();
    }

    public void searchByTitle(String title) {
        boolean found = false;
        for (Book b : books) {
            if (b.getTitle().toLowerCase().contains(title.toLowerCase())) {
                System.out.println(b);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No book found with title containing \"" + title + "\"");
        }
    }

    public void searchById(int id) {
        try {
            System.out.println(findById(id));
        } catch (BookNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void displayAvailableBooks() {
        boolean found = false;
        for (Book b : books) {
            if (!b.isIssued()) {
                System.out.println(b);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No available books.");
        }
    }

    public void displayIssuedBooks() {
        boolean found = false;
        for (Book b : books) {
            if (b.isIssued()) {
                System.out.println(b);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No issued books.");
        }
    }

    public void generateReport() {
        int total = books.size();
        int issuedCount = 0;
        for (Book b : books) {
            if (b.isIssued()) {
                issuedCount++;
            }
        }
        int available = total - issuedCount;

        System.out.println("----- LIBRARY REPORT -----");
        System.out.println("Total Books: " + total);
        System.out.println("Issued Books: " + issuedCount);
        System.out.println("Available Books: " + available);
        System.out.println("Total Students Registered: " + students.size());
    }

    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Book b : books) {
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
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String title = parts[1];
                String author = parts[2];
                boolean issued = Boolean.parseBoolean(parts[3]);
                int issuedToId = Integer.parseInt(parts[4]);
                books.add(new Book(id, title, author, issued, issuedToId));
            }
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }
}

public class day22 {
    private static Library library = new Library();
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
                    addBook();
                    break;
                case 2:
                    registerStudent();
                    break;
                case 3:
                    issueBook();
                    break;
                case 4:
                    returnBook();
                    break;
                case 5:
                    searchBook();
                    break;
                case 6:
                    library.displayAvailableBooks();
                    break;
                case 7:
                    library.displayIssuedBooks();
                    break;
                case 8:
                    library.generateReport();
                    break;
                case 9:
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
        System.out.println("===== LIBRARY MANAGEMENT SYSTEM =====");
        System.out.println("1. Add New Book");
        System.out.println("2. Register Student");
        System.out.println("3. Issue Book");
        System.out.println("4. Return Book");
        System.out.println("5. Search Book (Title or ID)");
        System.out.println("6. Display Available Books");
        System.out.println("7. Display Issued Books");
        System.out.println("8. Generate Library Report");
        System.out.println("9. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addBook() {
        try {
            System.out.print("Enter Book ID: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter Title: ");
            String title = scanner.nextLine().trim();
            System.out.print("Enter Author: ");
            String author = scanner.nextLine().trim();

            library.addBook(id, title, author);
            System.out.println("Book added successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Book ID must be numeric.");
        }
    }

    private static void registerStudent() {
        try {
            System.out.print("Enter Student ID: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter Name: ");
            String name = scanner.nextLine().trim();

            library.addStudent(id, name);
            System.out.println("Student registered successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Student ID must be numeric.");
        }
    }

    private static void issueBook() {
        try {
            System.out.print("Enter Book ID: ");
            int bookId = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter Student ID: ");
            int studentId = Integer.parseInt(scanner.nextLine().trim());

            library.issueBook(bookId, studentId);
            System.out.println("Book issued successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. IDs must be numeric.");
        } catch (BookNotFoundException | BookNotAvailableException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void returnBook() {
        try {
            System.out.print("Enter Book ID: ");
            int bookId = Integer.parseInt(scanner.nextLine().trim());

            library.returnBook(bookId);
            System.out.println("Book returned successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Book ID must be numeric.");
        } catch (BookNotFoundException | BookNotAvailableException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void searchBook() {
        System.out.print("Search by (1) Title or (2) ID: ");
        String option = scanner.nextLine().trim();

        if (option.equals("1")) {
            System.out.print("Enter title to search: ");
            String title = scanner.nextLine().trim();
            library.searchByTitle(title);
        } else if (option.equals("2")) {
            try {
                System.out.print("Enter ID to search: ");
                int id = Integer.parseInt(scanner.nextLine().trim());
                library.searchById(id);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. ID must be numeric.");
            }
        } else {
            System.out.println("Invalid option.");
        }
    }
}