
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class ProductNotFoundException extends Exception {
    public ProductNotFoundException(String message) {
        super(message);
    }
}

class InvalidQuantityException extends Exception {
    public InvalidQuantityException(String message) {
        super(message);
    }
}

class Product {
    private int id;
    private String name;
    private double price;
    private int stock;

    public Product(int id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Name: " + name + " | Price: " + price + " | Stock: " + stock;
    }
}

class Customer {
    private int id;
    private String name;
    private String email;

    public Customer(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Customer ID: " + id + " | Name: " + name + " | Email: " + email;
    }
}

class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return product.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return product.getName() + " x " + quantity + " = " + getSubtotal();
    }
}

class Cart {
    private ArrayList<CartItem> items = new ArrayList<>();

    public void addProduct(Product product, int quantity) throws InvalidQuantityException {
        if (quantity <= 0) {
            throw new InvalidQuantityException("Quantity must be greater than zero.");
        }
        if (quantity > product.getStock()) {
            throw new InvalidQuantityException("Not enough stock for " + product.getName() +
                    ". Available: " + product.getStock());
        }

        for (CartItem item : items) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new CartItem(product, quantity));
    }

    public void removeProduct(int productId) throws ProductNotFoundException {
        CartItem toRemove = findItem(productId);
        items.remove(toRemove);
    }

    public void updateQuantity(int productId, int quantity) throws ProductNotFoundException, InvalidQuantityException {
        CartItem item = findItem(productId);
        if (quantity <= 0) {
            throw new InvalidQuantityException("Quantity must be greater than zero.");
        }
        if (quantity > item.getProduct().getStock()) {
            throw new InvalidQuantityException("Not enough stock. Available: " + item.getProduct().getStock());
        }
        item.setQuantity(quantity);
    }

    private CartItem findItem(int productId) throws ProductNotFoundException {
        for (CartItem item : items) {
            if (item.getProduct().getId() == productId) {
                return item;
            }
        }
        throw new ProductNotFoundException("Product not in cart: ID " + productId);
    }

    public ArrayList<CartItem> getItems() {
        return items;
    }

    public double getTotal() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public void clear() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}

class Order {
    private int orderId;
    private int customerId;
    private double totalAmount;
    private double discount;
    private double finalAmount;

    public Order(int orderId, int customerId, double totalAmount, double discount, double finalAmount) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.discount = discount;
        this.finalAmount = finalAmount;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double getDiscount() {
        return discount;
    }

    public double getFinalAmount() {
        return finalAmount;
    }

    public String toFileFormat() {
        return orderId + "," + customerId + "," + totalAmount + "," + discount + "," + finalAmount;
    }

    @Override
    public String toString() {
        return "Order ID: " + orderId + " | Customer ID: " + customerId +
                " | Total: " + totalAmount + " | Discount: " + discount + " | Final: " + finalAmount;
    }
}

public class day24 {

    private static ArrayList<Product> products = new ArrayList<>();
    private static ArrayList<Customer> customers = new ArrayList<>();
    private static ArrayList<Order> orders = new ArrayList<>();
    private static Cart cart = new Cart();
    private static Scanner scanner = new Scanner(System.in);
    private static int nextOrderId = 1;
    private static final String FILE_NAME = "orders.txt";

