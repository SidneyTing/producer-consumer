import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class VideoData implements Serializable {
    private final String title;
    private final byte[] bytes;
    private final String format;
    private final String hash;

    public VideoData(String title, byte[] bytes, String format) {
        this.title = title;
        this.bytes = bytes;
        this.format = format;
        this.hash = generateHash(bytes);
    }

    private String generateHash(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(data);
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getTitle() {
        return title;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public String getFormat() {
        return format;
    }

    public String getHash() {
        return hash;
    }
}
