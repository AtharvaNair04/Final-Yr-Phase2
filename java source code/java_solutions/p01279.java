import java.util.Arrays;
import java.util.Scanner;

//Defend the Bases
public class Main{

	int[] bipartiteMatching(boolean[][] graph) {
		int n = graph.length;
		if(n==0)return new int[0];
		int m = graph[0].length;
		int[] match = new int[m];
		Arrays.fill(match, -1);
		int res = 0;
		for (int i = 0; i < n; i++) {
			boolean[] visited = new boolean[m];
			if (go(i,graph,visited,match)) res++;
		}
		return match;
	}
	boolean go(int v,boolean[][] graph,boolean[] visited,int[] match) {
		int m = graph[0].length;
		for (int i = 0; i < m; i++)  if (!visited[i] && graph[v][i] && match[i] == -1) {
			visited[i] = true;
			match[i] = v;
			return true;
		}
		for (int i = 0; i < m; i++) if (!visited[i] && graph[v][i]) {
			visited[i] = true;
			if (go(match[i],graph,visited,match)) {
				match[i] = v;
				return true;
			}
		}
		return false;
	}
	
	double EPS = 1e-9;
	
	void run(){
		Scanner sc = new Scanner(System.in);
		for(;;){
			int n = sc.nextInt();
			int m = sc.nextInt();
			if((n|m)==0)break;
			int[][] a = new  int[n][2];
			int[] v = new int[n];
			for(int i=0;i<n;i++){
				for(int j=0;j<2;j++)a[i][j] = sc.nextInt();
				v[i] = sc.nextInt();
			}
			int[][] b = new int[m][2];
			for(int i=0;i<m;i++)for(int j=0;j<2;j++)b[i][j]=sc.nextInt();
			double[][] t = new double[n][m];
			double max = 0;
			for(int i=0;i<n;i++){
				for(int j=0;j<m;j++){
					t[i][j] = Math.hypot(a[i][0]-b[j][0], a[i][1]-b[j][1])/v[i];
					max = Math.max(max, t[i][j]);
				}
			}
			double l = 0;
			double r = max+EPS;
			for(;;){
				if(Math.abs(r-l)<EPS){
					System.out.printf("%.8f\n", l);
					break;
				}
				double mid = (l+r)/2;
				boolean[][] map = new boolean[n][m];
				for(int i=0;i<n;i++)for(int j=0;j<m;j++)if(t[i][j]<mid+EPS)map[i][j] = true;
				int[] res = bipartiteMatching(map);
				int c = 0;
				for(int i:res)if(i!=-1)c++;
				if(c==m)r=mid;
				else l=mid;
			}
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
