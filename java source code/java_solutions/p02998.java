import java.util.*;

public class Main {
	public static class UnionFind {
		int[] param;
		int[] rank;
		int N;
		int xlen;
		
		public UnionFind(int n) {
			N=n;
			param = new int[N];
			rank = new int[N];
			for(int i=0; i<N; i++)
				param[i] = i;
		}
		
		public void init() {
			for(int i=0; i<N; i++)
				param[i] = i;
			Arrays.fill(rank, 0);
		}
		
		public UnionFind(int x, int y) {
			this(x*y);
			xlen=y;
		}
		
		public boolean same(int a, int b) {
			return getRoot(a)==getRoot(b);
		}
		
		public boolean same(int x1, int y1, int x2, int y2) {
			return same(x1*xlen+y1, x2*xlen+y2);
		}
		
		public int getRoot(int a) {
			if(param[a]==a) return a;
			else return (param[a] = getRoot(param[a]));
		}
		
		public void unite(int a, int b) {
			a = getRoot(a);
			b = getRoot(b);
			if(a==b)
				return;
			
			if(rank[a] < rank[b]) {
				param[a] = b;
			} else {
				param[b] = a;
				if(rank[a]==rank[b])
					rank[a]++;
			}
		}
		
		public void unite(int x1, int y1, int x2, int y2) {
			unite(x1*xlen+y1, x2*xlen+y2);
		}
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		HashMap<Integer, ArrayList<Integer>> xset = new HashMap<>();
		HashMap<Integer, ArrayList<Integer>> yset = new HashMap<>();
		HashSet<Integer> xall = new HashSet<>();
		
		for(int i=0; i<N; i++) {
			int x = sc.nextInt()-1;
			int y = sc.nextInt()-1;
			
			ArrayList<Integer> ys = xset.get(x);
			if(ys == null) {
				ys = new ArrayList<>();
			}
			ys.add(y);
			xset.put(x, ys);
			
			ArrayList<Integer> xs = yset.get(y);
			if(xs == null) {
				xs = new ArrayList<>();
			}
			xs.add(x);
			yset.put(y, xs);
			
			xall.add(x);
		}
		
		UnionFind uf = new UnionFind(100000);
		Iterator<Integer> it = yset.keySet().iterator();
		while(it.hasNext()) {
			int y = it.next();
			ArrayList<Integer> xlist = yset.get(y);
			for(int i : xlist) {
				uf.unite(xlist.get(0), i);
			}
		}
		
		HashMap<Integer, ArrayList<Integer>> xgroup = new HashMap<>();
		
		for(int x=0; x<100000; x++) {
			if(xall.contains(x)) {
				int root = uf.getRoot(x);
				ArrayList<Integer> xs = xgroup.get(root);
				if(xs == null) {
					xs = new ArrayList<>();
				}
				xs.add(x);
				xgroup.put(root, xs);
			}
		}
		
		long finalSize = 0;
		it = xgroup.keySet().iterator();
		while(it.hasNext()) {
			ArrayList<Integer> xlist = xgroup.get(it.next());
			HashSet<Integer> ys = new HashSet<>();
			for(int x : xlist) {
				ys.addAll(xset.get(x));
			}
			finalSize += (long)xlist.size()*ys.size();
		}
		
		System.out.println(finalSize - N);
		sc.close();
	}
}
