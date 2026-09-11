import java.util.*;
import java.io.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.lang.Math.*;

public class Main {

	int INF = 1 << 28;
	//long INF = 1L << 62;
	double EPS = 1e-10;
	int s, t, u, n, o, d;
	int[] e, c, h, a;

	void run() {
		Scanner sc = new Scanner(System.in);
		s = sc.nextInt(); t = sc.nextInt(); u = sc.nextInt(); n = sc.nextInt(); o = sc.nextInt(); d = sc.nextInt();
		e = new int[t]; c = new int[t]; h = new int[n]; a = new int[n];
		
		for (int i=0;i<t;i++) {
			e[i] = sc.nextInt(); c[i] = sc.nextInt();
		}
		
		for (int i=0;i<n;i++) {
			h[i] = sc.nextInt(); a[i] = sc.nextInt();
		}
		
		int[][] stamina = new int[s+1][u+1];
		for (int[] a : stamina) fill(a, -1);
		stamina[0][0] = 0;
		
		for (int l=0;l<t;l++) for (int j=s-e[l];j>=0;j--) for (int k=0;k<u;k++) if (stamina[j][k] >= 0){
			stamina[j+e[l]][k+1] = max(stamina[j+e[l]][k+1], stamina[j][k] + c[l]);
		}
//		for (int[] a: stamina) debug(a);
		int max_cal = 0;
		for (int i=0;i<=s;i++) max_cal = max(max_cal, stamina[i][u]);
		int cal = max_cal;
		int[] happy = new int[cal+1];
		fill(happy, -1); happy[0] = 0;
		
		for (int j=0;j<=cal;j++) for (int i=0;i<n;i++) if (j+a[i] <= cal){
			happy[j+a[i]] = max(happy[j+a[i]], happy[j] + h[i]);
			happy[j+1] = max(happy[j+1], happy[j]);
		}
		
//		debug(happy);
		
		int[][] dp = new int[2][s+1];
		fill(dp[0], -1); dp[0][s] = 0;
		int x = 0;
		for (int i=0;i<d;i++) {
			fill(dp[1-x], -1);
			for (int j=0;j<=s;j++) if (dp[x][j] >= 0){
				for (int k=0;k<=j;k++) if (stamina[k][u] >= 0){
					dp[1-x][min(s, j-k+o)] = max(dp[1-x][min(s, j-k+o)], dp[x][j] + happy[stamina[k][u]]);
				}
			}
//			debug(dp[1-x]);
			x = 1 - x;
		}
		
		int ret = -1;
		for (int i=0;i<=s;i++) ret = max(ret, dp[x][i]);
		
		System.out.println(ret);
	}

	void debug(Object... os) {
		System.err.println(Arrays.deepToString(os));
	}

	public static void main(String[] args) {
		new Main().run();
	}
}
