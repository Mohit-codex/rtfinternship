
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;


public class day19 {

 
    static class ChatMessage {
        final String timestamp;
        final String user;
        final String text;

        ChatMessage(String timestamp, String user, String text) {
            this.timestamp = timestamp;
            this.user = user;
            this.text = text;
        }

        @Override
        public String toString() {
            return "[" + timestamp + "] " + user + ": " + text;
        }
    }

    static class ChatHistory {
      
        private final List<ChatMessage> messages = new CopyOnWriteArrayList<>();

        void addMessage(ChatMessage message) {
            messages.add(message);
          
            System.out.println(message);
        }

        List<ChatMessage> getAllMessages() {
            return new ArrayList<>(messages);
        }

        List<ChatMessage> filterByUser(String user) {
            List<ChatMessage> result = new ArrayList<>();
            for (ChatMessage m : messages) {
                if (m.user.equalsIgnoreCase(user)) {
                    result.add(m);
                }
            }
            return result;
        }
    }

   
    static class ChatUser extends Thread {
        private final String username;
        private final ChatHistory history;
        private final String[] sampleMessages;
        private final Random random = new Random();
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        private volatile boolean running = true;

        ChatUser(String username, ChatHistory history, String[] sampleMessages) {
            this.username = username;
            this.history = history;
            this.sampleMessages = sampleMessages;
            setName(username); 
        }

        void stopChatting() {
            running = false;
            this.interrupt();
        }

        @Override
        public void run() {
            while (running) {
                try {
                    
                    int delay = 500 + random.nextInt(2000);
                    Thread.sleep(delay);

                    String text = sampleMessages[random.nextInt(sampleMessages.length)];
                    String timestamp = LocalTime.now().format(formatter);

                    history.addMessage(new ChatMessage(timestamp, username, text));
                } catch (InterruptedException e) {
                   
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ChatHistory history = new ChatHistory();

        String[] aliceMessages = {
            "Hey everyone!", "Anyone around?", "Working on the project.",
            "lol that's funny", "brb"
        };
        String[] bobMessages = {
            "Yo!", "Just joined.", "What's the plan?", "Sounds good to me.",
            "gtg, talk later"
        };
        String[] carolMessages = {
            "Morning all", "Can someone review my PR?", "Thanks!",
            "That deadline is tight", "Let's sync tomorrow"
        };

        List<ChatUser> users = new ArrayList<>();
        users.add(new ChatUser("Alice", history, aliceMessages));
        users.add(new ChatUser("Bob", history, bobMessages));
        users.add(new ChatUser("Carol", history, carolMessages));

        System.out.println("=== Chat simulation started (running for 10 seconds) ===\n");

        for (ChatUser user : users) {
            user.start();
        }

        
        Thread.sleep(10_000);

        for (ChatUser user : users) {
            user.stopChatting();
        }
        for (ChatUser user : users) {
            user.join();
        }

        System.out.println("\n=== Chat simulation ended ===");

      
        System.out.println("\n--- Full Chat History (" + history.getAllMessages().size() + " messages) ---");
        for (ChatMessage m : history.getAllMessages()) {
            System.out.println(m);
        }

        String filterUser = "Alice";
        System.out.println("\n--- Messages from " + filterUser + " only ---");
        for (ChatMessage m : history.filterByUser(filterUser)) {
            System.out.println(m);
        }
    }
}
