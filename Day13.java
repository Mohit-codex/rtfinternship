import java.io.*;
import java.util.*;

public class Day13 {
    public static void main(String[] args) throws IOException {
        BufferedWriter sampleWriter = new BufferedWriter(new FileWriter("logfile.txt"));
        sampleWriter.write("2024-01-15 10:00:01 ERROR DISK FULL\n");
        sampleWriter.write("2024-01-15 10:00:02 INFO STARTED\n");
        sampleWriter.write("2024-01-15 10:00:03 WARNING MEMORY LOW\n");
        sampleWriter.write("2024-01-15 10:00:04 ERROR FILE MISSING\n");
        sampleWriter.write("2024-01-15 10:00:05 INFO SERVER RUNNING\n");
        sampleWriter.write("2024-01-15 10:00:06 ERROR CONNECTION TIMEOUT\n");
        sampleWriter.write("2024-01-15 10:00:07 INFO USER LOGGED IN\n");
        sampleWriter.write("2024-01-15 10:00:08 WARNING CPU HIGH\n");
        sampleWriter.write("2024-01-15 10:00:09 ERROR DATABASE DOWN\n");
        sampleWriter.write("2024-01-15 10:00:10 DEBUG CACHE CLEARED\n");
        sampleWriter.close();

        BufferedReader br = new BufferedReader(new FileReader("logfile.txt"));
        Map<String, Integer> logCount = new HashMap<>();
        List<String> errorLines = new ArrayList<>();
        List<String> allLogs = new ArrayList<>();
        String line;
        String keyword = "ERROR";

        while ((line = br.readLine()) != null) {
            allLogs.add(line);
            String[] parts = line.split(" ", 3);

            if (parts.length < 3) continue;

            String timestamp = parts[0] + " " + parts[1];
            String rest = parts[2];
            String[] restParts = rest.split(" ", 2);
            String logType = restParts[0].trim();
            String message = restParts.length > 1 ? restParts[1].trim() : "";

            logCount.put(logType, logCount.getOrDefault(logType, 0) + 1);

            if (logType.equalsIgnoreCase(keyword)) {
                errorLines.add("[" + timestamp + "] " + message);
            }
        }
        br.close();

        String mostFrequent = "";
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : logCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostFrequent = entry.getKey();
            }
        }

        StringBuilder report = new StringBuilder();
        report.append("======= LOG MONITORING REPORT =======\n");
        report.append("\nLog Type Count:\n");
        for (Map.Entry<String, Integer> entry : logCount.entrySet()) {
            report.append("  " + entry.getKey() + " -> " + entry.getValue() + "\n");
        }

        report.append("\nMost Frequent Log Type: " + mostFrequent + " (" + maxCount + " times)\n");

        report.append("\nAll ERROR Lines:\n");
        for (String err : errorLines) {
            report.append("  " + err + "\n");
        }

        report.append("\nFiltered by Keyword [" + keyword + "]: " + errorLines.size() + " found\n");
        report.append("=====================================\n");

        System.out.print(report);

        BufferedWriter bw = new BufferedWriter(new FileWriter("log_report.txt"));
        bw.write(report.toString());
        bw.close();

        System.out.println("Report saved to log_report.txt");
    }
}
