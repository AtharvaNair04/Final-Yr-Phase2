import static java.lang.System.*;
import java.util.*;

public class Main {
	public static final int MOD = 1000000007;
	
    public static void main(String[] args) {
        Scanner sc = new Scanner(in);

        int n = sc.nextInt();
		int[] stone = new int[n];
		for(int i=0; i<n; i++) stone[i] = sc.nextInt()-1;
		
		int[] next = new int[n];
		Arrays.fill(next, -1);
		int[] last = new int[200000];
		Arrays.fill(last, -1);
		for(int i=n-1; i>=0; i--) {
			if(last[stone[i]] > i+1) 
				next[i] = last[stone[i]];
			last[stone[i]] = i;
		}
		
		long[] dp = new long[n];
		Arrays.fill(dp, 0L);
		dp[0] = 1L;
		
		for(int i=0; i<n-1; i++) {
			dp[i+1] += dp[i];
			dp[i+1] %= MOD;
			
			if(next[i] != -1) {
				dp[next[i]] += dp[i];
				dp[next[i]] %= MOD;
			}
			
			// for(int j=0; j<n; j++) out.print(dp[j] + " ");
			// out.println();
		}

        out.println(dp[n-1]);
    }
}
