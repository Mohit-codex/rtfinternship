import java.util.Scanner;

// Calculator class - handles all operations
class Calculator {
    public double add(double a, double b) {
        return a + b;
    }

    public double sub(double a, double b) {
        return a - b;
    }

    public double mul(double a, double b) {
        return a * b;
    }

    public double div(double a, double b) {
        if (b == 0) {
            System.out.println("Error: Division by zero!");
            return 0;
        }
        return a / b;
    }

    // BONUS operations
    public double mod(double a, double b) {
        return a % b;
    }

    public double pow(double a, double b) {
        return Math.pow(a, b);
    }
}

// CommandProcessor class - parses and executes commands
class CommandProcessor {
    private Calculator calculator = new Calculator();

    public void process(String input) {
        String[] parts = input.trim().split("\\s+");

        // BONUS: Handle invalid commands (less than 3 parts)
        if (parts.length < 3) {
            System.out.println("Error: Invalid command format! Use: COMMAND num1 num2");
            return;
        }

        String command = parts[0].toUpperCase();
        double num1, num2;

        // BONUS: Handle invalid number input
        try {
            num1 = Double.parseDouble(parts[1]);
            num2 = Double.parseDouble(parts[2]);
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid numbers provided!");
            return;
        }

        double result;

        switch (command) {
            case "ADD":
                result = calculator.add(num1, num2);
                System.out.println("Result: " + num1 + " + " + num2 + " = " + result);
                break;
            case "SUB":
                result = calculator.sub(num1, num2);
                System.out.println("Result: " + num1 + " - " + num2 + " = " + result);
                break;
            case "MUL":
                result = calculator.mul(num1, num2);
                System.out.println("Result: " + num1 + " * " + num2 + " = " + result);
                break;
            case "DIV":
                result = calculator.div(num1, num2);
                System.out.println("Result: " + num1 + " / " + num2 + " = " + result);
                break;
            // BONUS commands
            case "MOD":
                result = calculator.mod(num1, num2);
                System.out.println("Result: " + num1 + " % " + num2 + " = " + result);
                break;
            case "POW":
                result = calculator.pow(num1, num2);
                System.out.println("Result: " + num1 + " ^ " + num2 + " = " + result);
                break;
            default:
                // BONUS: Handle invalid/unknown commands
                System.out.println("Error: Unknown command '" + command + "'! Use ADD, SUB, MUL, DIV, MOD, POW");
        }
    }
}

// Main class
public class Daytwo{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CommandProcessor processor = new CommandProcessor();

        System.out.println("=== Command-Based Calculator ===");
        System.out.println("Format: COMMAND num1 num2");
        System.out.println("Commands: ADD, SUB, MUL, DIV, MOD, POW");
        System.out.println("Type 'EXIT' to quit");
        System.out.println("================================");

        while (true) {
            System.out.print("\nEnter command: ");
            String input = scanner.nextLine();

            if (input.trim().equalsIgnoreCase("EXIT")) {
                System.out.println("Goodbye!");
                break;
            }

            processor.process(input);
        }

        scanner.close();
    }
}