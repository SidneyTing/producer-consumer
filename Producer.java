import java.util.concurrent.*;
import java.io.*;

public class Producer {
    private static final int MIN_PRODUCER_THREADS = 1;
    private static final int MAX_PRODUCER_THREADS = 10;
    private static final int DEFAULT_PRODUCER_THREADS = 5;

    private static final String CONFIG_FILE = "config.txt";
    
    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        int p = readConfig(); 

        System.out.println("");

        ExecutorService producerPool = Executors.newFixedThreadPool(p);

        for (int i = 0; i < p; i++) {
            producerPool.execute(new PThread(i, host));
        }

        producerPool.shutdown();
    }

    private static int readConfig() {
        int p = DEFAULT_PRODUCER_THREADS; 
        
        System.out.println("CONFIGURATIONS:");
        try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                String[] parts = line.split("\\s+", 2);
                if (parts.length < 2) continue;
                
                String param = parts[0].toLowerCase();
                
                if (param.equals("p")) {
                    try {
                        p = Integer.parseInt(parts[1].trim());
                        
                        if (p < MIN_PRODUCER_THREADS) {
                            System.out.println("Producer threads too low (" + p + "), using minimum: " + MIN_PRODUCER_THREADS);
                            p = MIN_PRODUCER_THREADS;
                        } else if (p > MAX_PRODUCER_THREADS) {
                            System.out.println("Producer threads too high (" + p + "), using maximum: " + MAX_PRODUCER_THREADS);
                            p = MAX_PRODUCER_THREADS;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid producer thread count in config, using default: " + p);
                    }
                    break; 
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read config.txt, using default producer threads: " + p);
        }
        
        System.out.println("Producer threads (p): " + p);
        return p;
    }
}