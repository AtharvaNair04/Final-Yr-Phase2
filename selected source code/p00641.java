import java.util.*;

public class Main {

	int[][] graph;
	int[][] weight;
	int maxValue, maxCnt;
	int N;
	boolean[] visited;
	static final int MOD = 10007;
	
	void run(){
		Scanner in = new Scanner(System.in);
		for(;;){
			N = in.nextInt();
			if(N == 0) return ;
			graph = new int[N][2];
			weight = new int[N][2];
			visited = new boolean[N];
			for(int i=0; i<N; i++){
				for(int j=0; j<2; j++){
					int x = Integer.parseInt(in.next()), fx = Integer.parseInt(in.next());
					graph[i][j] = x;
					weight[i][j] = fx;
				}
			}
			System.out.println(solve());
		}
	}

	int solve(){
		int ret = 1;
		for(int i=0; i<N; i++)if(!visited[i]){
			maxValue = weight[i][1]; maxCnt = 1;
			dfs(i);
			ret = (ret*(maxCnt%MOD))%MOD;
		}
		return ret;
	}

	void dfs(int v){
		for(;;){
			visited[v] = true;
			boolean end = true;
			for(int i=0; i<2; i++){
				if(!visited[graph[v][i]]){
					if(maxValue <= weight[v][i]){
						if(maxValue == weight[v][i]){
							maxCnt++;
						}
						else{
							maxValue = weight[v][i];
							maxCnt = 1;
						}
					}
					v = graph[v][i];
					end = false;
					break;
				}
			}
			if(end) return ;
		}
	}

	public static void main(String args[]){
		new Main().run();
	}
}
