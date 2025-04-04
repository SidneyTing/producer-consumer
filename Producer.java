import java.util.concurrent.*;

public class Producer {
    public static void main(String[] args) {
        int p = 2;

        ExecutorService producerPool = Executors.newFixedThreadPool(p);

        for (int i = 0; i < p; i++) {
            producerPool.execute(new PThread(i));
        }

        producerPool.shutdown();
    }
}
