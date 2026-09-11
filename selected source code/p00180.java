import java.util.*;

public class Main {
	Scanner sc = new Scanner(System.in);
	int prim(int cost[][],int mincost[],boolean used[],int V){
		for(int i = 0;i < V;i++){
			mincost[i] = Integer.MAX_VALUE;
			used[i] = false;
		}
		mincost[0] = 0;
		int res = 0;
		while(true){
			int v = -1;
			for(int u = 0;u < V;u++){
				if(!used[u] && (v == -1 || mincost[u] < mincost[v])) v = u;
			}
			if(v == -1)break;
			used[v] = true;
			res += mincost[v];
			for(int u = 0;u < V;u++){
				if(mincost[u] > cost[v][u]){
					mincost[u] = cost[v][u];
				}
			}
		}
		return res;
	}
	void doIt() { 
		while(true){
			int n = sc.nextInt();
			int m = sc.nextInt();
			if(n+m==0)break;
			int cost[][] = new int [n][n];
			int mincost[] = new int [n];
			boolean used[] = new boolean[n];
			for(int i = 0;i < n;i++){
				for(int j = 0;j < n;j++){
					cost[i][j] = Integer.MAX_VALUE;
					if(i == j)cost[i][j] = 0;
				}
			}
			for(int i = 0;i < m;i++){
				int a = sc.nextInt();
				int b = sc.nextInt();
				cost[a][b] = sc.nextInt();
				cost[b][a] = cost[a][b];
			}
			System.out.println(prim(cost,mincost,used,n));
		}
     }
    public static void main(String[] args) {
    	// TODO Auto-generated method stub
    	new Main().doIt();
    }
}
