import java.util.*;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int H = sc.nextInt();
		int W = sc.nextInt();
		char[][] c = new char[H][W];
		for(int i=0; i<H; i++)
			c[i] = sc.next().toCharArray();
	
		int ans = 0;
		
		for(int i=0; i<W-1; i++) {
			int[][] dp = new int[H+1][H+1];
			for(int j=0; j<H+1; j++)
				Arrays.fill(dp[j], Integer.MAX_VALUE);
			int[][] match = new int[H+1][H+1];
			
			dp[0][0] = 0;
			for(int j=0; j<=H; j++) {
				for(int k=0; k<=H; k++) {
					if(j<H) {
						if(k>0)
							match[j+1][k] = match[j][k-1] + (c[j][i]==c[k-1][i+1] ? 1 : 0);
						dp[j+1][k] = Math.min(dp[j+1][k], dp[j][k] + match[j+1][k]);
					}
					if(k<H) {
						if(j>0)
							match[j][k+1] = match[j-1][k] + (c[j-1][i]==c[k][i+1] ? 1 : 0);
						dp[j][k+1] = Math.min(dp[j][k+1], dp[j][k] + match[j][k+1]);
					}
				}
			}
			ans += dp[H][H];
		}
		
		System.out.println(ans);
		sc.close();
	}
}
