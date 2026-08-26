import java.util.ArrayList;
import java.util.Scanner;
 
class InvalidMarksException extends Exception {
    public InvalidMarksException(String message) {
        super(message);
    }
}
 
class Student {
    private int id;
    private String name;
    private int age;
    private double marks;
 
    public Student(int id, String name, int age, double marks) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.marks = marks;
    }
 
    public int getId() {
        return id;
    }
 
    public void setId(int id) {
        this.id = id;
    }
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public int getAge() {
        return age;
    }
 
    public void setAge(int age) {
        this.age = age;
    }
 
    public double getMarks() {
        return marks;
    }
 
    public void setMarks(double marks) {
        this.marks = marks;
    }
 
    @Override
    public String toString() {
        return "ID: " + id + " | Name: " + name + " | Age: " + age + " | Marks: " + marks;
    }
}
 
public class day21 {
 
    private static ArrayList<Student> students = new ArrayList<>();
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
                    addStudent();
                    break;
                case 2:
                    searchStudent();
                    break;
                case 3:
                    updateStudent();
                    break;
                case 4:
                    deleteStudent();
                    break;
                case 5:
                    displayAllStudents();
                    break;
                case 6:
                    sortByMarks();
                    break;
                case 7:
                    findTopper();
                    break;
                case 8:
                    findAverageMarks();
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
        System.out.println("===== STUDENT MANAGEMENT SYSTEM =====");
        System.out.println("1. Add Student");
        System.out.println("2. Search Student");
        System.out.println("3. Update Student Details");
        System.out.println("4. Delete Student");
        System.out.println("5. Display All Students");
        System.out.println("6. Sort Students by Marks");
        System.out.println("7. Find Topper");
        System.out.println("8. Find Average Marks");
        System.out.println("9. Exit");
        System.out.print("Enter your choice: ");
    }
 
    private static void addStudent() {
        try {
            System.out.print("Enter ID: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
 
            for (Student s : students) {
                if (s.getId() == id) {
                    System.out.println("A student with this ID already exists.");
                    return;
                }
            }
 
            System.out.print("Enter Name: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty.");
            }
 
            System.out.print("Enter Age: ");
            int age = Integer.parseInt(scanner.nextLine().trim());
            if (age <= 0) {
                throw new IllegalArgumentException("Age must be positive.");
            }
 
            System.out.print("Enter Marks: ");
            double marks = Double.parseDouble(scanner.nextLine().trim());
            if (marks < 0 || marks > 100) {
                throw new InvalidMarksException("Marks must be between 0 and 100.");
            }
 
            students.add(new Student(id, name, age, marks));
            System.out.println("Student added successfully.");
 
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. ID, Age and Marks must be numeric.");
        } catch (IllegalArgumentException | InvalidMarksException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
 
    private static Student findById(int id) {
        for (Student s : students) {
            if (s.getId() == id) {
                return s;
            }
        }
        return null;
    }
 
    private static void searchStudent() {
        try {
            System.out.print("Enter ID to search: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
 
            Student found = findById(id);
            if (found != null) {
                System.out.println("Student found: " + found);
            } else {
                System.out.println("No student found with ID " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. ID must be numeric.");
        }
    }
 
    private static void updateStudent() {
        try {
            System.out.print("Enter ID to update: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
 
            Student student = findById(id);
            if (student == null) {
                System.out.println("No student found with ID " + id);
                return;
            }
 
            System.out.print("Enter new Name (leave blank to keep unchanged): ");
            String name = scanner.nextLine().trim();
            if (!name.isEmpty()) {
                student.setName(name);
            }
 
            System.out.print("Enter new Age (enter 0 to keep unchanged): ");
            int age = Integer.parseInt(scanner.nextLine().trim());
            if (age > 0) {
                student.setAge(age);
            }
 
            System.out.print("Enter new Marks (enter -1 to keep unchanged): ");
            double marks = Double.parseDouble(scanner.nextLine().trim());
            if (marks != -1) {
                if (marks < 0 || marks > 100) {
                    throw new InvalidMarksException("Marks must be between 0 and 100.");
                }
                student.setMarks(marks);
            }
 
            System.out.println("Student updated successfully.");
 
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Age and Marks must be numeric.");
        } catch (InvalidMarksException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
 
    private static void deleteStudent() {
        try {
            System.out.print("Enter ID to delete: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
 
            Student student = findById(id);
            if (student == null) {
                System.out.println("No student found with ID " + id);
                return;
            }
 
            students.remove(student);
            System.out.println("Student deleted successfully.");
 
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. ID must be numeric.");
        }
    }
 
    private static void displayAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No student records available.");
            return;
        }
 
        System.out.println("----- All Students -----");
        for (Student s : students) {
            System.out.println(s);
        }
    }
 
    private static void sortByMarks() {
        if (students.isEmpty()) {
            System.out.println("No student records available.");
            return;
        }
 
        students.sort((a, b) -> Double.compare(b.getMarks(), a.getMarks()));
        System.out.println("Students sorted by marks (highest first):");
        displayAllStudents();
    }
 
    private static void findTopper() {
        if (students.isEmpty()) {
            System.out.println("No student records available.");
            return;
        }
 
        Student topper = students.get(0);
        for (Student s : students) {
            if (s.getMarks() > topper.getMarks()) {
                topper = s;
            }
        }
 
        System.out.println("Topper: " + topper);
    }
 
    private static void findAverageMarks() {
        if (students.isEmpty()) {
            System.out.println("No student records available.");
            return;
        }
 
        double total = 0;
        for (Student s : students) {
            total += s.getMarks();
        }
 
        double average = total / students.size();
        System.out.println("Average Marks: " + average);
    }
}