import java.util.Arrays;
import java.util.Scanner;
public class Main {
	
	static boolean[] isPrime;
	static int[][] data;
	static int size = 1000 + 2;
	static int set = size / 2 - 1;
	
	static void prime() {
		isPrime = new boolean[1000001];
		Arrays.fill(isPrime, true);
		isPrime[0] = false;
		isPrime[1] = false;
		for(int i=2; i*i<=1000000; i++) {
			if(isPrime[i]) {
				for(int j=i+i; j<=1000000; j+=i) {
					isPrime[j] = false;
				}
			}
		}
	}
	
	static void numArray() {
		data = new int[size][size];
		for(int i = 0; i < size; i++){
			Arrays.fill(data[i], 1<<24);
		}
		int now = 1, posx = set, posy = set;
		for(int i=1; now<=1000000; i++){
			System.out.print("");
			if(i % 2 == 1){
				for(int j=0; j<i && now<=1000000; j++){
					data[posy][posx] = now;
					posx++;
					now++;
				}
				for(int j=0; j<i && now<=1000000; j++){
					data[posy][posx] = now;
					posy--;
					now++;
				}
			}
			else {
				for(int j=0; j<i && now<=1000000; j++){
					data[posy][posx] = now;
					posx--;
					now++;
				}
				for(int j=0; j<i && now<=1000000; j++){
					data[posy][posx] = now;
					posy++;
					now++;
				}
			}
		}
	}
	
	public static void main(String[] args) {
		try(Scanner sc = new Scanner(System.in)){
			prime();
			numArray();
			while(sc.hasNext()) {
				int m = sc.nextInt();
				int n = sc.nextInt();
				if(m + n == 0) break;
				
				int x = -1;
				int y = -1;
				for(int i= 0; i<size; i++) {
					for(int j=0; j<size; j++) {
						if(data[i][j] == n) {
							y = i;
							x = j;
						}
					}
				}
				
				int[][] dp = new int[size][size];
				dp[y][x] = 1;
				int max = 1;
				int num = 0;
				for(int i=y; i<size; i++) {
					for(int j=0; j<size; j++) {
						if(i == y && data[i][j] != n) continue;
						if(data[i][j] > m) continue;
						if(dp[i][j] == 0) continue;
						
						if(isPrime[data[i][j]]) {
							dp[i][j]++;
							if(max < dp[i][j]) {
								max = dp[i][j];
								num = data[i][j];
							}
							else if(max == dp[i][j]) {
								if(num < data[i][j]) {
									num = data[i][j];
								}
							}
						}
						
						dp[i+1][j] = Math.max(dp[i+1][j], dp[i][j]);
						if(j >= 1) {
							dp[i+1][j-1] = Math.max(dp[i+1][j-1], dp[i][j]);
						}
						if(j + 1 < 1002) {
							dp[i+1][j+1] = Math.max(dp[i+1][j+1], dp[i][j]);
						}
					}
				}
				System.out.println(max-1 + " " + num);
			}
		}
	}
}
