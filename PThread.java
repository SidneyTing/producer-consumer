import java.io.*;
import java.net.*;

public class PThread implements Runnable {
	private int id;
	private String host;

	public PThread(int id, String host) {
		this.id = id;
		this.host = host;
	}

	public void run() {
		int port = 4000;

		File upload_dir = new File("upload");
		File in_dir = new File(upload_dir, String.valueOf(id));

		if (!in_dir.exists()) {
            in_dir.mkdirs();
        }

        System.out.println("Producer " + id + " listening for Consumers...");
        Socket clientEndpoint = null;
        do {
            try {
                clientEndpoint = new Socket(host, port);
            } catch (IOException e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}
            }
        } while (clientEndpoint == null);
        System.out.println("Connected to Consumer " + id + "!");

		File[] videoFiles = in_dir.listFiles((dir, name) -> name.toLowerCase().endsWith(".mp4") || name.toLowerCase().endsWith(".mov"));
		if (videoFiles == null || videoFiles.length == 0) {
			System.out.println("No files detected!" + "\tProducer Thread: " + id + "\tDirectory: " + in_dir);

		} else {
			try {
				DataOutputStream dos = new DataOutputStream(clientEndpoint.getOutputStream());
				for (File videoFile : videoFiles) {
					System.out.println("Read file! \tProducer Thread: " + id + "\tFile: " + videoFile);		
					
					dos.writeLong(videoFile.length());
					
					FileInputStream fis = new FileInputStream(videoFile);
	
					byte[] buffer = new byte[1024];
					int bytesRead;
					
					while ((bytesRead = fis.read(buffer)) != -1) {
						dos.write(buffer, 0, bytesRead);
					}
	
					dos.flush();
					fis.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			clientEndpoint.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}