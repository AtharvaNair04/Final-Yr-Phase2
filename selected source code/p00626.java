import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

//Chocolate with Heart Marks
public class Main{

	int minimumSteinerTree(List<Integer> vs, int[][] e, int n){
		int K = vs.size(), INF = 1<<29;
		if(K<=1)return 0;
		int[][] wf = new int[n][n];
		for(int i=0;i<n;i++)for(int j=0;j<n;j++)wf[i][j]=e[i][j];
		for(int i=0;i<n;i++)wf[i][i]=0;
		for(int k=0;k<n;k++)for(int i=0;i<n;i++)for(int j=0;j<n;j++)wf[i][j]=Math.min(wf[i][j], wf[i][k]+wf[k][j]);
		
		int[][] dp = new int[1<<K][n];
		for(int[]d:dp)Arrays.fill(d, INF);
		for(int p=0;p<K;p++)for(int q=0;q<n;q++)dp[1<<p][q]=wf[vs.get(p)][q];
		
		for(int S=1;S<1<<K;S++){
			if((S&(S-1))==0)continue;
			for(int p=0;p<n;p++)for(int sub=0;sub<S;sub++){
				if((S|sub)==S){
					dp[S][p] = Math.min(dp[S][p], dp[sub][p]+dp[S-sub][p]);
				}
			}
			for(int p=0;p<n;p++)for(int q=0;q<n;q++){
				dp[S][p] = Math.min(dp[S][p], dp[S][q]+wf[q][p]);
			}
		}
		int res = INF;
		for(int S=0;S<1<<K;S++)for(int q=0;q<n;q++){
			res = Math.min(res, dp[S][q] + dp[((1<<K)-1)-S][q]);
		}
		return res;
	}
	
	void run(){
		Scanner sc = new Scanner(System.in);
		int[][] d = {{-1,0},{0,1},{1,0},{0,-1}};
		for(;;){
			int h = sc.nextInt(), w = sc.nextInt();
			if((h|w)==0)break;
			int[][] e = new int[h*w][h*w];
			for(int[]a:e)Arrays.fill(a, 1<<29);
			List<Integer> vs = new ArrayList<Integer>();
			for(int i=0;i<h;i++)for(int j=0;j<w;j++){
				if(sc.nextInt()==1)vs.add(i*w+j);
				for(int k=0;k<4;k++){
					int ni = i+d[k][0], nj = j+d[k][1];
					if(0<=ni&&ni<h&&0<=nj&&nj<w)e[i*w+j][ni*w+nj]=1;
				}
			}
			System.out.println(h*w-minimumSteinerTree(vs, e, h*w)-1);
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
