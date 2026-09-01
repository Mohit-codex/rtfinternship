
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.*;

class InvalidOptionException extends Exception {
    public InvalidOptionException(String message) {
        super(message);
    }
}

class Student {
    private int id;
    private String name;

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Student ID: " + id + " | Name: " + name;
    }
}

class Question {
    private int id;
    private String text;
    private String[] options; // A, B, C, D
    private char correctOption;

    public Question(int id, String text, String[] options, char correctOption) {
        this.id = id;
        this.text = text;
        this.options = options;
        this.correctOption = Character.toUpperCase(correctOption);
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String[] getOptions() {
        return options;
    }

    public char getCorrectOption() {
        return correctOption;
    }

    public void display() {
        System.out.println(text);
        String[] labels = {"A", "B", "C", "D"};
        for (int i = 0; i < options.length; i++) {
            System.out.println(labels[i] + ". " + options[i]);
        }
    }
}

class Result {
    private int studentId;
    private String studentName;
    private int score;
    private int totalQuestions;
    private String dateTime;

    public Result(int studentId, String studentName, int score, int totalQuestions, String dateTime) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.dateTime = dateTime;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public int getScore() {
        return score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public String getDateTime() {
        return dateTime;
    }

    public double getPercentage() {
        return totalQuestions == 0 ? 0 : (score * 100.0) / totalQuestions;
    }

    public String toFileFormat() {
        return studentId + "," + studentName + "," + score + "," + totalQuestions + "," + dateTime;
    }

    @Override
    public String toString() {
        return studentName + " (ID: " + studentId + ") | Score: " + score + "/" + totalQuestions +
                " (" + String.format("%.1f", getPercentage()) + "%) | Date: " + dateTime;
    }
}

class Quiz {
    private ArrayList<Question> questions = new ArrayList<>();
    private static final int TIME_LIMIT_SECONDS = 15;

    public void addQuestion(Question q) {
        questions.add(q);
    }

    public int getQuestionCount() {
        return questions.size();
    }

    public Result startQuiz(Student student, Scanner scanner) {
        if (questions.isEmpty()) {
            System.out.println("No questions available in the quiz.");
            return new Result(student.getId(), student.getName(), 0, 0, LocalDateTime.now().toString());
        }

        ArrayList<Question> quizQuestions = new ArrayList<>(questions);
        Collections.shuffle(quizQuestions); // random question order each attempt

        int score = 0;
        int qNum = 1;

        for (Question q : quizQuestions) {
            System.out.println();
            System.out.println("Question " + qNum + " of " + quizQuestions.size() +
                    " (Time limit: " + TIME_LIMIT_SECONDS + " seconds)");
            q.display();
            System.out.print("Your answer (A/B/C/D): ");

            String answer = readAnswerWithTimeout(scanner, TIME_LIMIT_SECONDS);

            try {
                char selected = validateAnswer(answer);
                if (selected == q.getCorrectOption()) {
                    System.out.println("Correct!");
                    score++;
                } else {
                    System.out.println("Wrong. Correct answer: " + q.getCorrectOption());
                }
            } catch (InvalidOptionException e) {
                System.out.println(e.getMessage() + " Marked as incorrect.");
            }

            qNum++;
        }

        String timestamp = LocalDateTime.now().toString();
        return new Result(student.getId(), student.getName(), score, quizQuestions.size(), timestamp);
    }

    private char validateAnswer(String answer) throws InvalidOptionException {
        if (answer == null || answer.trim().isEmpty()) {
            throw new InvalidOptionException("No answer given (time up or empty input).");
        }
        char selected = Character.toUpperCase(answer.trim().charAt(0));
        if (selected != 'A' && selected != 'B' && selected != 'C' && selected != 'D') {
            throw new InvalidOptionException("Invalid option entered.");
        }
        return selected;
    }

    // Reads a line from the scanner but gives up after timeoutSeconds, simulating a per-question timer.
    private String readAnswerWithTimeout(Scanner scanner, int timeoutSeconds) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(scanner::nextLine);

