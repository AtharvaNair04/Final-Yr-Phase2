import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.StringTokenizer;

public class Main {
	
	public static void main(String[] args) throws InterruptedException {
		FastScanner fs=new FastScanner();
		int n=fs.nextInt(), nQ=fs.nextInt();
		
		int[] a=fs.readArray(n);
		int[] lastSeen=new int[n+10];
		Arrays.fill(lastSeen, n);
		BIT bit=new BIT(n+100);
		
		Query[] queries=new Query[nQ];
		ArrayList<Query>[] queriesAt=new ArrayList[n];
		for (int i=0; i<n; i++) queriesAt[i]=new ArrayList<>();
		for (int qq=0; qq<nQ; qq++) {
			int l=fs.nextInt()-1, r=fs.nextInt()-1;
			Query q=new Query(l, r);
			queries[qq]=q;
			queriesAt[l].add(q);
		}
		
		for (int l=n-1; l>=0; l--) {
			bit.update(lastSeen[a[l]], -1);
			bit.update(l, 1);
			lastSeen[a[l]]=l;
			
			for (Query qq: queriesAt[l]) {
				qq.ans=bit.read(qq.r);
			}
		}
		PrintWriter out=new PrintWriter(System.out);
		for (Query qq:queries)
			out.println(qq.ans);
		out.close();
	}
	
	static class Query {
		int l, r, ans;
		public Query(int l, int r) {
			this.l=l;
			this.r=r;
		}
	}
	
	static class BIT {
		int n;
		int[] tree;
		
		public BIT(int n) {
			this.n = n;
			tree = new int[n + 2];
		}
		
		int read(int i) {
			i++;
			int sum = 0;
			while (i > 0) {
				sum += tree[i];
				i -= i & -i;
			}
			return sum;
		}
		
		void update(int i, int val) {
			i++;
			while (i <= n) {
				tree[i] += val;
				i += i & -i;
			}
		}

		// if the BIT is a freq array, returns the
		// index of the kth item, or n if there are fewer
		// than k items.
		int getKth(int k) {
			int e=Integer.highestOneBit(n), o=0;
			for (; e!=0; e>>=1) {
				if (e+o<=n && tree[e+o]<=k) {
					k-=tree[e+o];
					o+=e;
				}
			}
			return o;
		}
		
	}

	
	void sort(int[] a) {
		ArrayList<Integer> list=new ArrayList<>();
		for (int i:a) list.add(i);
		Collections.sort(list);
		for (int i=0; i<a.length; i++) a[i]=list.get(i);
	}
	
	
	static class FastScanner {
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st=new StringTokenizer("");
		
		public String next() {
			while (!st.hasMoreTokens())
				try {
					st=new StringTokenizer(br.readLine());
				} catch (IOException e) {
					e.printStackTrace();
				}
			return st.nextToken();
			
		}
		
		public int nextInt() {
			return Integer.parseInt(next());
		}
		
		public int[] readArray(int n) {
			int[] a=new int[n];
			for (int i=0; i<n; i++) a[i]=nextInt();
			return a;
		}
		
		public long nextLong() {
			return Long.parseLong(next());
		}
		
		public long[] readLongArray(int n) {
			long[] a=new long[n];
			for (int i=0; i<n; i++) a[i]=nextLong();
			return a;
		}
	}

}
