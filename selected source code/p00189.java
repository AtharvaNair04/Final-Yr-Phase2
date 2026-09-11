import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.StringTokenizer;
//http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=0189
class Main{
	public static void main(String[] args) {
		int INF=10000007;
		SC sc=new SC(System.in);
		while(true) {
			int N=sc.nextInt();
			if(N==0) {
				System.exit(0);
			}
			Vertex[] miti=new Vertex[10];
			int[] rtx=new int[10];
			Arrays.fill(rtx, 0);
			for(int i=0; i<10; i++) {
				miti[i]=new Vertex();
			}
			int max=0;
			for(int i=0; i<N; i++) {
				int s=sc.nextInt();
				int g=sc.nextInt();
				int cost=sc.nextInt();
				miti[s].add(g, cost);
				miti[g].add(s, cost);
				max=Math.max(Math.max(max, s),Math.max(max, g));
				rtx[s]++;
				rtx[g]++;
			}
			int[] min=new int[max+1];
			int[] mts=new int[max+1];
			for(int i=0; i<=max; i++) {
				Arrays.fill(min, INF);
				min[i]=0;
				while(true) {
					boolean update=false;
					for(int j=0; j<=max; j++) {
						for(int k=0; k<rtx[j]; k++) {
							Pair<Integer,Integer> p=miti[j].get(k);
							//pl((miti[j].get(k)).toString()+" "+j+" "+k+" "+i);
							if(min[j]!=INF && min[Pair.getKey(p)]>min[j]+Pair.getValue(p)) {
								min[Pair.getKey(p)]=min[j]+Pair.getValue(p);
								update=true;
							}
						}
	 				}
					if(!update) {
						break;
					}
				}
				int tmp=0;
				for(int j=0; j<=max; j++) {
					tmp+=min[j];
				}
				mts[i]=tmp*10+i;
			}
			Arrays.sort(mts);
			pl(mts[0]%10+" "+mts[0]/10);
		}
	}
	static class SC {
		private BufferedReader reader = null;
		private StringTokenizer tokenizer = null;
		public SC(InputStream in) {
			reader = new BufferedReader(new InputStreamReader(in));
		}
		public String next() {
			if (tokenizer == null || !tokenizer.hasMoreTokens()) {
				try {
					tokenizer = new StringTokenizer(reader.readLine());
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			}
			return tokenizer.nextToken();
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
		public String nextLine() {
			try {
				return reader.readLine();
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		}
	}
	public static void pl(Object o) {
		System.out.println(o);
	}
	public static void p(Object o) {
		System.out.print(o);
	}
	public static final class Pair<K, V> {		//powered by mikit
		public K key;
		public V value;
		public Pair(K key, V value) {
			this.key = key;
			this.value = value;
		}
		public static int getValue(Pair<Integer,Integer> a) {
			return a.value;
		}
		public static int getKey(Pair<Integer,Integer> a) {
			return a.key;
		}
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Pair<?, ?> pair = (Pair<?, ?>) o;
			return Objects.equals(key, pair.key) &&
					Objects.equals(value, pair.value);
		}
		public int hashCode() {
			return Objects.hash(key, value);
		}
		public String toString() {
			return "Pair{x= " + key+" y=" + value +"}";
		}
	}
	public static class Vertex{
		ArrayList <Pair<Integer,Integer>> rinsetu=new ArrayList<Pair<Integer,Integer>>();
		public void add(int V,int cost) {
			rinsetu.add(new Pair<Integer, Integer>(V,cost));
		}
		public void show() {		//隣接リストを出力
			for(int i=0; i<rinsetu.size(); i++) {
				System.out.println(rinsetu.get(i));
			}
		}
		public void sort_by_cost() {		//コストの低い順にソートします
			rinsetu.sort(Comparator.comparing(p -> p.value));
		}
		public void sort_by_Vertex() {		//向かう頂点の番号順にソートします
			rinsetu.sort(Comparator.comparing(p -> p.key));
		}
		public int find_Vertex(int a) {		//ある頂点から本当に特定の辺に行くか？
			for(int i=0; i<rinsetu.size(); i++) {
				if(a==Pair.getKey(rinsetu.get(i))) {
					return i;
				}
			}
			return -1;
		}
		public Pair<Integer, Integer> get(int i) {		//1辺のデータを渡す
			return rinsetu.get(i);
		}
	}
}


// トリプルのほうが良くない？WWOWWO
