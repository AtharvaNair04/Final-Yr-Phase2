import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Random;

public class Main {
	static InputStream is;
	static PrintWriter out;
	static String INPUT = "";
	
	static void solve()
	{
		int n = ni();
		int[] a = na(n);
		char[] s = ns(n-1);
		
		int low = 0, high = n+1;
		while(high - low > 1){
			int h = high+low>>1;
			if(ok(h, s, a)){
				low =h;
			}else{
				high = h;
			}
		}
		out.println(low);
	}
	
	static boolean ok(int h, char[] s, int[] a)
	{
		Node less = null;
		Node more = null;
		int nmore = 0, nless = 0;
		boolean f = a[0] >= h;
		for(int v : a){
			if(v >= h){
				if(nless > 0){
					less = merge(less, new Node(nless));
				}
				nmore++;
				nless = 0;
			}else{
				if(nmore > 0){
					more = merge(more, new Node(nmore));
				}
				nless++;
				nmore = 0;
			}
		}
		if(nless > 0){
			less = merge(less, new Node(nless));
		}
		if(nmore > 0){
			more = merge(more, new Node(nmore));
		}
		
		for(char c : s){
//			tr(h, c, f);
//			tr(toString(less, ""));
//			tr(toString(more, ""));
			if(c == 'M'){
				add(less, 0, count(less), -1);
				int left = f ? 1 : 0;
				int right = count(more) > count(less) || count(more) == count(less) && !f ? count(more)-1 : count(more);
				add(more, left, right, 1);
				while(less != null){
					Node argmin = less.argmin;
					if(argmin != null && less.min <= 0){
						int ind = index(argmin);
						if(f){
							if(count(more) >= ind+2){
								add(more, ind, ind+1, get(more, ind+1).v);
								more = erase(more, ind+1);
							}
							less = erase(less, ind);
						}else{
							if(ind >= 1){
								if(count(more) >= ind+1){
									add(more, ind-1, ind, get(more, ind).v);
									more = erase(more, ind);
								}
							}else{
								f = true;
							}
							less = erase(less, ind);
						}
					}else{
						break;
					}
				}
			}else{
				add(more, 0, count(more), -1);
				int left = !f ? 1 : 0;
				int right = count(less) > count(more) || count(less) == count(more) && f ? count(less)-1 : count(less);
				add(less, left, right, 1);
				while(more != null){
					Node argmin = more.argmin;
					if(argmin != null && more.min <= 0){
						int ind = index(argmin);
						if(!f){
							if(count(less) >= ind+2){
								add(less, ind, ind+1, get(less, ind+1).v);
								less = erase(less, ind+1);
							}
							more = erase(more, ind);
						}else{
							if(ind >= 1){
								if(count(less) >= ind+1){
									add(less, ind-1, ind, get(less, ind).v);
									less = erase(less, ind);
								}
							}else{
								f = false;
							}
							more = erase(more, ind);
						}
					}else{
						break;
					}
				}
			}
		}
		return count(more) >= 1;
	}
	
	public static Random gen = new Random();
	
	static public class Node
	{
		public int v; // value
		public long priority;
		public Node left, right, parent;
		
		public int count;
		
		public int add; // add cover
		public int min; 
		public Node argmin;
		public int temp;
		
