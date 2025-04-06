import java.io.*;
import java.util.concurrent.*;

public class VideoCompressor {

    public static byte[] compressVideoBytes(byte[] inputVideoBytes) throws IOException, InterruptedException {
        // FFmpeg command to compress video (H.264 codec)
        ProcessBuilder processBuilder = new ProcessBuilder(
            "ffmpeg",
            "-i", "pipe:0",          // Read from stdin
            "-vcodec", "libx264",    // Use H.264 codec
            "-crf", "28",            // Quality (18-28 is a good range)
            "-preset", "fast",       // Balance between speed/compression
            "-f", "mp4",             // Output format
            "pipe:1"                 // Write to stdout
        );

        Process process = processBuilder.start();

        // Write input bytes to FFmpeg's stdin in a separate thread
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future = executor.submit(() -> {
            try (OutputStream stdin = process.getOutputStream()) {
                stdin.write(inputVideoBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Read compressed output bytes
        ByteArrayOutputStream compressedBytes = new ByteArrayOutputStream();
        try (InputStream stdout = process.getInputStream()) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = stdout.read(buffer)) != -1) {
                compressedBytes.write(buffer, 0, bytesRead);
            }
        }

        // Wait for completion
        future.get();  // Ensure input writing is done
        executor.shutdown();
        process.waitFor();

        if (process.exitValue() == 0) {
            return compressedBytes.toByteArray();
        } else {
            throw new IOException("FFmpeg compression failed");
        }
    }

}