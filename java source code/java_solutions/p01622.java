import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;


public class Main {
	static int N;
	static int[] R, W;
	
	// list内の要素を何個か足して、max以下の最大数を返す
	static private int calc(int[] list, int max)
	{
		Arrays.sort(list);
		
		int[][] dp = new int[1001][1001];
		for (int i = 0; i < 1001; i++) {
			for (int j = 0; j < 1001; j++) {
					dp[i][j] = 0;
			}
		}
		dp[0][0] = 1;
		
		for (int i = 0; i < list.length; i++) {
			for (int j = 0; j < 1001; j++) {
				if(dp[i][j] == 0) continue;
				
				dp[i+1][j] = 1;
				if(j + list[i] <= max) dp[i+1][j+list[i]] = 1;
			}
		}
		
		for (int i = max; i >= 0; i--) {
			if(dp[list.length][i] == 1) return i;
		}
		
		return 0;
	}
	
	static private void solve()
	{
		if(N == 1)
		{
			System.out.println(R[0]*2 + W[0]);
			return;
		}
		
		int maxread = 0;
		int maxindex = 0;
		int sumread = 0;
		int sumwrite = 0;
		for (int i = 0; i < N; i++) {
			if(maxread < R[i])
			{
				maxread = R[i];
				maxindex = i;
			}
			sumread += R[i];
			sumwrite += W[i];
		}
		
		if((sumread - maxread) >= maxread)
		{
			System.out.println(sumread + sumwrite);
			return;
		}
		
		int free = maxread - (sumread - maxread);
		
		int[] list = new int[N-1];
		for (int i = 0; i < N; i++) {
			if(i == maxindex) continue;
			
			if(i < maxindex)
				list[i] = W[i];
			else {
				list[i-1] = W[i];
			}
		}
		
		int totalwrite = calc(list, free);
		
		System.out.println(maxread*2+sumwrite-totalwrite);
	}
	
	static public void main(String[] args)
	{
		Scanner sca = new Scanner(System.in);
		
		while(true)
		{
			
			N = sca.nextInt();
			if(N == 0) break;
			
			R = new int[N];
			W = new int[N];
			for (int i = 0; i < N; i++) {
				R[i] = sca.nextInt();
				W[i] = sca.nextInt();
			}
			
			solve();
		}
	}
}
