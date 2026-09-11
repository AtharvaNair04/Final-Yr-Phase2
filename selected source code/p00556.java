import java.util.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
	
	public static HashMap<String,String> map = new HashMap<>();
	static int N;
	static int M;
	static int[] ar;
	static int[] c;
	static int INF = 2 << 28;

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		N = sc.nextInt();
		M = sc.nextInt();
		ar = new int[N];
		c  = new int[M];
		for(int i = 0; i < N; i++) {
			ar[i] = sc.nextInt()-1;
			c[ar[i]]++;
		}
		

		int[][] dp = new int[M+1][1 << M];
		int[][] dd = new int[M+1][1 << M];
		int[][] ck = new int[M][N];
		
		for(int i = 0; i < dp.length; i++) {
			Arrays.fill(dp[i], INF);
			Arrays.fill(dd[i], INF);
		}
		for(int i = 0; i < M; i++) {
			Arrays.fill(ck[i], INF);
		}
		dp[0][0] = 0;
		dd[0][0] = 0;
		for(int i = 0; i < M; i++) {
			for(int j = 0; j < dp[i].length; j++) {
				if(dp[i][j] == INF) continue;
				int tmp = dd[i][j];
				for(int k = 0; k < M; k++) {
					if((j & (1 << k)) != 0) continue;
					int next = j & (1 << k);
					int sum  = 0;
					if(ck[k][tmp] == INF) {
						for(int l = 0; l < c[k]; l++) {
							if(ar[l + tmp] != k) sum++;
						}
						ck[k][tmp] = sum;
					} else {
						sum = ck[k][tmp];
					}
					dp[i+1][j | (1 << k)] = Math.min(dp[i+1][j | (1 << k)], dp[i][j] + sum);
					dd[i+1][j | (1 << k)] = tmp + c[k];
				}
			}
		}
		System.out.println(dp[dp.length-1][dp[0].length-1]);
		
	}
}
