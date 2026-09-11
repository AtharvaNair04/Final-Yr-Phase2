import java.util.*;

public class Main {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		int R = sc.nextInt();
		int C = sc.nextInt();
		int K = sc.nextInt();
		int[][] v = new int[R][C];
		
		for(int i=0; i<K; i++) {
			v[sc.nextInt()-1][sc.nextInt()-1] = sc.nextInt();
		}

		long[][][] dp = new long[R][C][4];
		if(v[0][0]>0)
			dp[0][0][1] = v[0][0];
		for(int i=0; i<R; i++) {
			for(int j=0; j<C; j++) {
				if(i>0) {
					for(int k=0; k<4; k++)
						dp[i][j][0] = Math.max(dp[i][j][0], dp[i-1][j][k]);
					if(v[i][j]>0) {
						for(int k=0; k<4; k++)
							dp[i][j][1] = Math.max(dp[i][j][1], dp[i-1][j][k]+v[i][j]);
					}
				}
				if(j>0) {
					for(int k=0; k<4; k++)
						dp[i][j][k] = Math.max(dp[i][j][k], dp[i][j-1][k]);
					if(v[i][j]>0) {
						for(int k=0; k<3; k++)
							dp[i][j][k+1] = Math.max(dp[i][j][k+1], dp[i][j-1][k]+v[i][j]);
					}
				}
			}
		}
		long ans = 0;
		for(int i=0; i<4; i++)
			ans = Math.max(ans, dp[R-1][C-1][i]);
		System.out.println(ans);
		
		sc.close();
	}
	
}
