import java.util.*;

public class Day9 {
    static class Student {
        String name;
        int marks;

        Student(String name, int marks) {
            this.name = name;
            this.marks = marks;
        }
    }

    public static void main(String[] args) {
        List<Student> students = new ArrayList<>();
        students.add(new Student("Alice", 88));
        students.add(new Student("Bob", 95));
        students.add(new Student("Charlie", 95));
        students.add(new Student("Diana", 76));
        students.add(new Student("Eve", 88));
        students.add(new Student("Frank", 60));

        students.sort((s1, s2) -> {
            if (s2.marks != s1.marks) {
                return s2.marks - s1.marks;
            }
            return s1.name.compareTo(s2.name);
        });

        System.out.println("Student Rankings:");
        System.out.println("---------------------------");

        int rank = 1;
        for (int i = 0; i < students.size(); i++) {
            if (i > 0 && students.get(i).marks != students.get(i - 1).marks) {
                rank = i + 1;
            }
            System.out.println("Rank " + rank + " | " + students.get(i).name + " | Marks: " + students.get(i).marks);
        }

        System.out.println("\nTop 3 Students:");
        System.out.println("---------------------------");
        int count = 0;
        int lastMarks = -1;
        for (Student s : students) {
            if (s.marks != lastMarks) {
                count++;
                lastMarks = s.marks;
            }
            if (count > 3) break;
            System.out.println(s.name + " | Marks: " + s.marks);
        }
    }
}
