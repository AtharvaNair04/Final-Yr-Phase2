import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {

	Scanner sc = new Scanner(System.in);

	class V implements Comparable<V> {
		int t;
		int v;

		E prev = null;
		long cost = 32345678;

		long min = 34345678;
		int h;
		LinkedList<E> es = new LinkedList<E>();

		public int compareTo(V t) {
			return (int) (cost - t.cost);
		}

	}

	class E {
		V from;
		V to;
		E rev;

		int cost;
		int cap;

		E(V from, V to, int cost, int cap) {
			this.from = from;
			this.to = to;
			this.cost = cost;
			this.cap = cap;
		}

	}

	long mcf(LinkedList<V> vs, int flow, V s, V t) {
		long res = 0;
		s.min = 0;
		s.cost = 0;
		for (; flow > 0;) {
			PriorityQueue<V> q = new PriorityQueue<V>();

			for (V vv : vs) {
				vv.min = 343456789;
			}
			s.min = 0;
			s.cost = 0;
			q.add(s);
//			 System.out.println("stataoiuw  " + flow);

			// sc.nextLine();
			for (; !q.isEmpty();) {
				V vv = q.poll();

				// System.out.println("test " + vv.t + " " + vv.v + " " +
				// vv.cost);
				if (vv.cost != vv.min) {
					continue;
				}

				for (E dij : vv.es) {
					// System.out.println(dij.cap + " " + dij.to.min);
					long tmp = dij.cost + vv.cost - dij.to.h + vv.h;
					// System.out.println(tmp);
					if (dij.cap > 0 && tmp < dij.to.min) {
						dij.to.min = tmp;

						dij.to.prev = dij;
						dij.to.cost = tmp;
						q.add(dij.to);
					}
				}
			}

			int d = flow;
			for (E e = t.prev; e != null; e = e.from.prev) {
				d = Math.min(e.cap, d);
			}

			for (E e = t.prev; e != null; e = e.from.prev) {
				res += d * e.cost;
				e.cap -= d;
				e.rev.cap += d;
			}

			for (V vvv : vs) {
				vvv.h += vvv.min;
			}

			flow -= d;
		}
		return res;
	}

	void run() {
		int n = sc.nextInt();

		LinkedList<V> vs = new LinkedList<V>();
		V nil = new V();
		V s = new V();
		V t = new V();
		nil.t = 2;
		s.t = 3;
		t.t = 4;
		long sum = 0;

		for (int i = 0; i < n; i++) {
			int a = sc.nextInt();
			int b = sc.nextInt();
			sum += a + b;
			V va = null;
			V vb = null;
			for (V vn : vs) {
				if (vn.t == 0) {
					if (vn.v == a) {
						va = vn;
					}
				}
			}
			
			boolean aaa = false;
			if (va == null) {
				aaa = true;
				va = new V();
				vs.add(va);
				va.t = 0;
				va.v = a;
			}

			
			for (V vn : vs) {
				if (vn.t == 0) {
					if (vn.v == b) {
						vb = vn;
					}
				}
			}

			V vz = new V();
			vz.t = 1;
			vz.v = i;

			boolean bbb = false;
			if (vb == null) {
				bbb = true;
				vb = new V();
				vs.add(vb);
				vb.t = 0;
				vb.v = b;
			}

			E ea = new E(vz, va, -a, 0);
			E ae = new E(va, vz, a, 1);
			ea.rev = ae;
			ae.rev = ea;

			E eb = new E(vz, vb, -b, 0);
			E be = new E(vb, vz, b, 1);
			eb.rev = be;
			be.rev = eb;

			E en = new E(vz, nil, -a - b, 0);
			E ne = new E(nil, vz, a + b, 1);
			en.rev = ne;
			ne.rev = en;

			va.es.add(ae);
			vb.es.add(be);
			nil.es.add(ne);

			vz.es.add(ea);
			vz.es.add(eb);
			vz.es.add(en);

			if (i == 0) {
				E sn = new E(s, nil, 0, n);
				E ns = new E(nil, s, 0, 0);
				sn.rev = ns;
				ns.rev = sn;

				s.es.add(sn);
				nil.es.add(ns);
			}
			if (aaa) {
				E sa = new E(s, va, 0, 1);
				E as = new E(va, s, 0, 0);
				sa.rev = as;
				as.rev = sa;
				s.es.add(sa);
				va.es.add(as);
			}
			if (bbb) {
				E sb = new E(s, vb, 0, 1);
				E bs = new E(vb, s, 0, 0);
				sb.rev = bs;
				bs.rev = sb;
				s.es.add(sb);
				vb.es.add(bs);
			}
			E zt = new E(vz, t, 0, 1);
			E tz = new E(t, vz, 0, 0);
			zt.rev = tz;
			tz.rev = zt;

			vz.es.add(zt);
			t.es.add(tz);
			vs.add(vz);
		}
		vs.add(nil);
		vs.add(s);
		vs.add(t);
		// System.out.println(vs.size());
		long ans = mcf(vs, n, s, t);
		// System.out.println(ans);

		System.out.println(sum - ans);

	}

	public static void main(String[] args) {

		new Main().run();
	}

}
