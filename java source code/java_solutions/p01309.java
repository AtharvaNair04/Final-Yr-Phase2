import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		for(int t=0;t<T;t++) {
			int n = sc.nextInt();
			int x = sc.nextInt();
			int y = sc.nextInt();
			Vector2[] v = new Vector2[n+1];
			for(int i=0;i<=n;i++) {
				v[i] = new Vector2(sc.nextInt(), sc.nextInt());
			}
			Graph g = new Graph(n+1);
			UnionFind uf = new UnionFind(n+1);
			int count = 0;
			for(int i=1;i<=n;i++) {
				if (Vector2.distSquare(v[i], v[0]) <= 100) {
					g.addBidirectionalEdge(i, 0, 1);
					uf.union(i, 0);
					count++;
				}
				for(int j=i+1;j<=n;j++) {
					if (Vector2.distSquare(v[i], v[j]) <= 2500) {
						g.addBidirectionalEdge(i, j, 1);
						uf.union(i, j);
					}
				}
			}
			//System.out.println(g);
			if (y < x && uf.size(0) >= 3 && uf.size(0) != count + 1) {
				System.out.println("You're always welcome!");
				continue;
			}

			int[] dist = g.minDistQueue(0);
			int max = 0;
			for(int i=0;i<=n;i++) {
				if (dist[i] < Graph.INF) {
					max = Math.max(max,dist[i]);
				}
			}
			System.out.println(x * max);

			System.gc();
		}
	}

}
class Graph {
	public static final int INF = 1<<29;
	int n;
	ArrayList<Edge>[] graph;

	@SuppressWarnings("unchecked")
	public Graph(int n) {
		this.n = n;
		this.graph = new ArrayList[n];
		for(int i=0;i<n;i++) {
			graph[i] = new ArrayList<Edge>();
		}
	}

	public void addBidirectionalEdge(int from,int to,int cost) {
		addEdge(from,to,cost);
		addEdge(to,from,cost);
	}
	public void addEdge(int from,int to,int cost) {
		graph[from].add(new Edge(to, cost));
	}

	//O(E) all cost is 0 or 1
	public int[] minDistQueue(int s) {
		int[] d = new int[n];
		Arrays.fill(d, INF);
		ArrayDeque<Integer> q = new ArrayDeque<Integer>();
		q.add(s);
		d[s] = 0;
		while(!q.isEmpty()) {
			int v = q.pollFirst();
			for(Edge e:graph[v]) {
				int u = e.to;
				if (d[v] + e.cost < d[u]) {
					d[u] = d[v] + e.cost;
					if (e.cost == 0) {
						q.addFirst(u);
					}else{
						q.addLast(u);
					}
				}
			}
		}
		return d;
	}

	class Edge {
		int to;
		int cost;
		public Edge(int to,int cost) {
			this.to = to;
			this.cost = cost;
		}
		public String toString() {
			return String.valueOf(to);
		}
	}
	public String toString() {
		return Arrays.toString(graph);
	}
}
class UnionFind {
	private int[] data;
	public UnionFind(int size) {
		data = new int[size];
		Arrays.fill(data, -1);
	}
	public void union(int x,int y) {
		x = root(x);
		y = root(y);
		if (x!=y) {
			if (data[y] < data[x]) {
				int tmp = y;
				y = x;
				x = tmp;
			}
			data[x] += data[y];
			data[y] = x;
		}
	}
	public boolean isConnected(int x,int y) {
		return root(x)==root(y);
	}
	private int root(int x) {
		return data[x] < 0 ? x : (data[x] = root(data[x]));
	}
	public int size(int x) {
		return -data[root(x)];
	}
	public String toString() {
		return Arrays.toString(data);
	}
}
class Vector2 {
	int x = 0;
	int y = 0;
	public Vector2(int x,int y) {
		this.x = x;
		this.y = y;
	}
	public Vector2 subtract(Vector2 v) {
		return new Vector2(this.x-v.x,this.y-v.y);
	}
	public int distSquare() {
		return x * x + y * y;
	}
	public static int distSquare(Vector2 a,Vector2 b) {
		return a.subtract(b).distSquare();
	}
	public String toString() {
		return this.x + " " + this.y;
	}
}
