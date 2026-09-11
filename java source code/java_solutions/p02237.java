import java.util.Scanner;

public class Main {
	void run() {
		Scanner sc = new Scanner(System.in);
		int u, k, v;
		int n = sc.nextInt();
		int[][] M = new int[n][n];
		for(int i = 0; i < n; i++) {
			u = sc.nextInt();
			k = sc.nextInt();
			u--;
			for(int j = 0; j < k; j++) {
				v = sc.nextInt();
				v--;
				M[u][v] = 1;
			}
		}

		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				if(j != 0) {
					System.out.print(" ");
				}
				System.out.print(M[i][j]);
			}

			System.out.println("");
		}
		sc.close();
	}

	public static void main(String[] args) {
		new Main().run();
	}
}
