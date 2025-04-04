import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Consumer {
    public static void main(String[] args) {
        int nPort = 4000;
        int c = 3;

        ExecutorService consumerPool = Executors.newFixedThreadPool(c);

        try {
            ServerSocket serverSocket = new ServerSocket(nPort);

            for (int i = 0; i < c; i++) {
                consumerPool.execute(new CThread(i, serverSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        consumerPool.shutdown();
    }
}
