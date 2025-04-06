import java.util.concurrent.*;
import java.io.*;

public class Producer {
    // Constants for validation
    private static final int MIN_PRODUCER_THREADS = 1;
    private static final int MAX_PRODUCER_THREADS = 10;
    
    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        int p = 5; 

        try (BufferedReader reader = new BufferedReader(new FileReader("config.txt"))) {
            String line = reader.readLine();
            try {
                p = Integer.parseInt(line.trim());
                
                if (p < MIN_PRODUCER_THREADS) {
                    System.out.println("Producer threads too low (" + p + "), using minimum: " + MIN_PRODUCER_THREADS);
                    p = MIN_PRODUCER_THREADS;
                } else if (p > MAX_PRODUCER_THREADS) {
                    System.out.println("Producer threads too high (" + p + "), using maximum: " + MAX_PRODUCER_THREADS);
                    p = MAX_PRODUCER_THREADS;
                }
                
                System.out.println("Producer threads (p): " + p);
            } catch (NumberFormatException e) {
                System.out.println("Invalid producer thread count in config, using default: " + p);
            }
        } catch (IOException e) {
            System.out.println("Could not read config.txt, using default producer threads: " + p);
        }

        ExecutorService producerPool = Executors.newFixedThreadPool(p);

        for (int i = 0; i < p; i++) {
            producerPool.execute(new PThread(i, host));
        }

        producerPool.shutdown();
    }
}