		public Node(int v)
		{
			this.v = v;
			priority = gen.nextLong();
			update(this);
		}
		
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("Node [v=");
			builder.append(v);
			builder.append(", count=");
			builder.append(count);
			builder.append(", parent=");
			builder.append(parent != null ? parent.v : "null");
			builder.append(", add=");
			builder.append(add);
			builder.append(", min=");
			builder.append(min);
			builder.append("]");
			return builder.toString();
		}
	}
	
	public static String toString(Node a, String indent)
	{
		if(a == null)return "";
		StringBuilder sb = new StringBuilder();
		sb.append(toString(a.left, indent + "  "));
		sb.append(indent).append(a).append("\n");
		sb.append(toString(a.right, indent + "  "));
		return sb.toString();
	}


	public static Node update(Node a)
	{
		if(a == null)return null;
		a.count = 1;
		if(a.left != null)a.count += a.left.count;
		if(a.right != null)a.count += a.right.count;
		
		a.min = a.v; a.argmin = a;
		if(a.left != null && a.left.min+a.add < a.min){
			a.min = a.left.min+a.add; a.argmin = a.left.argmin;
		}
		if(a.right != null && a.right.min+a.add < a.min){
			a.min = a.right.min+a.add; a.argmin = a.right.argmin;
		}
		return a;
	}
	
	public static Node disconnect(Node a)
	{
		if(a == null)return null;
		a.left = a.right = a.parent = null;
		return update(a);
	}
	
	public static Node root(Node x)
	{
		if(x == null)return null;
		while(x.parent != null)x = x.parent;
		return x;
	}
	
	public static int count(Node a)
	{
		return a == null ? 0 : a.count;
	}
	
	public static void setParent(Node a, Node par)
	{
		if(a != null)a.parent = par;
	}
	
	public static int min(Node a, int L, int R)
	{
		if(a == null || L >= R || L >= count(a) || R <= 0)return Integer.MAX_VALUE / 2;
		if(L <= 0 && R >= count(a)){
			return a.min;
		}else{
			int lmin = min(a.left, L, R);
			int rmin = min(a.right, L-count(a.left)-1, R-count(a.left)-1);
			int min = Math.min(lmin, rmin) + a.add;
			if(L <= count(a.left) && count(a.left) < R)min = Math.min(min, a.v);
			return min;
		}
	}
	
	public static Node argmin(Node a, int L, int R)
	{
		if(a == null || L >= R || L >= count(a) || R <= 0)return null;
		if(L <= 0 && R >= count(a)){
			a.argmin.temp = a.min;
			return a.argmin;
		}else{
			fall(a);
			Node lmin = argmin(a.left, L, R);
			Node rmin = argmin(a.right, L-count(a.left)-1, R-count(a.left)-1);
			Node min = lmin.temp < rmin.temp ? lmin : rmin;
			min.temp += a.add;
			if(L <= count(a.left) && count(a.left) < R){
				if(a.v < lmin.temp){
					min = a;
					min.temp = a.v;
				}
			}
			return min;
		}
	}
	
	static int val(Node n){ return n == null ? Integer.MAX_VALUE / 2 : n.v; }
	
	public static void add(Node a, int L, int R, int V)
	{
		if(a == null || L >= R || L >= count(a) || R <= 0)return;
		if(L <= 0 && R >= count(a)){
			a.v += V;
			a.add += V;
		}else{
			add(a.left, L, R, V);
			add(a.right, L-count(a.left)-1, R-count(a.left)-1, V);
			if(L <= count(a.left) && count(a.left) < R)a.v += V;
		}
		update(a);
	}
	
	public static void fall(Node a)
	{
		if(a == null)return;
		if(a.left != null){
			a.left.add += a.add;
			a.left.v += a.add;
			update(a.left);
		}
		if(a.right != null){
			a.right.add += a.add;
			a.right.v += a.add;
			update(a.right);
		}
		a.add = 0;
		update(a);
	}
	
	public static Node merge(Node a, Node b)
	{
		if(b == null)return a;
		if(a == null)return b;
		if(a.priority > b.priority){
			fall(a);
			setParent(a.right, null);
			setParent(b, null);
			a.right = merge(a.right, b);
			setParent(a.right, a);
			return update(a);
		}else{
			fall(b);
			setParent(a, null);
			setParent(b.left, null);
			b.left = merge(a, b.left);
			setParent(b.left, b);
			return update(b);
		}
	}
	
	// [0,K),[K,N)
	public static Node[] split(Node a, int K)
	{
		if(a == null)return new Node[]{null, null};
		fall(a);
		if(K <= count(a.left)){
			setParent(a.left, null);
			Node[] s = split(a.left, K);
			a.left = s[1];
			setParent(a.left, a);
			s[1] = update(a);
			return s;
		}else{
			setParent(a.right, null);
			Node[] s = split(a.right, K-count(a.left)-1);
			a.right = s[0];
			setParent(a.right, a);
			s[0] = update(a);
			return s;
		}
	}
	
	public static Node insert(Node a, int K, Node b)
	{
		if(a == null)return b;
		fall(a);
		if(b.priority < a.priority){
			if(K <= count(a.left)){
				a.left = insert(a.left, K, b);
				setParent(a.left, a);
			}else{
				a.right = insert(a.right, K-count(a.left)-1, b);
				setParent(a.right, a);
			}
			return update(a);
		}else{
			Node[] ch = split(a, K);
			b.left = ch[0]; b.right = ch[1];
			setParent(b.left, b);
			setParent(b.right, b);
			return update(b);
		}
	}
	
	// delete K-th
	public static Node erase(Node a, int K)
	{
		if(a == null)return null;
		if(K < count(a.left)){
			a.left = erase(a.left, K);
			setParent(a.left, a);
			return update(a);
		}else if(K == count(a.left)){
			fall(a);
			setParent(a.left, null);
			setParent(a.right, null);
			Node aa = merge(a.left, a.right);
			disconnect(a);
			return aa;
		}else{
			a.right = erase(a.right, K-count(a.left)-1);
			setParent(a.right, a);
			return update(a);
		}
	}
	
	public static Node get(Node a, int K)
	{
		while(a != null){
			fall(a);
			if(K < count(a.left)){
				a = a.left;
			}else if(K == count(a.left)){
				break;
			}else{
				K = K - count(a.left)-1;
				a = a.right;
			}
		}
		return a;
	}
	
	static Node[] Q = new Node[100];
	public static Node update(Node a, int K, int v)
	{
		int p = 0;
		while(a != null){
			Q[p++] = a;
			if(K < count(a.left)){
				a = a.left;
			}else if(K == count(a.left)){
				break;
			}else{
				K = K - count(a.left)-1;
				a = a.right;
			}
		}
		a.v = v;
		while(p > 0){
			update(Q[--p]);
		}
		return a;
	}
	
	public static int index(Node a)
	{
		if(a == null)return -1;
		int ind = count(a.left);
		while(a != null){
			Node par = a.parent;
			if(par != null && par.right == a){
				ind += count(par.left) + 1;
			}
			a = par;
		}
		return ind;
	}
	
	public static Node[] nodes(Node a) { return nodes(a, new Node[count(a)], 0, count(a)); }
	public static Node[] nodes(Node a, Node[] ns, int L, int R)
	{
		if(a == null)return ns;
		nodes(a.left, ns, L, L+count(a.left));
		ns[L+count(a.left)] = a;
		nodes(a.right, ns, R-count(a.right), R);
		return ns;
	}
	
	public static void main(String[] args) throws Exception
	{
		long S = System.currentTimeMillis();
		is = INPUT.isEmpty() ? System.in : new ByteArrayInputStream(INPUT.getBytes());
		out = new PrintWriter(System.out);
		
		solve();
		out.flush();
		long G = System.currentTimeMillis();
		tr(G-S+"ms");
	}
	
	private static boolean eof()
	{
		if(lenbuf == -1)return true;
		int lptr = ptrbuf;
		while(lptr < lenbuf)if(!isSpaceChar(inbuf[lptr++]))return false;
		
		try {
			is.mark(1000);
			while(true){
				int b = is.read();
				if(b == -1){
					is.reset();
					return true;
				}else if(!isSpaceChar(b)){
					is.reset();
					return false;
				}
			}
		} catch (IOException e) {
			return true;
		}
	}
	
	private static byte[] inbuf = new byte[1024];
	static int lenbuf = 0, ptrbuf = 0;
	
	private static int readByte()
	{
		if(lenbuf == -1)throw new InputMismatchException();
		if(ptrbuf >= lenbuf){
			ptrbuf = 0;
			try { lenbuf = is.read(inbuf); } catch (IOException e) { throw new InputMismatchException(); }
			if(lenbuf <= 0)return -1;
		}
		return inbuf[ptrbuf++];
	}
	
	private static boolean isSpaceChar(int c) { return !(c >= 33 && c <= 126); }
