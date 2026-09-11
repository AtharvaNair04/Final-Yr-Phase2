import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {

	public static final int MAX = 10;
	
	public static int ceiling = Integer.MAX_VALUE;
	
	public static int dfs(final int n, int deep, int cur_node, int dist, int[] min, boolean[] used, int[][] adj){
		if(deep == n - 1){
			return ceiling = Math.min(ceiling, dist);
		}
		
		int[] old_min = min.clone();
		int min_dist = ceiling;
		
		//System.out.println(deep);
		for(int next_node = 0; next_node < n; next_node++){
			if(used[next_node]){
				continue;
			}
			
			final int next_dist = Math.max(min[next_node], dist + adj[cur_node][next_node]);
			
			if(ceiling <= next_dist){
				continue;
			}
			
			used[next_node] = true;
			
			for(int other_node = 0; other_node < n; other_node++){
				if(used[other_node]){
					continue;
				}
				
				min[other_node] = Math.max(min[other_node], dist + adj[cur_node][other_node]);
			}
			
			min_dist = Math.min(min_dist, dfs(n, deep + 1, next_node, next_dist, min, used, adj));
			
			used[next_node] = false;
			System.arraycopy(old_min, 0, min, 0, n);			
		}
		
		return min_dist;
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		int[][] adj = new int[MAX][MAX];
		boolean[] used = new boolean[MAX];
		int[] min = new int[MAX];
		
		while(true){
			final int n = sc.nextInt();
			
			if(n == 0){
				break;
			}
			
			for(int i = 0; i < n; i++){
				for(int j = 0; j < n; j++){
					adj[i][j] = sc.nextInt();
				}
			}
			
			for(int i = 0; i < n; i++){
				for(int j = i + 1; j < n; j++){
					adj[i][j] = adj[j][i] = Math.max(adj[i][j], adj[j][i]);
				}
			}
			
			
			ceiling = Integer.MAX_VALUE;
			for(int i = 0; i < n; i++){
				Arrays.fill(min, 0);
				Arrays.fill(used, false);
				used[i] = true;
				dfs(n, 0, i, 0, min, used, adj);
			}
			
			System.out.println(ceiling);
			
		}
	}

}
