public class Day16{

    static int limit;

    static class EvenPrinter implements Runnable {
        public void run() {
            for (int i = 2; i <= limit; i += 2) {
                System.out.println("Thread-EVEN  --> " + i);
                try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
            }
        }
    }

    static class OddPrinter implements Runnable {
        public void run() {
            for (int i = 1; i <= limit; i += 2) {
                System.out.println("Thread-ODD   --> " + i);
                try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        System.out.print("Enter upper limit: ");
        limit = scanner.nextInt();
        scanner.close();

        System.out.println("\n====== MULTI-THREADED NUMBER PRINTER ======");
        System.out.println("Limit : " + limit);
        System.out.println("Thread 1 -> Even Numbers");
        System.out.println("Thread 2 -> Odd  Numbers");
        System.out.println("===========================================\n");

        Thread evenThread = new Thread(new EvenPrinter(), "EvenThread");
        Thread oddThread  = new Thread(new OddPrinter(),  "OddThread");

        oddThread.start();
        evenThread.start();

        oddThread.join();
        evenThread.join();

        System.out.println("\n===========================================");
        System.out.println("Both threads finished execution.");
        System.out.println("===========================================");
    }
}
