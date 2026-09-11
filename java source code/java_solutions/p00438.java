import java.util.*;

public class Main {
	
	public static void main (String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		while (true) {
			int w = sc.nextInt();
			int h = sc.nextInt();
			if ((w | h) == 0) break;
			long[][] path = new long[h][w];
			int n = sc.nextInt();
			for (int i = 0; i < n; i++) {
				int x = sc.nextInt() - 1;
				int y = sc.nextInt() - 1;
				path[y][x] = -1;
			}
			
			for (int i = 0; i < h; i++) {
				if (path[i][0] == -1) break;
				path[i][0] = 1;
			}
			for (int i = 0; i < w; i++) {
				if (path[0][i] == -1) break;
				path[0][i] = 1;
			}
			for (int i = 1; i < h; i++) {
				for (int j = 1; j < w; j++) {
					if (path[i][j] == -1) continue;
					long a = path[i - 1][j] == -1 ? 0 : path[i - 1][j];
					long b = path[i][j - 1] == -1 ? 0 : path[i][j - 1];
					path[i][j] += a + b;
				}
			}
//			for (int i = 0; i < h; i++) {
//				for (int j = 0; j < w; j++) {
//					System.out.printf("%12d ", path[i][j]);
//				}
//				System.out.println();
//			}
			System.out.println(path[h - 1][w - 1]);
		}
		
	}
	
}
