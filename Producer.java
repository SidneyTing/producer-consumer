import java.util.concurrent.*;

public class Producer {
    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        int p = 5;

        ExecutorService producerPool = Executors.newFixedThreadPool(p);

        for (int i = 0; i < p; i++) {
            producerPool.execute(new PThread(i, host));
        }

        producerPool.shutdown();
    }
}
