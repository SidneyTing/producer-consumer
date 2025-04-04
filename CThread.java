import java.io.*;
import java.net.*;

public class CThread implements Runnable {
	private int id;
    private ServerSocket serverSocket;

	public CThread(int id, ServerSocket serverSocket) {
		this.id = id;
        this.serverSocket = serverSocket;
	}

    public void run() {
        int fileCtr = 0;

        File saved_dir = new File("saved");

        if (!saved_dir.exists()) {
            saved_dir.mkdirs();
        }

        try {
            Socket serverEndpoint = serverSocket.accept();
            DataInputStream dis = new DataInputStream(serverEndpoint.getInputStream());

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
            serverEndpoint.close();
            serverSocket.close();

            System.out.println("Consumer " + id + " stopped.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
