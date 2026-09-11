import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class Main implements Runnable{
	int N,num,size;
	int[] depth,first,last,segment;
	ArrayList<Edge>[] graph;

	private class Edge{
		int to,weight;
		public Edge(int to,int weight){
			this.to = to;
			this.weight = weight;
		}
	}

	public void init(int n){
		size = 1;
		while(size < n)size<<=1;
		segment = new int[2*size];
		for(int i = 0;i < 2 * size - 1;i++)segment[i]=Integer.MIN_VALUE;
	}
	public void update(int k,int a){
		k += size - 1;
		segment[k] = a;
		while(k > 0){
			k = (k - 1) / 2;
			segment[k] = Math.max(segment[k * 2 + 1], segment[k * 2 + 2]);
		}
	}
	public int query(int a,int b,int k,int l,int r){
		if(r <= a || b <= l)return Integer.MIN_VALUE;
		if(a <= l && r <= b)return segment[k];
		int vl = query(a,b,k * 2 + 1,l,(l + r)/2);
		int vr = query(a,b,k * 2 + 2,(l + r)/2,r);
		return Math.max(vl, vr);
	}

	public int[] dfs(int prev,int now){
		int[] ret = {now,0};
		for(Edge edge : graph[now]){
			if(edge.to != prev){
				int[] tmp = dfs(now,edge.to);
				tmp[1]+=edge.weight;
				if(ret[1] < tmp[1])ret = tmp;
			}
		}
		return ret;
	}

	//??¨?????´????????????u,v????±???????
	public int[] doubleSweep(){
		int u = dfs(-1,0)[0];
		int v = dfs(-1,u)[0];
		return new int[]{u,v};
	}

	public void eulerTour(int prev,int now,int dis){
		depth[now] = dis;
		first[now] = num++;
		for(Edge edge : graph[now]){
			if(edge.to != prev){
				eulerTour(now,edge.to,dis+edge.weight);
				num++;
			}
		}
		last[now] = num;
	}

	@SuppressWarnings("unchecked")
	public void solve() {
		N = nextInt();

		graph = new ArrayList[N];
		for(int i = 0;i < N;i++)graph[i] = new ArrayList<Edge>();

		for(int i = 0;i < N-1;i++){
			int p = nextInt();
			int w = nextInt();
			graph[i+1].add(new Edge(p,w));
			graph[p].add(new Edge(i+1,w));
		}

		int[] tmp = doubleSweep();
		int u = tmp[0];
		int v = tmp[1];
		int ans = 0;
		int M = 2 * N;

		depth = new int[M];
		first = new int[M];
		last = new int[M];

		//u
		num = 0;
		eulerTour(-1,u,0);

		init(2*M);
		for(int i = 0;i < M;i++){
			update(first[i],depth[i]);
		}
		for(int i = 0;i < N;i++){
			for(Edge edge : graph[i]){
				int to = edge.to;
				if(depth[edge.to] < depth[i])to = i;
				int vl = query(0,first[to],0,0,size);
				int vr = query(last[to],M-1,0,0,size);
				ans = Math.max(ans,Math.max(vl, vr)+edge.weight);
			}
		}

		Arrays.fill(depth, 0);
		Arrays.fill(first,0);
		Arrays.fill(last, 0);
		Arrays.fill(segment, Integer.MIN_VALUE);
		num = 0;

		//v
		eulerTour(-1,v,0);

		for(int i = 0;i < M;i++){
			update(first[i],depth[i]);
		}
		for(int i = 0;i < N;i++){
			for(Edge edge : graph[i]){
				int to = edge.to;
				if(depth[edge.to] < depth[i])to = i;
				int vl = query(0,first[to],0,0,size);
				int vr = query(last[to],M-1,0,0,size);
				ans = Math.max(ans,Math.max(vl, vr)+edge.weight);
			}
		}

		out.println(ans);
	}

	public static void main(String[] args) {
		new Thread(null,new Main(),"",32*1024 * 1024).start();
	}

	/* Input */
	private static final InputStream in = System.in;
	private static final PrintWriter out = new PrintWriter(System.out);
	private final byte[] buffer = new byte[2048];
	private int p = 0;
	private int buflen = 0;

	private boolean hasNextByte() {
		if (p < buflen)
			return true;
		p = 0;
		try {
			buflen = in.read(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (buflen <= 0)
			return false;
		return true;
	}

	public boolean hasNext() {
		while (hasNextByte() && !isPrint(buffer[p])) {
			p++;
		}
		return hasNextByte();
	}

	private boolean isPrint(int ch) {
		if (ch >= '!' && ch <= '~')
			return true;
		return false;
	}

	private int nextByte() {
		if (!hasNextByte())
			return -1;
		return buffer[p++];
	}

	public String next() {
		if (!hasNext())
			throw new NoSuchElementException();
		StringBuilder sb = new StringBuilder();
		int b = -1;
		while (isPrint((b = nextByte()))) {
			sb.appendCodePoint(b);
		}
		return sb.toString();
	}

	public int nextInt() {
		return Integer.parseInt(next());
	}

	public long nextLong() {
		return Long.parseLong(next());
	}

	public double nextDouble() {
		return Double.parseDouble(next());
	}
	@Override
	public void run() {
		out.flush();
		new Main().solve();
		out.close();
	}
}
