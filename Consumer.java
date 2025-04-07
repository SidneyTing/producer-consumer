import java.util.concurrent.*;
import java.util.*;
import java.io.*;

public class Consumer {
    public static final Set<String> processedFiles = ConcurrentHashMap.newKeySet();

    private static final int MIN_CONSUMER_THREADS = 1;
    private static final int MAX_CONSUMER_THREADS = 10;
    private static final int MIN_QUEUE_SIZE = 1;
    private static final int MAX_QUEUE_SIZE = 100;

    private static final String CONFIG_FILE = "config.txt";

    public static void main(String[] args) {
        int port = 4000;
        int http_port = 8080;
        
        int[] config = readConfig();
        int c = config[0]; 
        int q = config[1]; 

        BlockingQueue<VideoData> queue = new LinkedBlockingQueue<>(q);

        Thread httpServer = new Thread(() -> startHttpServer(http_port));
        httpServer.start();

        Thread dispatcher = new Thread(new Dispatcher(port, queue));
        dispatcher.start();

        ExecutorService consumerPool = Executors.newFixedThreadPool(c);
        for (int i = 0; i < c; i++) {
            consumerPool.execute(new CThread(i, queue));
        }

        consumerPool.shutdown();
    }
    
    private static int[] readConfig() {
        int c = 1; 
        int q = 1; 
        
        System.out.println("CONFIGURATIONS:");
        try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE))) {
            reader.readLine();
            
            String cLine = reader.readLine();
            try {
                c = Integer.parseInt(cLine.trim());
                
                if (c < MIN_CONSUMER_THREADS) {
                    System.out.println("Consumer threads too low (" + c + "), using minimum: " + MIN_CONSUMER_THREADS);
                    c = MIN_CONSUMER_THREADS;
                } else if (c > MAX_CONSUMER_THREADS) {
                    System.out.println("Consumer threads too high (" + c + "), using maximum: " + MAX_CONSUMER_THREADS);
                    c = MAX_CONSUMER_THREADS;
                }
                
                System.out.println("Consumer threads (c): " + c);
            } catch (NumberFormatException e) {
                System.out.println("Invalid consumer thread count in config, using default: " + c);
            }
            
            String qLine = reader.readLine();
            try {
                q = Integer.parseInt(qLine.trim());
                
                if (q < MIN_QUEUE_SIZE) {
                    System.out.println("Queue size too low (" + q + "), using minimum: " + MIN_QUEUE_SIZE);
                    q = MIN_QUEUE_SIZE;
                } else if (q > MAX_QUEUE_SIZE) {
                    System.out.println("Queue size too high (" + q + "), using maximum: " + MAX_QUEUE_SIZE);
                    q = MAX_QUEUE_SIZE;
                }
                
                System.out.println("Queue size (q): " + q);
            } catch (NumberFormatException e) {
                System.out.println("Invalid queue size in config, using default: " + q);
            }
        } catch (IOException e) {
            System.out.println("Could not read config.txt, using defaults: c=" + c + ", q=" + q);
        }

        System.out.println("");
        
        return new int[] {c, q};
    }

    public static void startHttpServer(int port) {
        try {
            new HttpFileServer(port).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}