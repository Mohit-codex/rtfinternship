import java.util.*;
import java.util.concurrent.*;

public class Day17 {

    static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);
    static Map<String, ScheduledFuture<?>> taskMap = new HashMap<>();

    static void scheduleTask(String taskName, String message, int delaySec, boolean repeat, int intervalSec) {
        Runnable task = () -> {
            System.out.println("[" + new java.util.Date() + "] EXECUTING --> " + taskName + " : " + message);
        };

        ScheduledFuture<?> future;

        if (repeat) {
            future = scheduler.scheduleAtFixedRate(task, delaySec, intervalSec, TimeUnit.SECONDS);
            System.out.println("  [SCHEDULED] " + taskName + " -> runs after " + delaySec + "s, repeats every " + intervalSec + "s");
        } else {
            future = scheduler.schedule(task, delaySec, TimeUnit.SECONDS);
            System.out.println("  [SCHEDULED] " + taskName + " -> runs once after " + delaySec + "s");
        }

        taskMap.put(taskName, future);
    }

    static void cancelTask(String taskName) {
        ScheduledFuture<?> future = taskMap.get(taskName);
        if (future != null && !future.isCancelled()) {
            future.cancel(false);
            System.out.println("  [CANCELLED] " + taskName + " has been cancelled.");
        } else {
            System.out.println("  [INFO] " + taskName + " not found or already cancelled.");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("======= TASK SCHEDULER SYSTEM =======");
        System.out.println("Start Time: " + new java.util.Date());
        System.out.println("======================================\n");

        System.out.println("Adding Tasks...");
        scheduleTask("Task-1", "Send Email Notification",   2, false, 0);
        scheduleTask("Task-2", "Backup Database",           5, false, 0);
        scheduleTask("Task-3", "Generate Report",           7, false, 0);
        scheduleTask("Alarm-1","Ring Morning Alarm",        3, true,  4);
        scheduleTask("Alarm-2","System Health Check",       1, true,  6);

        System.out.println("\n[INFO] Task-2 will be cancelled before execution...");
        Thread.sleep(3000);
        cancelTask("Task-2");

        System.out.println("\n[INFO] Letting remaining tasks execute...\n");
        Thread.sleep(10000);

        System.out.println("\n[INFO] Cancelling all repeating alarms...");
        cancelTask("Alarm-1");
        cancelTask("Alarm-2");

        scheduler.shutdown();
        System.out.println("\n======================================");
        System.out.println("All tasks completed. Scheduler stopped.");
        System.out.println("======================================");
    }
}
