import java.util.Scanner;

public class Main{
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		int n;
		int a[][] = new int[9][9];
		int b[][] = new int[9][9];
		String str;

		n = sc.nextInt();
		str = "";
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < 9; j++) {
				for (int k = 0; k < 9; k++) {
					a[j][k] = sc.nextInt();
					b[j][k] = 0;
				}
			}

			for (int j = 0; j < 9; j++) {
				for (int k = 0; k < 9; k++) {
					for (int l = 0; l < 9; l++) {
						if (l != k && a[j][k] == a[j][l]) {
							b[j][k] = 1;
						}
						if (l != j && a[j][k] == a[l][k]) {
							b[j][k] = 1;
						}
					}
					for (int l = (j / 3) * 3; l < (j / 3) * 3 + 3; l++) {
						for (int m = (k / 3) * 3; m < (k / 3) * 3 + 3; m++) {
							if ((j != l || k != m) && a[j][k] == a[l][m]) {
								b[j][k] = 1;
							}
						}
					}
				}
			}

			String s = "";
			System.out.print(str);
			for (int j = 0; j < 9; j++) {
				for (int k = 0; k < 9; k++) {
					if (b[j][k] == 0) {
						s = " ";
					} else {
						s = "*";
					}
					System.out.print(s + a[j][k]);
				}
				System.out.println();
			}
			str = "\n";
		}
	}
}
