import java.util.*;

class Product {
    String name;
    double price;
    int quantity;

    Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String toString() {
        return "Name: " + name + " | Price: " + price + " | Quantity: " + quantity;
    }
}

public class Day10 {
    static HashMap<String, Product> inventory = new HashMap<>();

    static void addProduct(String name, double price, int quantity) {
        inventory.put(name, new Product(name, price, quantity));
        System.out.println("Product added: " + name);
    }

    static void updateQuantity(String name, int quantity) {
        if (inventory.containsKey(name)) {
            inventory.get(name).quantity = quantity;
            System.out.println("Updated quantity of " + name + " to " + quantity);
        } else {
            System.out.println("Product not found: " + name);
        }
    }

    static void searchProduct(String name) {
        if (inventory.containsKey(name)) {
            System.out.println("Found -> " + inventory.get(name));
        } else {
            System.out.println("Product not found: " + name);
        }
    }

    static void totalInventoryValue() {
        double total = 0;
        for (Product p : inventory.values()) {
            total += p.price * p.quantity;
        }
        System.out.println("Total Inventory Value: Rs." + total);
    }

    static void lowStockAlert(int threshold) {
        System.out.println("Low Stock Products (quantity < " + threshold + "):");
        for (Product p : inventory.values()) {
            if (p.quantity < threshold) {
                System.out.println("  -> " + p.name + " | Quantity: " + p.quantity);
            }
        }
    }

    static void removeProduct(String name) {
        if (inventory.containsKey(name)) {
            inventory.remove(name);
            System.out.println("Removed product: " + name);
        } else {
            System.out.println("Product not found: " + name);
        }
    }

    static void displayAll() {
        System.out.println("===== FULL INVENTORY =====");
        for (Product p : inventory.values()) {
            System.out.println(p);
        }
    }

    public static void main(String[] args) {
        addProduct("Laptop", 55000, 10);
        addProduct("Mouse", 500, 3);
        addProduct("Keyboard", 1200, 25);
        addProduct("Monitor", 12000, 2);
        addProduct("USB Hub", 800, 1);

        System.out.println();
        displayAll();

        System.out.println();
        updateQuantity("Mouse", 15);

        System.out.println();
        searchProduct("Keyboard");
        searchProduct("Headphones");

        System.out.println();
        totalInventoryValue();

        System.out.println();
        lowStockAlert(5);

        System.out.println();
        removeProduct("USB Hub");

        System.out.println();
        displayAll();
    }
}
