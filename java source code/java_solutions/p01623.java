import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class Main {
	static ContestScanner in;
	static Writer out;
	public static void main(String[] args) {
		Main main = new Main();
		try {
			in = new ContestScanner();
			out = new Writer();
			main.solve();
			out.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	void solve() throws IOException {
		while(true) {
			int n = in.nextInt();
			int m = in.nextInt();
			if(n == 0) break;
			Isl[] isl = new Isl[n];
			for(int i = 0; i < n; i++) {
				isl[i] = new Isl(i, in.nextInt());
			}
			for(int i = 0; i < m; i++) {
				int a = in.nextInt() - 1;
				int b = in.nextInt() - 1;
				int c = in.nextInt();
				if(isl[a].h > isl[b].h) {
					int tmp = a;
					a = b;
					b = tmp;
				}
				isl[a].edge.add(new Edge(a, b, c));
			}
			int ans = getAmount(n, m, isl);
			if(ans < 0) {
				for(int i = 0; i <= -ans; i++) {
					isl[i].h = isl[-ans + 1].h;
				}
				ans = getAmount(n, m, isl);
			}
			out.println(ans);
		}
	}
	
	int getAmount(int n, int m, Isl[] isl) {
		Arrays.sort(isl);
		PriorityQueue<Edge> pq = new PriorityQueue<>();
		UnionFind uf = new UnionFind(n);
		int cost = 0;
		int group = 0;
		int last = -1;
		for(int i = 0; i < n; i++) {
			pq.addAll(isl[i].edge);
			group++;
			if(i < n - 1 && isl[i + 1].h == isl[i].h)
				continue;
			while(!pq.isEmpty()) {
				Edge e = pq.poll();
				if(uf.same(e.from, e.to)) continue;
				group--;
				cost += e.cost;
				uf.unite(e.from, e.to);
//				out.printf("use: %d - %d (%d)\n", e.from + 1, e.to + 1, e.cost);
			}
			if(group >= 2) {
				last = i;
			}
		}
		for(int i = 0; i < n ; i++) {
			if(!uf.same(0, i)) return 0;
		}
		return last == -1 ? cost : -last;
	}
}

class Isl implements Comparable<Isl> {
	int id, h;
	List<Edge> edge;
	public Isl(int id, int h) {
		this.id = id;
		this.h = h;
		edge = new ArrayList<>();
	}
	
	@Override
	public int compareTo(Isl o) {
		return o.h - h;
	}
}

class Edge implements Comparable<Edge> {
	int from, to, cost;
	public Edge(int from, int to, int cost) {
		this.from = from;
		this.to = to;
		this.cost = cost;
	}
	@Override
	public int compareTo(Edge o) {
		return cost - o.cost;
	}
}

class UnionFind{
	int[] par;
	int[] rank;
	UnionFind(int n){
		par = new int[n];
		rank = new int[n];
		for(int i=0; i<n; i++){
			par[i] = i;
			rank[i] = 0;
		}
	}
	int find(int x){
		if(par[x] == x) return x;
		return par[x] = find(par[x]);
	}
	void unite(int x, int y){
		x = find(x);
		y = find(y);
		if(x==y) return;
		if(rank[x] > rank[y]) par[x] = y;
		else{
			par[y] = x;
			if(rank[x] == rank[y]) rank[x]++;
		}
	}
	boolean same(int x, int y){
		return find(x) == find(y);
	}
}

@SuppressWarnings("serial")
class MultiSet<T> extends HashMap<T, Integer>{
	@Override public Integer get(Object key){return containsKey(key)?super.get(key):0;}
	public void add(T key,int v){put(key,get(key)+v);}
	public void add(T key){put(key,get(key)+1);}
	public void sub(T key){final int v=get(key);if(v==1)remove(key);else put(key,v-1);}
	public MultiSet<T> merge(MultiSet<T> set)
	{MultiSet<T>s,l;if(this.size()<set.size()){s=this;l=set;}else{s=set;l=this;}
	for(Entry<T,Integer>e:s.entrySet())l.add(e.getKey(),e.getValue());return l;}
}

class Writer extends PrintWriter{
	public Writer(String filename)throws IOException
	{super(new BufferedWriter(new FileWriter(filename)));}
	public Writer()throws IOException{super(System.out);}
}
class ContestScanner implements Closeable{
	private BufferedReader in;private int c=-2;
	public ContestScanner()throws IOException 
	{in=new BufferedReader(new InputStreamReader(System.in));}
	public ContestScanner(String filename)throws IOException
	{in=new BufferedReader(new InputStreamReader(new FileInputStream(filename)));}
	public String nextToken()throws IOException {
		StringBuilder sb=new StringBuilder();
		while((c=in.read())!=-1&&Character.isWhitespace(c));
		while(c!=-1&&!Character.isWhitespace(c)){sb.append((char)c);c=in.read();}
		return sb.toString();
	}
	public String readLine()throws IOException{
		StringBuilder sb=new StringBuilder();if(c==-2)c=in.read();
		while(c!=-1&&c!='\n'&&c!='\r'){sb.append((char)c);c=in.read();}
		return sb.toString();
	}
	public long nextLong()throws IOException,NumberFormatException
	{return Long.parseLong(nextToken());}
	public int nextInt()throws NumberFormatException,IOException
	{return(int)nextLong();}
	public double nextDouble()throws NumberFormatException,IOException 
	{return Double.parseDouble(nextToken());}
	public void close() throws IOException {in.close();}
}
