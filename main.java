class Student {
    String name;
    int age;

    Student(String name, int age) {
        this.name = name;
        this.age = age;
    }
    // no toString() written
}

public class 
main {
    public static void main(String[] args) {
        Student s = new Student("Rahul", 20);

        System.out.println(s.toString()); // Student@7852e922
        System.out.println(s);            // Student@7852e922 (same)
    }
}