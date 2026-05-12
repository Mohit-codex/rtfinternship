import java.util.ArrayList;
import java.util.Scanner;

class Contact {
    String name;
    String phone;

    Contact(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String toString() {
        return "Name: " + name + " | Phone: " + phone;
    }
}

public class Daysix{

    static ArrayList<Contact> contacts = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;

        System.out.println("===== CONTACT MANAGER =====");

        do {
            System.out.println("\n1. Add Contact");
            System.out.println("2. Display All Contacts");
            System.out.println("3. Search by Name");
            System.out.println("4. Delete Contact");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1: addContact();      break;
                case 2: displayAll();      break;
                case 3: searchContact();   break;
                case 4: deleteContact();   break;
                case 5: System.out.println("Goodbye!"); break;
                default: System.out.println("Invalid choice!");
            }

        } while (choice != 5);
    }

    static void addContact() {
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Phone: ");
        String phone = sc.nextLine();

        contacts.add(new Contact(name, phone));
        System.out.println("Contact added successfully!");
    }
    static void displayAll() {
        if (contacts.isEmpty()) {
            System.out.println("No contacts found.");
            return;
        }
        System.out.println("\n--- All Contacts ---");
        for (int i = 0; i < contacts.size(); i++) {
            System.out.println((i + 1) + ". " + contacts.get(i));
        }
    }

   
    static void searchContact() {
        System.out.print("Enter name to search: ");
        String keyword = sc.nextLine();

        boolean found = false;
        for (Contact c : contacts) {
            if (c.name.equalsIgnoreCase(keyword)) {
                System.out.println("Found: " + c);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Contact not found.");
        }
    }

    static void deleteContact() {
        System.out.print("Enter name to delete: ");
        String name = sc.nextLine();

        boolean removed = false;
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).name.equalsIgnoreCase(name)) {
                contacts.remove(i);
                System.out.println("Contact deleted.");
                removed = true;
                break;
            }
        }
        if (!removed) {
            System.out.println("Contact not found.");
        }
    }
}