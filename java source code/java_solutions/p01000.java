import java.util.Scanner;

public class Main {
	static int n;
	static int s[][][];
	static int s2[][][];
	static int m1, m2;
	static int a[], b[];

	public static boolean isBorn(int x, int y, int z) {
		int sum = 0;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				for (int k = -1; k <= 1; k++) {
					if (0 <= z + i && z + i < 5 && 0 <= y + j && y + j < 5 && 0 <= x + k && x + k < 5) {
						sum += s[z + i][y + j][x + k];
					}
				}
			}
		}
		for (int i = 0; i < m1; i++) {
			if (sum == a[i]) {
				return true;
			}
		}
		return false;
	}

	public static boolean isDeath(int x, int y, int z) {
		int sum = -1;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				for (int k = -1; k <= 1; k++) {
					if (0 <= z + i && z + i < 5 && 0 <= y + j && y + j < 5 && 0 <= x + k && x + k < 5) {
						sum += s[z + i][y + j][x + k];
					}
				}
			}
		}
		for (int i = 0; i < m2; i++) {
			if (sum == b[i]) {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int cases = 1;
		String sss = "";

		while (true) {
			n = sc.nextInt();
			if (n == 0) {
				break;
			}
			s = new int[5][5][5];
			s2 = new int[5][5][5];
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 5; j++) {
					String str = sc.next();
					for (int k = 0; k < 5; k++) {
						s[i][j][k] = str.charAt(k) - '0';
					}
				}
			}
			m1 = sc.nextInt();
			a = new int[m1];
			for (int i = 0; i < m1; i++) {
				a[i] = sc.nextInt();
			}
			m2 = sc.nextInt();
			b = new int[m2];
			for (int i = 0; i < m2; i++) {
				b[i] = sc.nextInt();
			}

			while (n-- != 0) {
				for (int i = 0; i < 5; i++) {
					for (int j = 0; j < 5; j++) {
						for (int k = 0; k < 5; k++) {
							if (s[i][j][k] == 0) {
								if (isBorn(k, j, i)) {
									s2[i][j][k] = 1;
								} else {
									s2[i][j][k] = 0;
								}
							} else if (s[i][j][k] == 1) {
								if (isDeath(k, j, i)) {
									s2[i][j][k] = 0;
								} else {
									s2[i][j][k] = 1;
								}
							}
						}
					}
				}
				for (int i = 0; i < 5; i++) {
					for (int j = 0; j < 5; j++) {
						for (int k = 0; k < 5; k++) {
							s[i][j][k] = s2[i][j][k];
						}
					}
				}
			}

			System.out.print(sss);
			sss = "\n";
			System.out.println("Case " + cases + ":");
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 5; j++) {
					for (int k = 0; k < 5; k++) {
						System.out.print(s[i][j][k]);
					}
					System.out.println();
				}
				if (i != 4) {
					System.out.println();
				}
			}
			cases++;
		}
	}
}
