import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class Main{
	
	public static class UnionFind{
	    int[] par; // 
	    
	    public UnionFind(int n){
	        par = new int[n];
	        for(int i = 0; i < n; i++){ par[i] = -1; }
	    }
	    
	    public int find(int x){
	        if(par[x] < 0){
	            return x;
	        }else{
	            return par[x] = find(par[x]);
	        }
	    }
	    
	    public boolean union(int x, int y){
	        x = find(x);
	        y = find(y);
	        
	        if(x != y){
	            if(par[y] < par[x]) {  // 多い方が根になるようにスワップする.
	                int tmp = x; x = y; y = tmp;
	            }
	            par[x] += par[y];
	            par[y] = x;
	            return true;
	        }else{
	            return false;
	        }
	    }
	    
	    public boolean same(int x, int y){
	        return find(x) == find(y);
	    }
	    
	    public int size(int x){
	        return -par[find(x)];
	    }
	}
	
	public static class Edge implements Comparable<Edge>{
		int from, to, cost;

		public Edge(int from, int to, int cost) {
			super();
			this.from = from;
			this.to = to;
			this.cost = cost;
		}

		@Override
		public int compareTo(Edge o) {
			return this.cost - o.cost;
		}
	}
	
	public static long kruskal(final int N, final int E, final int cutted, ArrayList<Edge> edges, final long min, LinkedList<Integer> list){
		UnionFind uf = new UnionFind(N);
		
		int pos = 0;
		int cnt = 0;
		long cost = 0;
		while(cnt < N - 1 && pos < E){
			Edge edge = edges.get(pos);
			
			if(cutted == pos){
				pos++;
				continue;
			}else if(!uf.union(edge.from, edge.to)){
				pos++;
				continue;
			}else if(cost > min){
				return Long.MAX_VALUE;
			}
			
			if(list != null){
				list.add(pos);
			}
			
			//System.out.println(pos + " : " + edge.from + " " + edge.to);
			
			cost += edge.cost;
			cnt++;
			pos++;
		}
		
		return cnt < N - 1 ? Long.MAX_VALUE : cost;
	}
	
	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		
		final int N = sc.nextInt();
		final int E = sc.nextInt();
		
		
		ArrayList<Edge> edges = new ArrayList<Main.Edge>();
		for(int i = 0; i < E; i++){
			final int s = sc.nextInt() - 1;
			final int e = sc.nextInt() - 1;
			final int c = sc.nextInt();
			
			edges.add(new Edge(s, e, c));
		}
		
		Collections.sort(edges);
		LinkedList<Integer> cut = new LinkedList<Integer>();
		
		final long min = kruskal(N, E, -1, edges, Long.MAX_VALUE, cut);
		//System.out.println(min);
		
		long cnt = 0;
		long cost = 0;
		for(final int skip: cut){
			final long ret = kruskal(N, E, skip, edges, min, null);
			
			if(ret != min){
				cnt++;
				cost += edges.get(skip).cost;
			}
		}
		
		System.out.println(cnt + " " + cost);
		
		sc.close();
	}

	public static class Scanner {
		private BufferedReader br;
		private StringTokenizer tok;

		public Scanner(InputStream is) throws IOException {
			br = new BufferedReader(new InputStreamReader(is));
		}

		private void getLine() throws IOException {
			while (!hasNext()) {
				tok = new StringTokenizer(br.readLine());
			}
		}

		private boolean hasNext() {
			return tok != null && tok.hasMoreTokens();
		}

		public String next() throws IOException {
			getLine();
			return tok.nextToken();
		}

		public int nextInt() throws IOException {
			return Integer.parseInt(next());
		}
		
		public long nextLong() throws IOException {
			return Long.parseLong(next());
		}

		// 他のnextXXXもXXX.parseXXX()メソッドを使って作れるので省略

		public void close() throws IOException {
			br.close();
		}
	}
}
