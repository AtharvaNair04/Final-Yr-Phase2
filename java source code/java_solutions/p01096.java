import java.util.*;

class Main{
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		while(true){
			int n = scan.nextInt();
			if(n==0)break;

			int[] arr = new int[n];
			for(int i = 0; i < n; i++) arr[i] = scan.nextInt();
			int[][] dp = new int[n][n];

			for(int interval = 0; interval<n; interval += 2) {
				for(int start = 0, end = start + interval + 1; end < n; start++, end++) {
					//System.out.printf("start:%3d , end:%3d\n",start,end);

					if(Math.abs(arr[start]-arr[end])<2) {
						if(dp[start+1][end-1]==interval) {
							dp[start][end] = interval+2;
							continue;
						}
					}
					dp[start][end] = dp[start+1][end-1];

					for(int innerInterval = 0; innerInterval<interval; innerInterval+=2) {/*
						if(dp[start][end]<dp[start][start+innerInterval+1]+dp[start+innerInterval+2][end]) {
							System.out.println("\t kakoikomi");
						}*/
						dp[start][end] = Math.max(
							(
								dp[start][end]
							),
							(
								dp[start][start+innerInterval+1] +
								dp[start+innerInterval+2][end]
							)
						);
					}
				}
					//System.out.println("------\n");
			}

			int result = 0;
			for(int i = 0; i < dp.length; i++) {
				for(int j = 0; j < dp[i].length; j++) {
					//System.out.printf("%3d",dp[i][j]);
					result = Math.max(result,dp[i][j]);
				}
				//System.out.println();
			}
			System.out.println(result);
		}
	}
}
