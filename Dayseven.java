import java.util.HashMap;
import java.util.Map;

public class Dayseven{
    public static void main(String[] args) {
        String input = "THIS IS A TEST THIS IS SIMPLE TEST";
        String lower = input.toLowerCase();
        String[] words = lower.split(" ");

        HashMap<String, Integer> freq = new HashMap<>();
        for (String word : words) {
            freq.put(word, freq.getOrDefault(word, 0) + 1);
        }

        System.out.println("Word Frequencies:");
        for (Map.Entry<String, Integer> entry : freq.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        
        String mostFrequent = "";
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : freq.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostFrequent = entry.getKey();
            }
        }
        System.out.println("\nMost Frequent Word: " + mostFrequent + " (" + maxCount + " times)");
    }
}
