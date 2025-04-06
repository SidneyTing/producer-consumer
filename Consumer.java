import java.util.concurrent.*;
import java.util.*;

public class Consumer {
    public static final Set<String> processedFiles = ConcurrentHashMap.newKeySet();

    public static void main(String[] args) {
        int port = 4000;
        int c = 1;
        int http_port = 8080;

        BlockingQueue<VideoData> queue = new LinkedBlockingQueue<>(1);

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

    public static void startHttpServer(int port) {
        try {
            new HttpFileServer(port).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
