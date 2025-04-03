import java.io.*;
import java.net.*;

public class CThread extends Thread {
	private int id;

	public CThread(int id) {
		this.id = id;
	}

    public void run() {
        int nPort = 4000;
        int fileCtr = 0;

        File saved_dir = new File("saved");

        if (!saved_dir.exists()) {
            saved_dir.mkdirs();
        }

        try {
            Socket clientEndpoint = new Socket("localhost", nPort);
            DataInputStream dis = new DataInputStream(clientEndpoint.getInputStream());

            while (true) {
                String out_filename = "Consumer" + id + "_" + fileCtr + ".mov";

                long fileSize;
                try {
                    fileSize = dis.readLong();
                } catch (EOFException e) {
                    System.out.println("Consumer " + id + " stopped.");
                    break;
                }

                File out_file = new File(saved_dir, out_filename);
                FileOutputStream fos = new FileOutputStream(out_file);

                byte[] buffer = new byte[1024];
                long remaining = fileSize;
    
                while (remaining > 0) {
                    int bytesRead = dis.read(buffer, 0, (int) Math.min(buffer.length, remaining));
                    
                    fos.write(buffer, 0, bytesRead);
                    remaining -= bytesRead;
                }

                System.out.println("Saved file! \tConsumer Thread: " + id + "\tFile: " + out_filename);
    
                fos.close();
                fileCtr++;
            }
            
            dis.close();
            clientEndpoint.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
