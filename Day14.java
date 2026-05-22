import java.io.*;
import java.util.*;

public class Day14 {
    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("document.txt"));
        bw.write("Java is a powerful programming language\n");
        bw.write("Java is used for web development and mobile development\n");
        bw.write("Python is also a popular programming language\n");
        bw.write("Java and Python are both object oriented languages\n");
        bw.write("Web development includes frontend and backend development\n");
        bw.write("Java supports multithreading and object oriented programming\n");
        bw.close();

        Map<String, List<Integer>> index = new HashMap<>();
        Map<Integer, String> lineMap = new HashMap<>();

        BufferedReader br = new BufferedReader(new FileReader("document.txt"));
        String line;
        int lineNum = 1;

        while ((line = br.readLine()) != null) {
            lineMap.put(lineNum, line);
            String[] words = line.toLowerCase().replaceAll("[^a-zA-Z ]", "").split("\\s+");
            for (String word : words) {
                if (word.isEmpty()) continue;
                index.putIfAbsent(word, new ArrayList<>());
                if (!index.get(word).contains(lineNum)) {
                    index.get(word).add(lineNum);
                }
            }
            lineNum++;
        }
        br.close();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter search query: ");
        String query = scanner.nextLine().toLowerCase().replaceAll("[^a-zA-Z ]", "").trim();
        String[] queryWords = query.split("\\s+");

        System.out.println("\n====== SEARCH RESULTS FOR: \"" + query + "\" ======\n");

        Map<Integer, Integer> lineScore = new HashMap<>();

        for (String qWord : queryWords) {
            if (index.containsKey(qWord)) {
                for (int ln : index.get(qWord)) {
                    lineScore.put(ln, lineScore.getOrDefault(ln, 0) + 1);
                }
            }
        }

        if (lineScore.isEmpty()) {
            System.out.println("No results found.");
        } else {
            List<Map.Entry<Integer, Integer>> ranked = new ArrayList<>(lineScore.entrySet());
            ranked.sort((a, b) -> b.getValue() - a.getValue());

            for (Map.Entry<Integer, Integer> entry : ranked) {
                int ln = entry.getKey();
                int score = entry.getValue();
                String originalLine = lineMap.get(ln);
                String highlighted = originalLine;

                for (String qWord : queryWords) {
                    highlighted = highlighted.replaceAll("(?i)(" + qWord + ")", ">>$1<<");
                }

                System.out.println("Line " + ln + " [Score: " + score + " match(es)]");
                System.out.println("  Original  : " + originalLine);
                System.out.println("  Highlighted: " + highlighted);
                System.out.println();
            }

            System.out.println("--------------------------------------");
            System.out.println("Total Matching Lines : " + lineScore.size());

            for (String qWord : queryWords) {
                List<Integer> lines = index.getOrDefault(qWord, new ArrayList<>());
                System.out.println("Word \"" + qWord + "\" found " + lines.size() + " time(s) on lines: " + lines);
            }
        }

        scanner.close();
    }
}
