import java.io.IOException;
import java.net.ServerSocket;

public class Producer {
	public static void main(String[] args) {
		int nPort = 4000;
		int p = 3;

		try {
			ServerSocket serverSocket = new ServerSocket(nPort);

			for (int i = 0; i < p; i++) {
				PThread thread = new PThread(i, serverSocket);
				thread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