    public static void main(String[] args) {
        loadOrdersFromFile();

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
                    addProductCatalog();
                    break;
                case 2:
                    addCustomer();
                    break;
                case 3:
                    addToCart();
                    break;
                case 4:
                    removeFromCart();
                    break;
                case 5:
                    updateCartQuantity();
                    break;
                case 6:
                    viewCart();
                    break;
                case 7:
                    checkout();
                    break;
                case 8:
                    searchProduct();
                    break;
                case 9:
                    sortProductsByPrice();
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
        System.out.println("===== ONLINE SHOPPING CART SYSTEM =====");
        System.out.println("1. Add Product (catalog)");
        System.out.println("2. Add Customer");
        System.out.println("3. Add Product to Cart");
        System.out.println("4. Remove Product from Cart");
        System.out.println("5. Update Cart Quantity");
        System.out.println("6. View Cart");
        System.out.println("7. Checkout (Generate Invoice)");
        System.out.println("8. Search Product");
        System.out.println("9. Sort Products by Price");
        System.out.println("10. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addProductCatalog() {
        try {
            System.out.print("Enter Product ID: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter Name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Enter Price: ");
            double price = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Enter Stock: ");
            int stock = Integer.parseInt(scanner.nextLine().trim());

            products.add(new Product(id, name, price, stock));
            System.out.println("Product added successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. ID, Price and Stock must be numeric.");
        }
    }

    private static void addCustomer() {
        try {
            System.out.print("Enter Customer ID: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter Name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Enter Email: ");
            String email = scanner.nextLine().trim();

            customers.add(new Customer(id, name, email));
            System.out.println("Customer added successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Customer ID must be numeric.");
        }
    }

    private static Product findProductById(int id) {
        for (Product p : products) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    private static void addToCart() {
        try {
            System.out.print("Enter Product ID: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            Product product = findProductById(id);
            if (product == null) {
                System.out.println("No product found with ID " + id);
                return;
            }

            System.out.print("Enter Quantity: ");
            int qty = Integer.parseInt(scanner.nextLine().trim());

            cart.addProduct(product, qty);
            System.out.println("Product added to cart.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. ID and Quantity must be numeric.");
        } catch (InvalidQuantityException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void removeFromCart() {
        try {
            System.out.print("Enter Product ID to remove: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            cart.removeProduct(id);
            System.out.println("Product removed from cart.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. ID must be numeric.");
        } catch (ProductNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void updateCartQuantity() {
        try {
            System.out.print("Enter Product ID: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter New Quantity: ");
            int qty = Integer.parseInt(scanner.nextLine().trim());

            cart.updateQuantity(id, qty);
            System.out.println("Cart updated.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. ID and Quantity must be numeric.");
        } catch (ProductNotFoundException | InvalidQuantityException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewCart() {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }
        for (CartItem item : cart.getItems()) {
            System.out.println(item);
        }
        System.out.println("Total: " + cart.getTotal());
    }

    private static void checkout() {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty. Nothing to checkout.");
            return;
        }

        try {
            System.out.print("Enter Customer ID: ");
            int customerId = Integer.parseInt(scanner.nextLine().trim());

            Customer customer = null;
            for (Customer c : customers) {
                if (c.getId() == customerId) {
                    customer = c;
                    break;
                }
            }
            if (customer == null) {
                System.out.println("No customer found with ID " + customerId);
                return;
            }

            double total = cart.getTotal();
            double discount = 0;

            System.out.print("Enter Coupon Code (or press Enter to skip): ");
            String coupon = scanner.nextLine().trim();
            discount = applyCoupon(coupon, total);

            double finalAmount = total - discount;

            // reduce stock for purchased items
            for (CartItem item : cart.getItems()) {
                Product p = item.getProduct();
                p.setStock(p.getStock() - item.getQuantity());
            }

            Order order = new Order(nextOrderId++, customerId, total, discount, finalAmount);
            orders.add(order);
            saveOrderToFile(order);

            printInvoice(customer, order);

            cart.clear();
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Customer ID must be numeric.");
        }
    }

    private static double applyCoupon(String coupon, double total) {
        if (coupon.equalsIgnoreCase("SAVE10")) {
            System.out.println("Coupon applied: 10% off");
            return total * 0.10;
        } else if (coupon.equalsIgnoreCase("SAVE20")) {
            System.out.println("Coupon applied: 20% off");
            return total * 0.20;
        } else if (coupon.isEmpty()) {
            return 0;
        } else {
            System.out.println("Invalid coupon code. No discount applied.");
            return 0;
        }
    }

    private static void printInvoice(Customer customer, Order order) {
        System.out.println();
        System.out.println("----- INVOICE -----");
        System.out.println("Order ID: " + order.getOrderId());
        System.out.println("Customer: " + customer.getName() + " (" + customer.getEmail() + ")");
        System.out.println("--------------------");
        for (CartItem item : cart.getItems()) {
            System.out.println(item);
        }
        System.out.println("--------------------");
        System.out.println("Subtotal: " + order.getTotalAmount());
        System.out.println("Discount: " + order.getDiscount());
        System.out.println("Total Payable: " + order.getFinalAmount());
        System.out.println("--------------------");
        System.out.println("Thank you for shopping with us!");
    }

    private static void searchProduct() {
        System.out.print("Search by (1) ID or (2) Name: ");
        String option = scanner.nextLine().trim();

        if (option.equals("1")) {
            try {
                System.out.print("Enter Product ID: ");
                int id = Integer.parseInt(scanner.nextLine().trim());
                Product product = findProductById(id);
                if (product != null) {
                    System.out.println(product);
                } else {
                    System.out.println("No product found with ID " + id);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. ID must be numeric.");
            }
        } else if (option.equals("2")) {
            System.out.print("Enter name to search: ");
            String name = scanner.nextLine().trim();
            boolean found = false;
            for (Product p : products) {
                if (p.getName().toLowerCase().contains(name.toLowerCase())) {
                    System.out.println(p);
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No product found with name containing \"" + name + "\"");
            }
        } else {
            System.out.println("Invalid option.");
        }
    }

    private static void sortProductsByPrice() {
        if (products.isEmpty()) {
            System.out.println("No products in catalog.");
            return;
        }
        products.sort((a, b) -> Double.compare(a.getPrice(), b.getPrice()));
        System.out.println("Products sorted by price (lowest first):");
        for (Product p : products) {
            System.out.println(p);
        }
    }

    private static void saveOrderToFile(Order order) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            writer.println(order.toFileFormat());
        } catch (IOException e) {
            System.out.println("Error saving order to file: " + e.getMessage());
        }
    }

    private static void loadOrdersFromFile() {
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
                int orderId = Integer.parseInt(parts[0]);
                int customerId = Integer.parseInt(parts[1]);
                double total = Double.parseDouble(parts[2]);
                double discount = Double.parseDouble(parts[3]);
                double finalAmount = Double.parseDouble(parts[4]);

                orders.add(new Order(orderId, customerId, total, discount, finalAmount));

                if (orderId > maxId) {
                    maxId = orderId;
                }
            }
            nextOrderId = maxId + 1;
        } catch (IOException e) {
            System.out.println("Error loading orders file: " + e.getMessage());
        }
    }
}