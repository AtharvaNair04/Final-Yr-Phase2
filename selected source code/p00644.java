import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

//Winter Bells
public class Main{

	int[] d;
	void run(){
		Scanner sc = new Scanner(System.in);
		int INF = 1<<27;
		d = new int[100];
		int[][] wf = new int[100][100], e = new int[100][100];
		double[] K = new double[100], R = new double[100];
		for(;;){
			int n = sc.nextInt(), m = sc.nextInt(), p = sc.nextInt();
			if((n|m|p)==0)break;
			for(int[]a:wf)Arrays.fill(a, INF);
			for(int[]a:e)Arrays.fill(a, INF);
			for(int i=0;i<n;i++)wf[i][i]=0;
			for(int i=0;i<m;i++){
				int s = sc.nextInt(), t = sc.nextInt(), c = sc.nextInt();
				e[s][t] = e[t][s] = wf[s][t] = wf[t][s] = c;
			}
			for(int k=0;k<n;k++)for(int i=0;i<n;i++)for(int j=0;j<n;j++)wf[i][j]=Math.min(wf[i][j], wf[i][k]+wf[k][j]);
			PriorityQueue<Integer> q = new PriorityQueue<Integer>(n, new Comparator<Integer>() {
				public int compare(Integer o1, Integer o2) {
					return d[o1]-d[o2];
				}
			});
			Arrays.fill(K, 0);
			K[n-1] = 1;
			for(int i=0;i<n;i++){
				d[i] = -wf[0][i]; q.add(i);
			}
			while(!q.isEmpty()){
				int v = q.poll();
				for(int i=0;i<n;i++){
					if(i==v || e[i][v]==INF)continue;
					if(wf[0][n-1]==wf[0][i]+e[i][v]+wf[v][n-1]){
						K[i]+=K[v];
					}
				}
			}
			Arrays.fill(R, 0);
			R[0] = 1;
			for(int i=0;i<n;i++){
				d[i]=wf[0][i];
				q.add(i);
			}
			while(!q.isEmpty()){
				int v = q.poll();
				for(int i=0;i<n;i++){
					if(i==v || e[v][i]==INF)continue;
					if(wf[0][n-1]==wf[0][v]+e[v][i]+wf[i][n-1]){
						R[i]+=R[v];
					}
				}
			}
			while(p--!=0){
				int k = sc.nextInt();
				System.out.printf("%.8f\n", K[k]*R[k]/R[n-1]);
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
