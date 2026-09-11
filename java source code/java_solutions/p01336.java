import java.util.*;
import static java.lang.Math.*;
import static java.util.Arrays.*;

public class Main {

	int INF = 1 << 28;
	int n, c;
	int dp[];
	int max=0;
	int value[][];
	void run() {
		Scanner sc = new Scanner(System.in) ;
		for(;sc.hasNext();) {
			n = sc.nextInt();
			c = sc.nextInt();
			value = new int[n][4];
			max = 0;
			for(int i=0;i<n;i++) {
				while(!sc.hasNextInt())sc.next();
				for(int j=0;j<4;j++) value[i][j] = sc.nextInt();
			}
			for(int i=1;i<=3;i++) {
				dp = new int [c+1];
				fill(dp, -1);
				dp[0] = 0;
				for(int j=0;j<=c;j++) for(int k=0;k<n;k++) {
					if(dp[j]>=0 && j+value[k][0]<=c) dp[j+value[k][0]] = max(dp[j+value[k][0]], dp[j] + value[k][i]);
				}
				for(int j=c;j>=0;j--) {
					if(dp[j]>0) {
						max = max(max, dp[j]);
					}
				}
			}
			System.out.println(max);
		}
	}

	public static void main(String[] args) {
		new Main().run();
	}
}
