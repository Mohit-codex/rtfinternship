import java.util.LinkedList;
import java.util.Queue;

public class ProducerConsumer {

    // Shared buffer with limited size
    static class Buffer {
        private final Queue<Integer> queue = new LinkedList<>();
        private final int CAPACITY = 5;

        // Producer adds items
        public synchronized void produce(int item) throws InterruptedException {
            while (queue.size() == CAPACITY) {
                System.out.println("Buffer FULL. Producer waiting...");
                wait(); // wait until consumer removes something
            }

            queue.add(item);
            System.out.println("Produced: " + item + " | Buffer size: " + queue.size());

            notifyAll(); // wake up consumer if it's waiting
        }

        // Consumer removes items
        public synchronized int consume() throws InterruptedException {
            while (queue.isEmpty()) {
                System.out.println("Buffer EMPTY. Consumer waiting...");
                wait(); // wait until producer adds something
            }

            int item = queue.poll();
            System.out.println("Consumed: " + item + " | Buffer size: " + queue.size());

            notifyAll(); // wake up producer if it's waiting
            return item;
        }
    }

    public static void main(String[] args) {
        Buffer buffer = new Buffer();
        int ITEMS = 10;

        // Producer thread
        Thread producer = new Thread(() -> {
            for (int i = 1; i <= ITEMS; i++) {
                try {
                    buffer.produce(i);
                    Thread.sleep((long) (Math.random() * 400) + 200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Producer done.");
        });

        // Consumer thread
        Thread consumer = new Thread(() -> {
            for (int i = 1; i <= ITEMS; i++) {
                try {
                    buffer.consume();
                    Thread.sleep((long) (Math.random() * 600) + 300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Consumer done.");
        });

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Simulation finished successfully!");
    }
}