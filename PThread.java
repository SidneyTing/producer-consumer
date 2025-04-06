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
                } catch (InterruptedException e1) {}
            }
        } while (clientEndpoint == null);
        System.out.println("Connected to Consumer " + id + "!");

		File[] videoFiles = in_dir.listFiles((dir, name) -> name.endsWith(".mp4") || name.endsWith(".mov"));
		if (videoFiles == null || videoFiles.length == 0) {
			System.out.println("No files detected!" + "\tProducer Thread: " + id + "\tDirectory: " + in_dir);

		} else {
			try {
				ObjectOutputStream oos = new ObjectOutputStream(clientEndpoint.getOutputStream());

				for (File videoFile : videoFiles) {
					System.out.println("Read file! \tProducer Thread: " + id + "\tFile: " + videoFile);	

					String videoName = videoFile.getName();

					String title = videoName.substring(0, videoName.lastIndexOf('.'));
					byte[] bytes = Helper.convertFileToBytes(videoFile);
					String format = videoName.substring(videoName.lastIndexOf('.') + 1, videoName.length());

					oos.writeObject(new VideoData(title, bytes, format));
	
					oos.flush();
				}

				oos.close();

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