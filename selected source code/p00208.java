import java.io.IOException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {
		new Main().run();
	}

	private void run() throws IOException {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			int n = scanner.nextInt();
			if(n==0)
				break;
			char[] chs = Integer.toOctalString(n).toCharArray();
			for (int i = 0;i<chs.length;i++) {
				if (chs[i] >= '4') {
					chs[i]++;
				}
				if (chs[i] >= '6') {
					chs[i]++;
				}
			}
			System.out.println(chs);
		}
	}
}
