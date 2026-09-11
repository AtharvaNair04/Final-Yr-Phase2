import java.util.Scanner;

//Bingo
public class Main{

	void run(){
		Scanner sc = new Scanner(System.in);
		int MOD = 100000;
		for(;;){
			int N = sc.nextInt(), M = sc.nextInt(), S = sc.nextInt();
			if((N|M|S)==0)break;
			N*=N;
			int[][] dp = new int[N+1][S+1];
			dp[0][0] = 1;
			for(int m=1;m<=M;m++)for(int k=N;k>0;k--)for(int s=m;s<=S;s++){
				dp[k][s]+=dp[k-1][s-m];
				if(MOD<=dp[k][s])dp[k][s]-=MOD;
			}
			System.out.println(dp[N][S]);
		}
	}

	public static void main(String[] args) {
		new Main().run();
	}
}