//	private static boolean isSpaceChar(int c) { return !(c >= 32 && c <= 126); }
	private static int skip() { int b; while((b = readByte()) != -1 && isSpaceChar(b)); return b; }
	
	private static double nd() { return Double.parseDouble(ns()); }
	private static char nc() { return (char)skip(); }
	
	private static String ns()
	{
		int b = skip();
		StringBuilder sb = new StringBuilder();
		while(!(isSpaceChar(b))){
			sb.appendCodePoint(b);
			b = readByte();
		}
		return sb.toString();
	}
	
	private static char[] ns(int n)
	{
		char[] buf = new char[n];
		int b = skip(), p = 0;
		while(p < n && !(isSpaceChar(b))){
			buf[p++] = (char)b;
			b = readByte();
		}
		return n == p ? buf : Arrays.copyOf(buf, p);
	}
	
	private static char[][] nm(int n, int m)
	{
		char[][] map = new char[n][];
		for(int i = 0;i < n;i++)map[i] = ns(m);
		return map;
	}
	
	private static int[] na(int n)
	{
		int[] a = new int[n];
		for(int i = 0;i < n;i++)a[i] = ni();
		return a;
	}
	
	private static int ni()
	{
		int num = 0, b;
		boolean minus = false;
		while((b = readByte()) != -1 && !((b >= '0' && b <= '9') || b == '-'));
		if(b == '-'){
			minus = true;
			b = readByte();
		}
		
		while(true){
			if(b >= '0' && b <= '9'){
				num = num * 10 + (b - '0');
			}else{
				return minus ? -num : num;
			}
			b = readByte();
		}
	}
	
	private static long nl()
	{
		long num = 0;
		int b;
		boolean minus = false;
		while((b = readByte()) != -1 && !((b >= '0' && b <= '9') || b == '-'));
		if(b == '-'){
			minus = true;
			b = readByte();
		}
		
		while(true){
			if(b >= '0' && b <= '9'){
				num = num * 10 + (b - '0');
			}else{
				return minus ? -num : num;
			}
			b = readByte();
		}
	}
	
	private static void tr(Object... o) { if(INPUT.length() != 0)System.out.println(Arrays.deepToString(o)); }
}
