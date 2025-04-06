import java.io.*;
import java.util.concurrent.*;
// import java.util.zip.*;

public class CThread implements Runnable {
	private int id;
    private BlockingQueue<VideoData> queue;
    // private VideoCompressor compressor;

	public CThread(int id, BlockingQueue<VideoData> queue) {
		this.id = id;
        this.queue = queue;
        // this.compressor = new VideoCompressor(0.1f); 
    }

    public void run() {
        File saved_dir = new File("saved");

        if (!saved_dir.exists()) {
            saved_dir.mkdirs();
        }

        while (true) {
            try {
                VideoData video = queue.take();

                String hash = video.getHash();
                
                if (Consumer.processedFiles.contains(hash)) {
                    System.out.println("Duplicate file detected! Skipping: " + video.getTitle() + "." + video.getFormat());
                    continue;
                }
      
                Consumer.processedFiles.add(hash);

                // byte[] originalBytes = video.getBytes();
                // byte[] compressedBytes = originalBytes;
                
                // try {
                //     long startTime = System.currentTimeMillis();
                //     compressedBytes = compressor.compressVideo(originalBytes);
                //     long endTime = System.currentTimeMillis();
                    
                //     System.out.println("Compression completed in " + (endTime - startTime) + "ms");
                // } catch (Exception e) {
                //     System.err.println("Compression failed: " + e.getMessage());
                //     compressedBytes = originalBytes;
                // }
                
                String title = video.getTitle();
                byte[] videoBytes = video.getBytes();
                String format = video.getFormat();

                String out_filename = title + "." + format;
                File out_file = new File(saved_dir, out_filename);

                try (FileOutputStream fos = new FileOutputStream(out_file)) {
                    fos.write(videoBytes);
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
