import java.util.*;

public class Main {
	static class Range {
		int size;
		long lv, rv;
		int lp, rp;
		public Range(int size, long lv, long rv) {
			this.size = size;
			this.lv = lv;
			this.rv = rv;
		}
		
	}
 
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
 
		int N = sc.nextInt();
		long[] A = new long[N];
		for(int i=0; i<N; i++)
			A[i] = sc.nextLong();
		
		long[][] dp = new long[2][];
		
		dp[0] = dp(A);
		long[] B = new long[A.length];
		for(int i=0; i<N; i++)
			B[i] = A[N-1-i];
		dp[1] = dp(B);
		
		long ans = Long.MAX_VALUE;
		for(int i=0; i<=N; i++)
			ans = Math.min(ans, dp[0][i]+dp[1][N-i]+i);
		
		System.out.println(ans);
		sc.close();
	}
	
	public static long[] dp(long[] A) {
		int N = A.length;
		long[] ans = new long[N+1];
		
		LinkedList<Range> list = new LinkedList<>();
		ans[N] = 0;
		for(int i=N-1; i>=0; i--) {
			ans[i] = ans[i+1];
			list.push(new Range(1, A[i], A[i]));
			
			while(list.size() > 1) {
				Range cur = list.pop();
				if(compare(cur.rv, cur.rp, list.peek().lv, list.peek().lp)<=0) {
					list.push(cur);
					break;
				}
				Range next = list.pop();
				while(compare(cur.rv, cur.rp, next.lv, next.lp)>0) {
					next.lp+=2;
					next.rp+=2;
					ans[i] += 2*next.size;
				}
				cur.size += next.size;
				cur.rv = next.rv;
				cur.rp = next.rp;
				list.push(cur);
			}
		}
 
		return ans;
	}
	
	public static int compare(long lv, int lp, long rv, int rp) {
		int pdiff = lp-rp;
		if(pdiff>=0) {
			if(pdiff>=30)
				return 1;
			else
				return Long.compare(lv<<pdiff, rv);
		} else {
			if(pdiff<=-30)
				return -1;
			else
				return Long.compare(lv, rv<<(-pdiff));
		}
	}
}
