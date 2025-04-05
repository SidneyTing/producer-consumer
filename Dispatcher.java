import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Dispatcher implements Runnable {
    private int port;
    private BlockingQueue<VideoData> queue;

    public Dispatcher(int port, BlockingQueue<VideoData> queue) {
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
        try (ObjectInputStream ois = new ObjectInputStream(endpoint.getInputStream())) {
            while (true) {
                VideoData video;
                try {
                    video = (VideoData) ois.readObject();
                } catch (EOFException | ClassNotFoundException e) {
                    break;
                }
    
                queue.put(video);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
