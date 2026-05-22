import java.io.*;
import java.util.*;

public class Day12 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("employees.csv"));
        Map<String, List<Integer>> deptSalary = new HashMap<>();
        Map<String, Integer> highestPaid = new HashMap<>();
        String highestEmployee = "";
        int highestSalary = 0;
        String line;
        boolean isHeader = true;
        int invalidRows = 0;

        while ((line = br.readLine()) != null) {
            if (isHeader) { isHeader = false; continue; }

            String[] parts = line.split(",");

            if (parts.length != 4) { invalidRows++; continue; }

            try {
                int id = Integer.parseInt(parts[0].trim());
                String name = parts[1].trim();
                String dept = parts[2].trim();
                int salary = Integer.parseInt(parts[3].trim());

                deptSalary.putIfAbsent(dept, new ArrayList<>());
                deptSalary.get(dept).add(salary);

                if (!highestPaid.containsKey(dept) || salary > highestPaid.get(dept)) {
                    highestPaid.put(dept, salary);
                }

                if (salary > highestSalary) {
                    highestSalary = salary;
                    highestEmployee = name;
                }
            } catch (NumberFormatException e) {
                invalidRows++;
            }
        }
        br.close();

        StringBuilder report = new StringBuilder();
        report.append("========== CSV REPORT ==========\n");
        report.append("\nAverage Salary Per Department:\n");

        for (Map.Entry<String, List<Integer>> entry : deptSalary.entrySet()) {
            String dept = entry.getKey();
            List<Integer> salaries = entry.getValue();
            double avg = salaries.stream().mapToInt(i -> i).average().orElse(0);
            report.append("  " + dept + " -> Avg: " + String.format("%.2f", avg)
                    + " | Highest: " + highestPaid.get(dept) + "\n");
        }

        report.append("\nHighest Paid Employee Overall:\n");
        report.append("  " + highestEmployee + " with salary " + highestSalary + "\n");
        report.append("\nInvalid Rows Skipped: " + invalidRows + "\n");
        report.append("================================\n");

        System.out.print(report);

        BufferedWriter bw = new BufferedWriter(new FileWriter("report.txt"));
        bw.write(report.toString());
        bw.close();

        System.out.println("Report saved to report.txt");
    }
}