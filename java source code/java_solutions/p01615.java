import java.util.*;
import static java.util.Arrays.*;
import static java.lang.Math.*;

public class Main {

	int INF = 1 << 28;
	//long INF = 1L << 62;
	double EPS = 1e-10;
	int n, m;
	int[] mem;
	ArrayList<E>[] g;
	@SuppressWarnings("unchecked")
	void run() {
		Scanner sc = new Scanner(System.in);
		n = sc.nextInt(); m = sc.nextInt();
		mem = new int[n]; fill(mem, -1);
		g = new ArrayList[n];
		for(int i=0;i<n;i++) g[i] = new ArrayList<E>();
		for(int i=0;i<m;i++) {
			int a = sc.nextInt(), b = sc.nextInt(), c = sc.nextInt();
			g[a].add(new E(b, c));
		}
		
		System.out.println(dfs(0));
	}
	
	int dfs(int p) {
		if( mem[p] >= 0 ) return mem[p];
		if( p == n-1 ) return mem[p] = 0;
		int max = 0;
		for(E e: g[p]) {
			max = max(max, dfs(e.to) + e.val);
		}
		return mem[p] = max;
	}
	
	class E{
		int to, val;
		E(int to, int val) {
			this.to = to;
			this.val = val;
		}
	}

	public static void main(String[] args) {
		new Main().run();
	}
}
