public class Consumer {
    public static void main(String[] args) {
        int c = 3;

		for (int i = 0; i < c; i++) {
			CThread thread = new CThread(i);
			thread.start();
		}
    }
}
