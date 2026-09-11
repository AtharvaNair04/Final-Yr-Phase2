import java.util.Arrays;
import java.util.Scanner;

public class Main {
	
	
	public static int dfs(int rest, int remain, int[][] memo){
		if(memo[rest][remain] != -1){
			return memo[rest][remain];
		}else if(rest == 0){
			return remain == 0 ? 1 : 0;
		}
		
		int ret = 0;
		
		final int limit = Math.min(1000, remain);
		for(int cur = 0; cur <= limit; cur++){
			ret += dfs(rest - 1, remain - cur, memo);
		}
		
		return memo[rest][remain] = ret;
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int[][] memo = new int[5][4001];
		for(int i = 0; i < 5; i++){
			if(i == 0){
				memo[0][0] = 1; 
			}else{
				Arrays.fill(memo[i], -1);
			}
		}
		
		while (sc.hasNextInt()) {
			final int n = sc.nextInt();
			
			System.out.println(dfs(4, n, memo));
		}
	}

}
