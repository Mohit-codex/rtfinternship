import java.util.*;

public class Day8 {
    public static void main(String[] args) {
        List<Integer> userIds = Arrays.asList(101, 102, 103, 101, 104, 102);
        
        Set<Integer> uniqueUsers = new LinkedHashSet<>(userIds);
        
        Map<Integer, Integer> duplicateCount = new HashMap<>();
        for (int id : userIds) {
            duplicateCount.put(id, duplicateCount.getOrDefault(id, 0) + 1);
        }
        
        System.out.println("Unique Users: " + uniqueUsers);
        System.out.println("Total Unique Visitors: " + uniqueUsers.size());
        
        System.out.println("\nDuplicate Entry Counts:");
        for (Map.Entry<Integer, Integer> entry : duplicateCount.entrySet()) {
            if (entry.getValue() > 1) {
                System.out.println("User " + entry.getKey() + " appeared " + entry.getValue() + " times");
            }
        }
    }
}
