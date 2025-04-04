import java.util.concurrent.*;

public class Producer {
    public static void main(String[] args) {
        int p = 3;
        String host = args.length > 0 ? args[0] : "localhost";

        ExecutorService producerPool = Executors.newFixedThreadPool(p);

        for (int i = 0; i < p; i++) {
            producerPool.execute(new PThread(i, host));
        }

        producerPool.shutdown();
    }
}
