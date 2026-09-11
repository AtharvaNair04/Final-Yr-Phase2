import java.util.Scanner;

//Nim
public class Main{

	void run(){
		Scanner sc = new Scanner(System.in);
		for(;;){
			int n = sc.nextInt();
			if(n==0)break;
			int s = sc.nextInt();
			int[] m = new int[2*n];
			for(int i=0;i<2*n;i++)m[i]=sc.nextInt();
			boolean[][] dp = new boolean[2*n][s+1];
			for(int j=2;j<=s;j++)for(int i=0;i<2*n;i++){
				boolean w = false;
				for(int p=1;p<=m[i];p++){
					if(j-p<=0)break;
					if(!dp[(i+1)%(2*n)][j-p])w=true;
				}
				dp[i][j] = w;
			}
			System.out.println(dp[0][s]?1:0);
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
