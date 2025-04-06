import java.io.*;

public class Helper {
    public static byte[] convertFileToBytes(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = fis.read(buffer)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }

        byte[] bytes = baos.toByteArray();

        baos.flush();
        baos.close();
        fis.close();

        return bytes;
    }
}
