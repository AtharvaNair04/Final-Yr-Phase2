import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
	
	public static class UnionFind {
		int[] par;
		
		public UnionFind(int n){
			par = new int[n];
			Arrays.fill(par, -1);
		}
		
		public int find(final int x){
			if(par[x] < 0){
				return x;
			}else{
				return par[x] = find(par[x]);
			}
		}
		
		public boolean union(int x, int y){
			x = find(x);
			y = find(y);
			
			if(x == y){ return false; }
			
			if(par[y] < par[x]){
				int tmp = x; 
				x = y;
				y = tmp;
			}
			par[x] += par[y];
			par[y] = x;
			return true;
		}
		
		public boolean same(int x, int y){
			return find(x) == find(y);
		}
	}
	
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		
		final int N = sc.nextInt();
		HashMap<String, Integer> id = new HashMap<String, Integer>();
		int[] costs = new int[N];
		
		for(int i = 0; i < N; i++){
			final String name = sc.next();
			id.put(name, i);
			
			final int cost = sc.nextInt();
			costs[i] = cost;
		}
		//System.out.println(Arrays.toString(costs));
		
		final int M = sc.nextInt();
		UnionFind uf = new UnionFind(N);
		for(int i = 0; i < M; i++){
			final String s = sc.next();
			final String t = sc.next();
			
			final int s_id = id.get(s);
			final int t_id = id.get(t);
			
			if(!uf.same(s_id, t_id)){
				uf.union(s_id, t_id);
			}
		}
		
		for(int i = 0; i < N; i++){
			costs[uf.find(i)] = Math.min(costs[uf.find(i)], costs[i]);
		}
		
		long sum = 0;
		for(int i = 0; i < N; i++){
			sum += costs[uf.find(i)];
		}
		
		System.out.println(sum);
		
	}
	
}
