import java.util.*;

public class Main {
	
	private void doit(){
		Scanner sc = new Scanner(System.in);
		while(true){
			int n = sc.nextInt();
			int c = sc.nextInt();
			if((n|c) == 0) break;
			int [] data1 = new int[n];
			for(int i = 0; i < n; i++){
				StringBuilder sb = new StringBuilder();
				for(int j = 0; j < 16; j++){
					sb.append(sc.nextInt());
				}
				data1[i] = Integer.parseInt(sb.toString(), 2);
			}
			int [] data2 = new int[c];
			for(int i = 0; i < c; i++){
				StringBuilder sb = new StringBuilder();
				for(int j = 0; j < 16; j++){
					sb.append(sc.nextInt());
				}
				data2[i] = Integer.parseInt(sb.toString(), 2);
			}
			int [][] dp = new int[2][1 << 16];
			for(int i = 0; i < 2; i++){
				Arrays.fill(dp[i], -1);
			}
			dp[0][0] = 0;
			int max = 0;
			for(int i = 0; i < n; i++){
				int ind = i & 1;
				for(int j = 0; j < (1 << 16); j++){
					if(dp[ind][j] < 0) continue;
					dp[ind ^ 1][j] = Math.max(dp[ind ^ 1][j], dp[ind][j]);
					max = Math.max(max, dp[ind ^ 1][j]);
					for(int k = 0; k < c; k++){
						int temp = data1[i] | j;
						int temp2 = temp & data2[k];
						int count = Integer.bitCount(temp2);
						
						int nextbit = temp & (~data2[k]);
						int value = dp[ind][j] + count;
						dp[ind ^ 1][nextbit] = Math.max(dp[ind ^ 1][nextbit], value);
						max = Math.max(max, dp[ind ^ 1][nextbit]);
					}
				}
				Arrays.fill(dp[ind], -1);
			}
			System.out.println(max);
		}
	}


	private void debug(Object... o) {
		System.out.println("debug = " + Arrays.deepToString(o));
	}

	public static void main(String[] args) {
		new Main().doit();
	}
}
