import java.io.*;
import java.util.concurrent.*;

public class CThread implements Runnable {
	private int id;
    private BlockingQueue<VideoData> queue;

	public CThread(int id, BlockingQueue<VideoData> queue) {
		this.id = id;
        this.queue = queue;
    }

    public void run() {
        File saved_dir = new File("saved");

        if (!saved_dir.exists()) {
            saved_dir.mkdirs();
        }

        while (true) {
            try {
                VideoData video = queue.take();  // blocking

                // Duplication detection
                String hash = video.getHash();
                if (Consumer.processedFiles.contains(hash)) {
                    System.out.println("Duplicate file detected! Skipping: " + video.getTitle() + "." + video.getFormat());
                    continue;
                }
      
                Consumer.processedFiles.add(hash);

                byte[] compressedBytes = video.getBytes();
                
                String title = video.getTitle();
                String format = video.getFormat();

                // Reconstruct file
                String out_filename = title + "." + format;
                File out_file = new File(saved_dir, out_filename);

                try (FileOutputStream fos = new FileOutputStream(out_file)) {
                    fos.write(compressedBytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("Saved file! \tConsumer Thread: " + id + "\tFile: " + out_filename);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // System.out.println("Consumer " + id + " stopped.");
    }
}
