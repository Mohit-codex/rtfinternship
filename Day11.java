import java.io.*;
import java.util.*;

public class Day11{
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("input.txt"));
        Map<String, Integer> wordCount = new HashMap<>();
        int lineCount = 0;
        int wordTotal = 0;
        String longest = "";
        String line;

        while ((line = br.readLine()) != null) {
            lineCount++;
            String cleaned = line.toLowerCase().replaceAll("[^a-zA-Z ]", "");
            String[] words = cleaned.trim().split("\\s+");

            for (String word : words) {
                if (word.isEmpty()) continue;
                wordTotal++;
                wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
                if (word.length() > longest.length()) longest = word;
            }
        }
        br.close();

        String mostFrequent = "";
        int maxFreq = 0;
        for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
            if (entry.getValue() > maxFreq) {
                maxFreq = entry.getValue();
                mostFrequent = entry.getKey();
            }
        }

        System.out.println("Total Words  : " + wordTotal);
        System.out.println("Total Lines  : " + lineCount);
        System.out.println("Longest Word : " + longest);
        System.out.println("Most Frequent: " + mostFrequent + " (" + maxFreq + " times)");

        System.out.println("\nTop 5 Words:");
        wordCount.entrySet().stream()
            .sorted((a, b) -> b.getValue() - a.getValue())
            .limit(5)
            .forEach(e -> System.out.println("  " + e.getKey() + " -> " + e.getValue()));
    }
}
