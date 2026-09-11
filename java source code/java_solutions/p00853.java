import java.util.Arrays;
import java.util.BitSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class Main {
	
	Scanner sc;

	Main() {
		
		sc = new Scanner(System.in);
	}

	int n, m, k, a, b,
		c[][] = new int[50][50];

	void run() {

		for (;;) {

			n = sc.nextInt();
			m = sc.nextInt();
			k = sc.nextInt();
			a = sc.nextInt();
			b = sc.nextInt();
			if ((n | m | k | a -- | b --) == 0) break;

			for (int i = 0; i < n; i++) Arrays.fill(c[i], 0, n, INF);
			for (int i = 0; i < m; i++) {

				int x = sc.nextInt() - 1,
					y = sc.nextInt() - 1;
				c[x][y] = sc.nextInt();
			}
			
			kd();
			if (kq.isEmpty()) System.out.println("None");
			else {
				
				P p = kq.remove();
				int i;
				for (i = 0; i < p.p.length - 1; i ++) {
					
					System.out.print(p.p[i] + 1 + "-");
				}
				System.out.println(p.p[i] + 1);
			}
		}
	}

	class P implements Comparable<P> {

		int d;
		int[] p;
		BitSet s;
		int x;
		
		P(int a, int[] b) {

			d = a;
			p = b;
		}

		@Override
		public int compareTo(P t) {

			if (t == null) return - 1;
			if (d != t.d) return d - t.d;
			for (int i = 0; i < p.length; i ++) {
				
				if (i >= t.p.length) break;
				if (p[i] != t.p[i]) return p[i] - t.p[i];
			}
			return p.length - t.p.length;
		}
	}

	P z = new P(0, null);
	
	Queue<P> kq = new PriorityQueue<P>(200 * 50);
	
	void kd() {
		
		kq.clear();
		BitSet s = new BitSet(n);
		Arrays.fill(d, 0, n, null);
		d(a, 0, new int[]{a}, s);
		if (d[b] == null) return;
		d[b].s = s;
		d[b].x = 1;
		kq.add(d[b]);
		
		for (int i = 1; i < k; i ++) {
			
			if (kq.isEmpty()) break;
			
			P p = kq.remove();
			
			s = p.s.get(0, n);
			s.set(p.p[p.x]);
			int op[] = Arrays.copyOf(p.p, p.x);
			int od = 0;
			Arrays.fill(d, 0, n, null);
			for (int j = 2; j <= p.x; j ++) {
				
				od += c[p.p[j - 2]][p.p[j - 1]];
				d[p.p[j - 2]] = z;
			}
			
			d(p.p[p.x - 1], od, op, s);
			if (d[b] != null) {
				d[b].s = s;
				d[b].x = p.x;
				kq.add(d[b]);
			}
			
			for (int j = p.x + 1; j < p.p.length; j ++) {
				
				s = new BitSet(n);
				s.set(p.p[j]);
				op = Arrays.copyOf(p.p, j);
				od += c[p.p[j - 2]][p.p[j - 1]];
				Arrays.fill(d, 0, n, null);
				for (int k = 2; k <= j; k ++) d[p.p[k - 2]] = z;
				
				d(p.p[j - 1], od, op, s);
				if (d[b] != null) {
					d[b].s = s;
					d[b].x = j;
					kq.add(d[b]);
				}
			}
		}
	}
	
	int INF = 10000 * 50;
	P d[] = new P[50];
	Queue<P> q = new PriorityQueue<P>(50 * 49);

	void a(P p, int f, int t) {
		
		if (c[f][t] >= INF) return;
		P np = new P(p.d + c[f][t], Arrays.copyOf(p.p, p.p.length + 1));
		np.p[np.p.length - 1] = t;
		if (np.compareTo(d[t]) >= 0) return;
		d[t] = np;
		q.add(d[t]);
	}
	
	void d(int a, int od, int[] op, BitSet s) {

		q.clear();
		d[a] = new P(od, op);
		for (int i = 0; i < n; i++) {

			if (s.get(i)) continue;
			a(d[a], a, i);
		}

		while (!q.isEmpty()) {

			P p = q.remove();
			int v = p.p[p.p.length - 1];
			
			if (v == b) break;
			if (p.compareTo(d[v]) > 0) continue;

			for (int i = 0; i < n; i++) a(p, v, i);
		}
	}

	public static void main(String args[]) {
		
		Main m = new Main();
		m.run();
	}
}
