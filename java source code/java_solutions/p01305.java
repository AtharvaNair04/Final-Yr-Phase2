import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		for(int t=0;t<T;t++) {
			int[][] hand = new int[2][9];
			for(int i=0;i<2;i++) {
				for(int j=0;j<9;j++) {
					hand[i][j] = sc.nextInt();
				}
			}
			double[][][] dp = new double[2][180][1<<9];
			int now = 0;
			int nex = 1;
			dp[0][0][(1<<9)-1] = 1;
			for(int i=0;i<9;i++) {
				for(int j=0;j<180;j++) {
					for(int h1=0;h1<(1<<9);h1++) {
						if (Integer.bitCount(h1) != 9 - i) {
							continue;
						}
						if (dp[now][j][h1] == 0) {
							continue;
						}
						for(int c1=0;c1<9;c1++) {
							if ((h1&(1<<c1)) == 0) {
								continue;
							}
							int n1 = hand[0][c1];
							int n2 = hand[1][i];
							if (n1 > n2) {
								dp[nex][j+n1+n2][h1&~(1<<c1)] += dp[now][j][h1] * (1D / (9 - i));
							}else{
								dp[nex][j][h1&~(1<<c1)] += dp[now][j][h1] * (1D / (9 - i));
							}
						}
					}
				}
				for(int j=0;j<180;j++) {
					for(int h1=0;h1<(1<<9);h1++) {
						dp[now][j][h1] = 0;
					}
				}
				now = 1 - now;
				nex = 1 - nex;
			}
			double win = 0;
			for(int i=86;i<180;i++) {
				win += dp[now][i][0];
			}
			double lose = limit(1 - win);
			win = limit(win);
			System.out.println(win + " " + lose);
		}
		System.gc();
	}

	public static double limit(double x) {
		if (x > 1) {
			return 1;
		}else if(x < 0) {
			return 0;
		}
		return x;
	}

}
