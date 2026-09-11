import java.util.*;
public class Main {
	static long mod = 1000000007;
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		n = sc.nextInt();
		k = sc.nextInt();
		System.out.println(solve());
	}
	static long[][] dp1, dp2; 
	static int n, k, m;
	static long solve(){
		m = (int) Math.sqrt(n);
		dp1 = new long[k][m+1];
		dp2 = new long[k][m+1];
		for(int r = k-1; r >= 0; r--){
			if(r == k-1){
				for(int t = 1; t <= m; t++){
					dp1[r][t] = t;
				}
				for(int _t = m; _t >= 1; _t--){
					int t = n / _t;
					dp2[r][_t] = t;
				}
			}
			else{
				for(int t = 1; t <= m; t++){
					long res = ((t > 1 ? get(r, t-1) : 0) + get(r+1, n / t)) % mod;
					put(r, t, res);
				}
				for(int _t = m; _t >= 1; _t--){
					int t = n / _t;
					
					int c = n / t;
					int a = n / (c + 1);
					long res = (get(r, a) + ((t - a) * get(r+1, c)) % mod) % mod;
					put(r, t, res);
				}
			}
		}
		return get(0, n);
	}
	static long get(int r, int t){
		if(t <= m) return dp1[r][t];
		else return dp2[r][n / t];
	}
	static void put(int r, int t, long val){
		if(t <= m) dp1[r][t] = val;
		else dp2[r][n / t] = val;
	}
}
//100 10
//554261130
