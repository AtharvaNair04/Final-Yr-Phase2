import java.util.*;
public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		int n = sc.nextInt();
		int W = sc.nextInt();
		int w[] = new int[n+1];
		int v[] = new int[n+1];
		int dp[][] = new int[n+1][3*n+1];
		
		int w1 = sc.nextInt();
		w[1] = 0;
		v[1] = sc.nextInt();
		for(int i=2;i<=n;i++){
			w[i] = sc.nextInt() - w1;
			v[i] = sc.nextInt();
		}
		
		for(int i=1;i<=n;i++){
			for(int j=i;j>=1;j--){
				for(int k=w[i];k<=3*n;k++){
					dp[j][k] = Math.max(dp[j-1][k-w[i]] + v[i], dp[j][k]);
				}
			}
		}
		
		
		int ans = 0;
		for(int i=1;i<=n &&w1 * i <= W;i++){
			int t = W - w1 * i;
			t = Math.min(t, 3*n);
			ans = Math.max(ans, dp[i][t]);
		}
		System.out.println(ans);
	}

}
