import java.util.concurrent.*;

public class Consumer {
    public static void main(String[] args) {
        int port = 4000;
        int c = 1;

        BlockingQueue<byte[]> queue = new LinkedBlockingQueue<>(1);

        Thread dispatcher = new Thread(new Dispatcher(port, queue));
        dispatcher.start();

        ExecutorService consumerPool = Executors.newFixedThreadPool(c);
        for (int i = 0; i < c; i++) {
            consumerPool.execute(new CThread(i, queue));
        }

        consumerPool.shutdown();
    }
}

