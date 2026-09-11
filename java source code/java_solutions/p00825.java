import java.util.Arrays;
import java.util.Scanner;

//Concert Hall Scheduling
public class Main{

	class R implements Comparable<R>{
		int s, t, c;
		public R(int s, int t, int c) {
			this.s = s;
			this.t = t;
			this.c = c;
		}
		public int compareTo(R o) {
			return t-o.t;
		}
	}
	
	void run(){
		Scanner sc = new Scanner(System.in);
		int[][][] dp = new int[2][366][366];
		for(;;){
			int n = sc.nextInt();
			if(n==0)break;
			R[] r = new R[n];
			for(int i=0;i<n;i++)r[i]=new R(sc.nextInt(), sc.nextInt(), sc.nextInt());
			Arrays.sort(r);
			int x = 0;
			for(int i=0;i<2;i++)for(int j=0;j<366;j++)for(int k=0;k<366;k++)dp[i][j][k]=0;
			for(int i=0;i<n;i++,x=1-x)for(int j=1;j<366;j++)for(int k=1;k<366;k++){
				int max = Math.max(dp[1-x][j][k], Math.max(dp[x][j-1][k], dp[x][j][k-1]));
				if(r[i].t==j)max = Math.max(max, dp[1-x][r[i].s-1][k]+r[i].c);
				if(r[i].t==k)max = Math.max(max, dp[1-x][j][r[i].s-1]+r[i].c);
				dp[x][j][k] = max;
			}
			System.out.println(dp[1-x][365][365]);
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
