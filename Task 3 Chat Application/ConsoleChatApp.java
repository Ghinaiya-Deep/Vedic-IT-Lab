import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.concurrent.*;

public class ConsoleChatApp {

    private static final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void main(String[] args) {
        BlockingQueue<String> user1Queue = new LinkedBlockingQueue<>();
        BlockingQueue<String> user2Queue = new LinkedBlockingQueue<>();

        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(new User("User1", user1Queue, user2Queue));
        executor.submit(new User("User2", user2Queue, user1Queue));
    }

    static class User implements Runnable {
        private final String name;
        private final BlockingQueue<String> inputQueue;
        private final BlockingQueue<String> outputQueue;
        private final Scanner scanner;

        public User(String name, BlockingQueue<String> inputQueue, BlockingQueue<String> outputQueue) {
            this.name = name;
            this.inputQueue = inputQueue;
            this.outputQueue = outputQueue;
            this.scanner = new Scanner(System.in);
        }

        @Override
        public void run() {
            try {
                while (true) {
                    // Check for incoming message
                    if (!inputQueue.isEmpty()) {
                        String message = inputQueue.take();
                        printMessage(message);
                    }

                    // Prompt user to send message
                    System.out.print(name + " >> ");
                    String userInput = scanner.nextLine();

                    if (userInput.equalsIgnoreCase("exit")) {
                        System.out.println(name + " has left the chat.");
                        outputQueue.put(name + " has exited the chat.");
                        break;
                    }

                    String messageToSend = formatMessage(name, userInput);
                    outputQueue.put(messageToSend);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private String formatMessage(String sender, String msg) {
            String timeStamp = LocalDateTime.now().format(timeFormat);
            return "[" + timeStamp + "] " + sender + ": " + msg;
        }

        private void printMessage(String msg) {
            System.out.println("\n" + msg);
            System.out.print(name + " >> "); // reprint prompt
        }
    }
}
