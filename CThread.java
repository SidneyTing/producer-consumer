import java.io.*;
import java.util.concurrent.*;

public class CThread implements Runnable {
	private int id;
    private BlockingQueue<byte[]> queue;

	public CThread(int id, BlockingQueue<byte[]> queue) {
		this.id = id;
        this.queue = queue;
	}

    public void run() {
        int fileCtr = 0;

        File saved_dir = new File("saved");

        if (!saved_dir.exists()) {
            saved_dir.mkdirs();
        }

        while (true) {
            String out_filename = "Consumer" + id + "_" + fileCtr + ".mov";
            File out_file = new File(saved_dir, out_filename);

            try {
                byte[] videoBytes = queue.take();

                try (FileOutputStream fos = new FileOutputStream(out_file)) {
                    fos.write(videoBytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("Saved file! \tConsumer Thread: " + id + "\tFile: " + out_filename);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            fileCtr++;
        }

        // System.out.println("Consumer " + id + " stopped.");
    }
}
