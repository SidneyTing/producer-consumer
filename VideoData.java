import java.io.*;

public class VideoData implements Serializable {
    private final String title;
    private final byte[] bytes;
    private final String format;

    public VideoData(String title, byte[] bytes, String format) {
        this.title = title;
        this.bytes = bytes;
        this.format = format;
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
}
