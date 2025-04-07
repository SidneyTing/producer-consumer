import java.io.*;

public class VideoCompressor {
    
    private static final int CRF = 28;  // lower is better quality
    
    // Preset (faster = less compression, slower = better compression)
    private static final String PRESET = "fast"; // ultrafast, superfast, veryfast, faster, fast, medium, slow, slower
    
  
    public static byte[] compressVideo(byte[] inputVideoBytes) throws IOException {
        File tempInput = File.createTempFile("input", ".mp4");
        File tempOutput = File.createTempFile("output", ".mp4");
        
        try {
            try (FileOutputStream fos = new FileOutputStream(tempInput)) {
                fos.write(inputVideoBytes);
            }
            
            ProcessBuilder pb = new ProcessBuilder(
                "ffmpeg",
                "-i", tempInput.getAbsolutePath(),
                "-c:v", "libx264",
                "-crf", String.valueOf(CRF),
                "-preset", PRESET,
                "-c:a", "copy",
                "-y",
                tempOutput.getAbsolutePath()
            );
            
            pb.redirectErrorStream(true);
            
            System.out.println("Starting FFmpeg compression...");
            long startTime = System.currentTimeMillis();
            
            Process process = pb.start();
            
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("frame=")) {
                        System.out.print("\rProgress: " + line);
                    }
                }
            }
            
            int exitCode = process.waitFor();
            long endTime = System.currentTimeMillis();
            
            if (exitCode != 0) {
                throw new IOException("FFmpeg process failed with exit code " + exitCode);
            }
            
            System.out.println("\nCompression completed in " + (endTime - startTime) + "ms");
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (FileInputStream fis = new FileInputStream(tempOutput)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesRead);
                }
            }
            
            byte[] compressedBytes = baos.toByteArray();
            System.out.printf("Compressed from %s to %s \n",
                    formatFileSize(inputVideoBytes.length),
                    formatFileSize(compressedBytes.length));
                    
            return compressedBytes;
            
        } catch (Exception e) {
            // e.printStackTrace();
            System.err.println("Video compression failed! Falling back to original video data:\n" + e.getMessage());
            return inputVideoBytes; 
        } finally {
            if (tempInput.exists()) tempInput.delete();
            if (tempOutput.exists()) tempOutput.delete();
        }
    }
    
    private static String formatFileSize(long size) {
        if (size < 1024) return size + " B";
        int exp = (int) (Math.log(size) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp-1) + "";
        return String.format("%.1f %sB", size / Math.pow(1024, exp), pre);
    }
}