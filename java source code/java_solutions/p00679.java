import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		while (true) {
			int N = sc.nextInt();
			if (N == 0) break;
			int Q = sc.nextInt();
			Node tree = new Node(-1000000, -1000000, -1000000, 1000000, 1000000, 1000000);
			for (int i = 0; i < N; ++i) {
				int X = Integer.parseInt(sc.next());
				int Y = Integer.parseInt(sc.next());
				int Z = Integer.parseInt(sc.next());
				tree.add(new P(X, Y, Z));
			}
			for (int i = 0; i < Q; ++i) {
				int M = Integer.parseInt(sc.next());
				P[] ps = new P[M];
				double[] near = new double[M];
				for (int j = 0; j < M; ++j) {
					int X = Integer.parseInt(sc.next());
					int Y = Integer.parseInt(sc.next());
					int Z = Integer.parseInt(sc.next());
					ps[j] = new P(X, Y, Z);
					P n = tree.find(ps[j], 1 << 30);
					near[j] = Math.sqrt(n.d2(ps[j]));
				}
				double ans = 0;
				for (int j = 0; j < M - 1; ++j) {
					double direct = Math.sqrt(ps[j].d2(ps[j + 1]));
					double warp = near[j] + near[j + 1];
					ans += Math.min(direct, warp);
				}
				System.out.printf("%.8f\n", ans);
			}
		}
	}

	static class P {
		int x, y, z;

		public P(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		long d2(P p) {
			return sq(p.x - this.x) + sq(p.y - this.y) + sq(p.z - this.z);
		}

		static long sq(long v) {
			return v * v;
		}

	}

	static class Node {
		int x1, y1, z1, x2, y2, z2;
		Node[] child;
		ArrayList<P> ps;

		public Node(int x1, int y1, int z1, int x2, int y2, int z2) {
			this.x1 = x1;
			this.y1 = y1;
			this.z1 = z1;
			this.x2 = x2;
			this.y2 = y2;
			this.z2 = z2;
		}

		void add(P p) {
			if (leaf()) {
				if (this.ps == null) this.ps = new ArrayList<P>();
				this.ps.add(p);
				return;
			}
			int ind = index(p);
			if (child == null) {
				child = new Node[8];
				int[] nx = { x1, (x1 + x2) / 2, x2 };
				int[] ny = { y1, (y1 + y2) / 2, y2 };
				int[] nz = { z1, (z1 + z2) / 2, z2 };
				for (int i = 0; i < 2; ++i) {
					for (int j = 0; j < 2; ++j) {
						for (int k = 0; k < 2; ++k) {
							child[i * 4 + j * 2 + k] = new Node(nx[i], ny[j], nz[k], nx[i + 1], ny[j + 1], nz[k + 1]);
						}
					}
				}
			}
			child[ind].add(p);
		}

		P find(P p, double best) {
			P ret = null;
			if (leaf()) {
				if (this.ps == null) return null;
				long min = Long.MAX_VALUE;
				for (P px : ps) {
					long d2 = px.d2(p);
					if (d2 < min) {
						min = d2;
						ret = px;
					}
				}
				return ret;
			}
			if (child == null) return null;
			int ind = index(p);
			ret = child[ind].find(p, best);
			if (ret != null) {
				best = Math.min(best, Math.sqrt(ret.d2(p)));
			}
			for (int i = 0; i < 8; ++i) {
				if (i == ind) continue;
				if (best > child[ind].dist(p)) {
					P tmp = child[i].find(p, best);
					if (tmp != null && (ret == null || tmp.d2(p) < ret.d2(p))) {
						ret = tmp;
						best = Math.min(best, Math.sqrt(ret.d2(p)));
					}
				}
			}
			return ret;
		}

		int index(P p) {
			int ret = 0;
			if (p.x > (x1 + x2) / 2) ret += 4;
			if (p.y > (y1 + y2) / 2) ret += 2;
			if (p.z > (z1 + z2) / 2) ret += 1;
			return ret;
		}

		int dist(P p) {
			int ret = 0;
			if (p.x < x1) {
				ret = Math.max(ret, x1 - p.x);
			} else if (x2 < p.x) {
				ret = Math.max(ret, p.x - x2);
			}
			if (p.y < y1) {
				ret = Math.max(ret, y1 - p.y);
			} else if (y2 < p.y) {
				ret = Math.max(ret, p.y - y2);
			}
			if (p.z < z1) {
				ret = Math.max(ret, z1 - p.z);
			} else if (z2 < p.z) {
				ret = Math.max(ret, p.z - z2);
			}
			return ret;
		}

		boolean leaf() {
			return x2 - x1 < 1000;
		}

	}

}
