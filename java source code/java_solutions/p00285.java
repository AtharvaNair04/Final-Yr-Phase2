import java.util.*;
public class Main {
	static String S;
	static int p;
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while (true) {
			int m = sc.nextInt();
			int w = sc.nextInt();
			if(m == 0 && w == 0) break;
			long[] bm = new long[m];
			long[] bw = new long[w];
			
			for (int i = 0; i < m; i++) {
				bm[i] = sc.nextLong();
			}
			for (int i = 0; i < w; i++) {
				bw[i] = sc.nextLong();
			}
			long[][] dp = new long[m+1][1 << w];
			for(int i = 0; i < m+1; i++) {
				Arrays.fill(dp[i], -1);
			}
			dp[0][0] = 0L;
			for(int i = 0; i < m; i++) {
				for(int j = 0; j < (1 << w); j++) {
					if(dp[i][j] == -1) continue;
					dp[i+1][j] = Math.max(dp[i+1][j], dp[i][j]);
					for(int k = 0; k < w; k++) {
						if((j & (1 << k)) != 0) continue;
						dp[i+1][j | (1 << k)] = Math.max(dp[i+1][j | (1 << k)], dp[i][j] + Math.abs(bm[i] - bw[k]) * ((Math.abs(bm[i] - bw[k])-30) * (Math.abs(bm[i] - bw[k])-30)));
					}
				}
			}
			long max = 0;
			for(int i = 0; i < (1 << w); i++) {
				max = Math.max(dp[m][i], max);
			}

			System.out.println(max);
		}




	}

}
