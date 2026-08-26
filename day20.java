import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class day20 {

    static class ClientRequest implements Runnable {
        private final int requestId;
        private final Random random = new Random();

        ClientRequest(int requestId) {
            this.requestId = requestId;
        }

        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            long start = System.currentTimeMillis();

            System.out.println("Request #" + requestId + " received by " + threadName);

            try {
                int workTime = 500 + random.nextInt(2000);
                Thread.sleep(workTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            long end = System.currentTimeMillis();
            long processingTime = end - start;

            System.out.println("Request #" + requestId + " handled by " + threadName +
                    " | Processing time: " + processingTime + "ms");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int maxThreads = 3;
        int totalRequests = 10;

        ExecutorService serverPool = Executors.newFixedThreadPool(maxThreads);

        System.out.println("Mini server started with " + maxThreads + " worker threads");
        System.out.println("Incoming requests: " + totalRequests);
        System.out.println();

        for (int i = 1; i <= totalRequests; i++) {
            ClientRequest request = new ClientRequest(i);
            serverPool.submit(request);
            System.out.println("Request #" + i + " queued");
        }

        serverPool.shutdown();
        serverPool.awaitTermination(1, TimeUnit.MINUTES);

        System.out.println();
        System.out.println("All requests processed. Server shutting down.");
    }
}
