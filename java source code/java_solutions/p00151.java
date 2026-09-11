import java.util.Scanner;

public class Main{
	static class Dp {
		int number;
		int height, width, slopeP, slopeN;

		Dp(int number) {
			this.number = number;
			this.height = 0;
			this.width  = 0;
			this.slopeP = 0;
			this.slopeN = 0;
		}
	}

	public static void main(String srgs[]) {
		Scanner sc = new Scanner(System.in);
		int n;
		Dp a[][];
		int max;

		while (true) {
			n = sc.nextInt();
			if (n == 0) {
				break;
			}
			a = new Dp[n][n];
			max = 0;
			for (int i = 0; i < n; i++) {
				String s = sc.next();
				for (int j = 0; j < n; j++) {
					a[i][j] = new Dp(s.charAt(j) - '0');
					if (a[i][j].number == 1) {
						if (i == 0 && j == 0) {
							a[i][j].height = 1;
							a[i][j].width  = 1;
							a[i][j].slopeN = 1;
							a[i][j].slopeP = 1;
						} else if (i == 0) {
							a[i][j].height = 1;
							a[i][j].width  = a[i][j - 1].width + 1;
							a[i][j].slopeN = 1;
							a[i][j].slopeP = 1;
						} else if (j == 0) {
							a[i][j].height = a[i - 1][j].height + 1;
							a[i][j].width  = 1;
							a[i][j].slopeN = 1;
							a[i][j].slopeP = a[i - 1][j + 1].slopeP + 1;
						} else {
							a[i][j].height = a[i - 1][j].height + 1;
							a[i][j].width  = a[i][j - 1].width + 1;
							a[i][j].slopeN = a[i - 1][j - 1].slopeN + 1;
							if (j == n - 1) {
								a[i][j].slopeP = 1;
							} else {
								a[i][j].slopeP = a[i - 1][j + 1].slopeP + 1;
							}
						}
					}
					if (max < a[i][j].height) {
						max = a[i][j].height;
					}
					if (max < a[i][j].width) {
						max = a[i][j].width;
					}
					if (max < a[i][j].slopeP) {
						max = a[i][j].slopeP;
					}
					if (max < a[i][j].slopeN) {
						max = a[i][j].slopeN;
					}
				}
			}
			System.out.println(max);
		}
	}
}
