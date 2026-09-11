import java.util.*;
import java.math.*;
import java.lang.*;

public class Main {
	public static void main(String[] args){
		new Solution().solve();
	}
}

class Solution{
	public void solve(){
		Scanner scan = new Scanner(System.in);
		while(true){
			int V = scan.nextInt();
			int E = scan.nextInt();
			
			if(V == 0 && E == 0) break;
			
			Edge[] edges = new Edge[E];
			for(int i = 0; i < E; i++){
				edges[i] = new Edge();
				edges[i].from = scan.nextInt();
				edges[i].to = scan.nextInt();
				edges[i].cost = scan.nextInt();
				edges[i].from--;
				edges[i].to--;
			}
			Arrays.sort(edges);
			int ans = -1;
			for(int i = 0; i < E; i++){
				int res = calc(edges, V, i);
				if(res == -1) continue;
				if(ans == -1 || ans > res) ans = res;
			}
			System.out.println(ans);
		}
	}
	
	public boolean one(UnionFind uf, int V){
		int p = uf.findParent(0);
		for(int i = 0; i < V; i++){
			if(uf.findParent(i) != p) return false;
		}
		return true;
	}
	
	public int calc(Edge[] edges, int V, int f){
		UnionFind uf = new UnionFind(V);
		int maxval = 0, minval = (1<<20);
		for(int i = f; i < edges.length; i++){
			Edge e = edges[i];
			if(!uf.hasSameParent(e.from, e.to)){
				uf.unite(e.from, e.to);
				maxval = Math.max(maxval, e.cost);
				minval = Math.min(minval, e.cost);
			}
		}
		
		boolean flg = one(uf, V);
		return (flg?maxval-minval:-1);
	}
}

class Edge implements Comparable{
	public int from, to, cost;

	@Override
	public int compareTo(Object o) {
		return new Integer(this.cost).compareTo(new Integer(((Edge)o).cost));
	}
};

class UnionFind{
	private int[] par;
	private int[] rank;
	
	UnionFind(int V){
		par = new int[V];
		rank = new int[V];
		for(int i = 0; i < V; i++){
			par[i] = i;
			rank[i] = 0;
		}
	}
	
	public void unite(int x, int y){
		x = findParent(x);
		y = findParent(y);
		if(x == y) return;
		
		if(rank[x] < rank[y]){
			par[x] = par[y];
		}else{
			par[y] = par[x];
			if(rank[x] == rank[y]) rank[x]++;
		}
	}
	
	public int findParent(int x){
		if(par[x] == x) return x;
		return par[x] = findParent(par[x]);
	}
	
	public boolean hasSameParent(int x, int y){
		return findParent(x) == findParent(y);
	}
}
