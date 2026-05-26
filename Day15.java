import java.io.*;
import java.util.*;

public class Day15 {

    static class Employee {
        int id;
        String name;
        String department;
        int salary;
        int age;

        Employee(int id, String name, String department, int salary, int age) {
            this.id = id;
            this.name = name;
            this.department = department;
            this.salary = salary;
            this.age = age;
        }

        public String toCSV() {
            return id + "," + name + "," + department + "," + salary + "," + age;
        }

        public String toString() {
            return String.format("  ID:%-3d | %-10s | %-6s | Salary:%-7d | Age:%d",
                    id, name, department, salary, age);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("dataset.csv"));
        bw.write("ID,NAME,DEPARTMENT,SALARY,AGE\n");
        bw.write("1,Alice,IT,60000,25\n");
        bw.write("2,Bob,HR,40000,35\n");
        bw.write("3,John,IT,75000,28\n");
        bw.write("4,Sara,Finance,52000,29\n");
        bw.write("5,Mike,HR,38000,22\n");
        bw.write("6,Emma,IT,90000,32\n");
        bw.write("7,David,Finance,47000,27\n");
        bw.write("8,Linda,HR,43000,40\n");
        bw.write("9,INVALID_ROW\n");
        bw.write("10,Tom,IT,85000,26\n");
        bw.close();

        List<Employee> employees = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("dataset.csv"));
        String line;
        boolean isHeader = true;
        int invalidRows = 0;

        while ((line = br.readLine()) != null) {
            if (isHeader) { isHeader = false; continue; }
            String[] p = line.split(",");
            if (p.length != 5) { invalidRows++; continue; }
            try {
                employees.add(new Employee(
                    Integer.parseInt(p[0].trim()),
                    p[1].trim(),
                    p[2].trim(),
                    Integer.parseInt(p[3].trim()),
                    Integer.parseInt(p[4].trim())
                ));
            } catch (NumberFormatException e) {
                invalidRows++;
            }
        }
        br.close();

        System.out.println("======= DATA FILTER & EXPORT TOOL =======");
        System.out.println("Total Records Loaded : " + employees.size());
        System.out.println("Invalid Rows Skipped : " + invalidRows);

        List<Employee> highSalary = new ArrayList<>();
        for (Employee e : employees)
            if (e.salary > 50000) highSalary.add(e);

        highSalary.sort((a, b) -> b.salary - a.salary);

        System.out.println("\n[FILTER 1] Salary > 50000 (Sorted High to Low): " + highSalary.size() + " found");
        for (Employee e : highSalary) System.out.println(e);
        exportToFile(highSalary, "filtered_high_salary.csv", "ID,NAME,DEPARTMENT,SALARY,AGE");

        List<Employee> youngEmployees = new ArrayList<>();
        for (Employee e : employees)
            if (e.age < 30) youngEmployees.add(e);

        youngEmployees.sort((a, b) -> a.age - b.age);

        System.out.println("\n[FILTER 2] Age < 30 (Sorted Youngest First): " + youngEmployees.size() + " found");
        for (Employee e : youngEmployees) System.out.println(e);
        exportToFile(youngEmployees, "filtered_young.csv", "ID,NAME,DEPARTMENT,SALARY,AGE");

        List<Employee> multiFilter = new ArrayList<>();
        for (Employee e : employees)
            if (e.salary > 50000 && e.age < 30) multiFilter.add(e);

        multiFilter.sort((a, b) -> b.salary - a.salary);

        System.out.println("\n[FILTER 3] Salary > 50000 AND Age < 30: " + multiFilter.size() + " found");
        for (Employee e : multiFilter) System.out.println(e);
        exportToFile(multiFilter, "filtered_combined.csv", "ID,NAME,DEPARTMENT,SALARY,AGE");

        Map<String, Integer> deptCount = new HashMap<>();
        for (Employee e : employees)
            deptCount.put(e.department, deptCount.getOrDefault(e.department, 0) + 1);

        System.out.println("\n[SUMMARY] Employees Per Department:");
        for (Map.Entry<String, Integer> entry : deptCount.entrySet())
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue());

        System.out.println("\nExported Files:");
        System.out.println("  filtered_high_salary.csv");
        System.out.println("  filtered_young.csv");
        System.out.println("  filtered_combined.csv");
        System.out.println("==========================================");
    }

    static void exportToFile(List<Employee> list, String filename, String header) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
        bw.write(header + "\n");
        for (Employee e : list) bw.write(e.toCSV() + "\n");
        bw.close();
    }
}
