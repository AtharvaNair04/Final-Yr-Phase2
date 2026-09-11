import java.util.Scanner;

//Live Schedule
public class Main{

	int C, D, W, X;
	int[][] E, F;
	int[][][] dp;
	
	int get(int d, int w, int x){
		if(d==D||x<0)return 0;
		if(dp[d][w][x]!=-1)return dp[d][w][x];
		int res = get(d+1, w, x);
		for(int i=0;i<C;i++){
			if(E[i][d]==0)continue;
			int R = E[i][d], f = F[i][d];
			if(f<=w)res = Math.max(res, R+get(d+1, w-f, x));
			if(x==0)continue;
			for(int k=i+1;k<C;k++){
				if(E[k][d]==0)break;
				R+=E[k][d]; f+=F[k][d];
				if(w<f)break;
				res = Math.max(res, R+get(d+1, w-f, x-1));
			}
		}
		return dp[d][w][x] = res;
	}
	
	void run(){
		Scanner sc = new Scanner(System.in);
		dp = new int[30][51][6];
		for(;;){
			C = sc.nextInt(); D = sc.nextInt(); W = sc.nextInt(); X = sc.nextInt();
			if((C|D|W|X)==0)break;
			E = new int[C][D]; F = new int[C][D];
			for(int i=0;i<C;i++)for(int j=0;j<D;j++)E[i][j]=sc.nextInt();
			for(int i=0;i<C;i++)for(int j=0;j<D;j++)F[i][j]=sc.nextInt();
			for(int i=0;i<D;i++)for(int j=0;j<=W;j++)for(int k=0;k<=X;k++)dp[i][j][k]=-1;
			System.out.println(get(0, W, X));
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
