import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.TreeSet;

public class Main {
	
	public static int dfs(final int n, int cur, int prev, int[][] adj, boolean do_back){	
		boolean leaf = true;
		int sum_cost = 0;
		
		int[] costs = new int[n];
		for(int next = 0; next < n; next++){
			if(next != prev && adj[cur][next] > 0){
				final int cost = dfs(n, next, cur, adj, true) + adj[cur][next] * 2;
				sum_cost += cost;
				costs[next] = cost;
				
				if(leaf){
					leaf = false;
				}
			}
		}
		
		int min_cost = sum_cost;
		if(!do_back && !leaf){
			for(int next = 0; next < n; next++){
				if(next != prev && adj[cur][next] > 0 && costs[next] > 0){
					min_cost = Math.min(min_cost, (sum_cost - costs[next]) + (adj[cur][next] + dfs(n, next, cur, adj, false)));
				}
			}
		}
		
		//System.out.println((cur + 1) + " " + (prev == -1 ? "-" : (prev + 1) + "") + " " + sum_cost + " " + min_cost);
		
		if(leaf){
			return -2 * adj[prev][cur];
		}else{
			return min_cost;
		}
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		while (true) {
			final int N = sc.nextInt();
			
			if(N == 0){
				break;
			}
			
			int[][] adj = new int[N][N];
			for(int i = 0; i < N - 1; i++){
				final int from = sc.nextInt() - 1;
				final int to = sc.nextInt() - 1;
				final int cost = sc.nextInt();
				
				adj[from][to] = adj[to][from] = cost;
			}
			
			System.out.println(dfs(N, 0, -1, adj, false));
			
		}
	}

}
