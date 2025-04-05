import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Dispatcher implements Runnable {
    private int port;
    private BlockingQueue<byte[]> queue;

    public Dispatcher(int port, BlockingQueue<byte[]> queue) {
        this.port = port;
        this.queue = queue;
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Dispatcher listening for Producers on port " + port + "...");

            while (true) {
                Socket serverEndpoint = serverSocket.accept();
                System.out.println("Connected to Producer! \tHost: " + serverEndpoint.getInetAddress());
                new Thread(() -> produce(serverEndpoint)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void produce(Socket endpoint) {
        try (DataInputStream dis = new DataInputStream(endpoint.getInputStream())) {
            while (true) {
                long videoSize;
                try {
                    videoSize = dis.readLong();
                } catch (EOFException e) {
                    break;
                }
    
                byte[] videoBytes = new byte[(int) videoSize];
                dis.readFully(videoBytes);
                queue.offer(videoBytes);

                // System.out.println("Successfully enqueued!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
