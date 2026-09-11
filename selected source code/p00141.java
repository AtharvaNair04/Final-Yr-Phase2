import java.util.Scanner;

public class Main{
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		int n;
		int a[][];
		String enter = "";

		n = sc.nextInt();
		for (int i = 0; i < n ;i++) {
			int k = sc.nextInt();
			a = new int[k][k];
			int x = 0;
			int y = k - 1;
			int dir = 1;
			if (k == 1) {
				a[0][0] = 1;
			} else if (k == 2) {
				a[0][0] = 1;
				a[0][1] = 1;
				a[1][0] = 1;
				a[1][1] = 0;
			} else {
				while (true) {
					a[y][x] = 1;
					if (dir == 1) {
						if (y == 0 || (0 <= y - 2 && a[y - 2][x] == 1)) {
							if (a[y][x + 2] == 1 || a[y + 1][x + 1] == 1) {
								break;
							} else {
								dir = 2;
								x++;
							}
						} else {
							y--;
						}
					} else if (dir == 2) {
						if (x == k - 1 || (x + 2 < k && a[y][x + 2] == 1)) {
							if (a[y + 2][x] == 1 || a[y + 1][x - 1] == 1) {
								break;
							} else {
								dir = 3;
								y++;
							}
						} else {
							x++;
						}
					} else if (dir == 3) {
						if (y == k - 1 || (y + 2 < k && a[y + 2][x] == 1)) {
							if (a[y][x - 2] == 1 || a[y - 1][x - 1] == 1) {
								break;
							} else {
								dir = 4;
								x--;
							}
						} else {
							y++;
						}
					} else {
						if (x == 0 || (0 <= x - 2 && a[y][x - 2] == 1)) {
							if (a[y - 2][x] == 1 || a[y - 1][x + 1] == 1) {
								break;
							} else {
								dir = 1;
								y--;
							}
						} else {
							x--;
						}
					}
				}
			}
			System.out.print(enter);
			for (int j = 0; j < k; j++) {
				for (int l = 0; l < k; l++) {
					System.out.print((a[j][l] == 1) ? "#" : " ");
				}
				System.out.println();
			}
			enter = "\n";
		}
	}
}
