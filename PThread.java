import java.io.*;
import java.net.*;

public class PThread extends Thread {
	private int id;
	private ServerSocket serverSocket;

	public PThread(int id, ServerSocket serverSocket) {
		this.id = id;
		this.serverSocket = serverSocket;
	}

	public void run() {
		File upload_dir = new File("upload");
		File in_dir = new File(upload_dir, String.valueOf(id));

		try {
			Socket serverEndpoint = serverSocket.accept();
			DataOutputStream dos = new DataOutputStream(serverEndpoint.getOutputStream());
			
			File[] videoFiles = in_dir.listFiles((dir, name) -> name.toLowerCase().endsWith(".mp4") || name.toLowerCase().endsWith(".mov"));

			if (videoFiles == null || videoFiles.length == 0) {
				System.out.println("Producer Thread " + id + ": No files detected!");
			}

			for (File videoFile : videoFiles) {
				System.out.println("Read file! \tProducer Thread: " + id + "\tFile: " + videoFile);		
				
				long fileSize = videoFile.length();
                dos.writeLong(fileSize);
				
				FileInputStream fis = new FileInputStream(videoFile);

				byte[] buffer = new byte[1024];
				int bytesRead;
				
                while ((bytesRead = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, bytesRead);
                }

				dos.flush();
				fis.close();
			}

			dos.close();

			serverEndpoint.close();
			serverSocket.close();

			System.out.println("Producer " + id + " stopped.");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
