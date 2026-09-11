import java.util.Scanner;

public class Main {
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		int n, m;
		int s, g;
		int barData[][];

		n = sc.nextInt();
		while (n != 0) {
			s = sc.nextInt();
			g = sc.nextInt();
			m = sc.nextInt();
			barData = new int[m][n - 1];
			for (int i = 0; i < m; i++) {
				String st = sc.next();
				for (int j = 0; j < n - 1; j++) {
					barData[i][j] = st.charAt(j) - '0';
				}
			}
			if (check(s, g, n, m, barData)) {
				System.out.println("0");
				n = sc.nextInt();
				continue;
			}
			boolean flag = true;
			for (int i = 0; i < m && flag; i++) {
				for (int j = 0; j < n - 1 && flag; j++) {
					if (barData[i][j] == 1) {
						continue;
					} else {
						boolean left = false;
						boolean right = false;
						if (j == 0 || barData[i][j - 1] == 0) {
							left = true;
						}
						if (j == n - 2 || barData[i][j + 1] == 0) {
							right = true;
						}
						if (left && right) {
							barData[i][j] = 1;
							if (check(s, g, n, m, barData)) {
								System.out.println((i + 1) + " " + (j + 1));
								flag = false;
							}
							barData[i][j] = 0;
						}
					}
				}
			}
			if (flag) {
				System.out.println("1");
			}
			n = sc.nextInt();
		}
	}

	public static boolean check(int s, int g, int n, int m, int barData[][]) {
		int x = s;
		for (int i = 0; i < m; i++) {
			if (x - 1 < n - 1 && barData[i][x - 1] == 1) {
				x++;
			} else if (0 <= x - 2 && barData[i][x - 2] == 1) {
				x--;
			}
		}
		if (x == g) {
			return true;
		} else {
			return false;
		}
	}
}
