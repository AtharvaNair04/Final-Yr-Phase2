import java.util.Scanner;

public class Main {
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		int a[][];
		int x, y, d;
		String st;

		a = new int[15][15];
		for (int i = 0; i < 9; i++) {
			st = sc.next();
			for (int j = 0; j < st.length(); j++) {
				a[i][j] = st.charAt(j) - '0';
			}
		}
		x = 2;
		y = 1;
		d = 1;
		boolean flag = true;
		while (flag || x != 2 || y != 1) {
			flag = false;
			if (d == 1) {
				System.out.print("R");
				if (1 < y && a[(y - 1) * 2 - 1][x - 1] == 1) {
					d = 4;
				} else if (a[(y - 1) * 2][x - 1] == 1) {
					x++;
				} else if (a[(y - 1) * 2 + 1][x - 1] == 1) {
					x++;
					y++;
					d = 2;
				} else {
					y++;
					d = 3;
				}
			} else if (d == 2) {
				System.out.print("D");
				if (a[(y - 1) * 2][x - 2] == 1) {
					d = 1;
				} else if (a[(y - 1) * 2 + 1][x - 2] == 1) {
					y++;
				} else if (2 < x && a[(y - 1) * 2][x - 3] == 1) {
					x--;
					y++;
					d = 3;
				} else {
					x--;
					d = 4;
				}
			} else if (d == 3) {
				System.out.print("L");
				if (a[(y - 1) * 2 - 1][x - 2] == 1) {
					d = 2;
				} else if (2 < x && a[(y - 1) * 2 - 2][x - 3] == 1) {
					x--;
				} else if (2 < y && a[(y - 1) * 2 - 3][x - 2] == 1) {
					x--;
					y--;
					d = 4;
				} else {
					y--;
					d = 1;
				}
			} else {
				System.out.print("U");
				if (1 < x && a[(y - 1) * 2 - 2][x - 2] == 1) {
					d = 3;
				} else if (2 < y && a[(y - 1) * 2 - 3][x - 1] == 1) {
					y--;
				} else if (1 < y && a[(y - 1) * 2 - 2][x - 1] == 1) {
					x++;
					y--;
					d = 1;
				} else {
					x++;
					d = 2;
				}
			}
		}
		System.out.println();
	}
}
