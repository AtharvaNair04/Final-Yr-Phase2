import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n;
		String name[];
		int d[][];
		int score[];

		while (true) {
			n = sc.nextInt();
			if (n == 0) {
				break;
			}

			name = new String[n];
			d = new int[n][30];
			score = new int[n];

			for (int i = 0; i < n; i++) {
				name[i] = sc.next();
				int k = sc.nextInt();
				for (int j = 0; j < k; j++) {
					d[i][sc.nextInt()] = 1;
				}
			}

			for (int i = 0; i < 30; i++) {
				int p = n + 1;
				for (int j = 0; j < n; j++) {
					p -= d[j][i];
				}
				for (int j = 0; j < n; j++) {
					if (d[j][i] == 1) {
						score[j] += p;
					}
				}
			}

			int minId = 0;
			for (int i = 0; i < n; i++) {
				if (score[i] < score[minId] || (score[i] == score[minId] && name[i].compareTo(name[minId]) < 0)) {
					minId = i;
				}
			}

			System.out.println(score[minId] + " " + name[minId]);
		}
	}
}
