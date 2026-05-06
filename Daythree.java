import java.util.*;

class Question {
    private String question;
    private String[] options;
    private int correctAnswer; // 1-4

    public Question(String question, String[] options, int correctAnswer) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion()      { return question; }
    public String[] getOptions()     { return options; }
    public int getCorrectAnswer()    { return correctAnswer; }

    public void display(int qNum) {
        System.out.println("\nQ" + qNum + ": " + question);
        for (int i = 0; i < options.length; i++) {
            System.out.println("  " + (i + 1) + ". " + options[i]);
        }
    }
}

class Quiz {
    private List<Question> questions = new ArrayList<>();
    private int[] userAnswers;
    private int score = 0;

    public void addQuestion(Question q) {
        questions.add(q);
    }

    // BONUS: Randomize questions
    public void shuffle() {
        Collections.shuffle(questions);
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        userAnswers = new int[questions.size()];

        System.out.println("\n=============================");
        System.out.println("       ONLINE QUIZ SYSTEM    ");
        System.out.println("=============================");
        System.out.println("Total Questions: " + questions.size());
        System.out.println("Enter option number (1-4) for each question.");
        System.out.println("=============================");

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            q.display(i + 1);

            int answer = -1;
            while (answer < 1 || answer > 4) {
                System.out.print("Your answer: ");
                try {
                    answer = Integer.parseInt(scanner.nextLine().trim());
                    if (answer < 1 || answer > 4)
                        System.out.println("Please enter a number between 1 and 4.");
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input! Enter a number (1-4).");
                }
            }

            userAnswers[i] = answer;

            if (answer == q.getCorrectAnswer()) {
                score++;
                System.out.println("✔ Correct!");
            } else {
                System.out.println("✘ Wrong!");
            }
        }

        showResult();
    }

  
    public void showResult() {
        System.out.println("\n=============================");
        System.out.println("         QUIZ RESULTS        ");
        System.out.println("=============================");
        System.out.println("Score: " + score + " / " + questions.size());

        double percent = (score * 100.0) / questions.size();
        System.out.printf("Percentage: %.1f%%%n", percent);

        if      (percent == 100) System.out.println("Grade: A+ 🏆 Perfect!");
        else if (percent >= 80)  System.out.println("Grade: A  ⭐ Excellent!");
        else if (percent >= 60)  System.out.println("Grade: B  👍 Good!");
        else if (percent >= 40)  System.out.println("Grade: C  📖 Keep Practicing!");
        else                     System.out.println("Grade: F  💪 Don't Give Up!");

        System.out.println("\n--- Correct Answers Review ---");
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            int correct = q.getCorrectAnswer();
            int yours   = userAnswers[i];

            System.out.println("\nQ" + (i + 1) + ": " + q.getQuestion());
            System.out.println("  Your Answer   : " + yours + ". " + q.getOptions()[yours - 1]);
            System.out.println("  Correct Answer: " + correct + ". " + q.getOptions()[correct - 1]);
            System.out.println("  " + (yours == correct ? "✔ Correct" : "✘ Wrong"));
        }

        System.out.println("\n=============================");
        System.out.println("      Thanks for playing!    ");
        System.out.println("=============================");
    }
}


public class Daythree {
    public static void main(String[] args) {
        Quiz quiz = new Quiz();

       
        quiz.addQuestion(new Question(
            "What is the size of int in Java?",
            new String[]{"8 bytes", "4 bytes", "2 bytes", "1 byte"},
            2
        ));

        quiz.addQuestion(new Question(
            "Which keyword is used to create a class in Java?",
            new String[]{"object", "define", "class", "struct"},
            3
        ));

        quiz.addQuestion(new Question(
            "What does JVM stand for?",
            new String[]{"Java Virtual Memory", "Java Visual Machine",
                         "Java Virtual Machine", "Java Variable Method"},
            3
        ));

        quiz.addQuestion(new Question(
            "Which of these is NOT a primitive data type in Java?",
            new String[]{"int", "boolean", "String", "char"},
            3
        ));

        quiz.addQuestion(new Question(
            "What is the default value of a boolean in Java?",
            new String[]{"true", "false", "0", "null"},
            2
        ));

        
        quiz.shuffle();

       
        quiz.start();
    }
}