        try {
            return future.get(timeoutSeconds, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            System.out.println("\nTime's up!");
            future.cancel(true);
            return "";
        } catch (InterruptedException | ExecutionException e) {
            return "";
        } finally {
            executor.shutdownNow();
        }
    }
}

public class day25 {

    private static ArrayList<Student> students = new ArrayList<>();
    private static ArrayList<Result> leaderboard = new ArrayList<>();
    private static Quiz quiz = new Quiz();
    private static Scanner scanner = new Scanner(System.in);
    private static final String FILE_NAME = "leaderboard.txt";

    public static void main(String[] args) {
        loadLeaderboardFromFile();

        boolean running = true;
        while (running) {
            printMenu();
            int choice;

            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    addQuestion();
                    break;
                case 2:
                    registerStudent();
                    break;
                case 3:
                    takeQuiz();
                    break;
                case 4:
                    displayLeaderboard();
                    break;
                case 5:
                    running = false;
                    System.out.println("Exiting program.");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("===== ONLINE QUIZ & ASSESSMENT SYSTEM =====");
        System.out.println("1. Add Question");
        System.out.println("2. Register Student");
        System.out.println("3. Start Quiz");
        System.out.println("4. Display Leaderboard");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addQuestion() {
        try {
            System.out.print("Enter Question ID: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter Question Text: ");
            String text = scanner.nextLine().trim();

            String[] options = new String[4];
            String[] labels = {"A", "B", "C", "D"};
            for (int i = 0; i < 4; i++) {
                System.out.print("Enter Option " + labels[i] + ": ");
                options[i] = scanner.nextLine().trim();
            }

            System.out.print("Enter Correct Option (A/B/C/D): ");
            char correct = scanner.nextLine().trim().toUpperCase().charAt(0);

            quiz.addQuestion(new Question(id, text, options, correct));
            System.out.println("Question added successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Question ID must be numeric.");
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("Invalid input. Correct option cannot be empty.");
        }
    }

    private static void registerStudent() {
        try {
            System.out.print("Enter Student ID: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter Name: ");
            String name = scanner.nextLine().trim();

            students.add(new Student(id, name));
            System.out.println("Student registered successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Student ID must be numeric.");
        }
    }

    private static void takeQuiz() {
        if (students.isEmpty()) {
            System.out.println("No students registered yet.");
            return;
        }
        if (quiz.getQuestionCount() == 0) {
            System.out.println("No questions added yet.");
            return;
        }

        try {
            System.out.print("Enter Student ID: ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            Student student = null;
            for (Student s : students) {
                if (s.getId() == id) {
                    student = s;
                    break;
                }
            }
            if (student == null) {
                System.out.println("No student found with ID " + id);
                return;
            }

            System.out.println("\nStarting quiz for " + student.getName() + "...");
            Result result = quiz.startQuiz(student, scanner);

            System.out.println();
            System.out.println("----- RESULT SUMMARY -----");
            System.out.println(result);

            leaderboard.add(result);
            saveResultToFile(result);

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Student ID must be numeric.");
        }
    }

    private static void displayLeaderboard() {
        if (leaderboard.isEmpty()) {
            System.out.println("No quiz attempts yet.");
            return;
        }

        ArrayList<Result> sorted = new ArrayList<>(leaderboard);
        sorted.sort((a, b) -> Double.compare(b.getPercentage(), a.getPercentage()));

        System.out.println("----- LEADERBOARD -----");
        int rank = 1;
        for (Result r : sorted) {
            System.out.println(rank + ". " + r);
            rank++;
        }
    }

    private static void saveResultToFile(Result result) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            writer.println(result.toFileFormat());
        } catch (IOException e) {
            System.out.println("Error saving result to file: " + e.getMessage());
        }
    }

    private static void loadLeaderboardFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split(",");
                int studentId = Integer.parseInt(parts[0]);
                String studentName = parts[1];
                int score = Integer.parseInt(parts[2]);
                int total = Integer.parseInt(parts[3]);
                String dateTime = parts[4];

                leaderboard.add(new Result(studentId, studentName, score, total, dateTime));
            }
        } catch (IOException e) {
            System.out.println("Error loading leaderboard file: " + e.getMessage());
        }
    }
}