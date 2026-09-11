import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {
	Scanner sc;

	class E {
		P to;
		int type;

		E(P tto, int ttype) {
			to = tto;
			type = ttype;
		}
	}

	class S implements Comparable<S> {
		double cost;
		P p;

		S(P pp, double ccost) {
			p = pp;
			cost = ccost;
		}

		public int compareTo(S tar) {
			return cost - tar.cost > 0 ? 1 : -1;
		}

	}

	class P {
		double x;
		double y;

		int id = 0;
		LinkedList<E> next = new LinkedList<E>();

		P(double xx, double yy, int iid) {
			x = xx;
			y = yy;
			id = iid;
		}

		double dif(P b) {
			double xx = b.x - x;
			xx = xx * xx;
			double yy = b.y - y;
			yy = yy * yy;
			return Math.sqrt(xx + yy);
		}

	}

	void run() {
		for (;;) {
			int n = sc.nextInt();
			if(n==0){
				break;
			}
			int vw = sc.nextInt();
			int vc = sc.nextInt();

			LinkedList<P> l = new LinkedList<P>();
			for (int i = 0; i < n; i++) {
				int x = sc.nextInt() + 10000;
				int y = sc.nextInt() + 10000;
				l.add(new P(x, y, i));
			}

			P map[] = l.toArray(new P[0]);
			for (int i = 0; i < n - 1; i++) {
				map[i].next.add(new E(map[i + 1], 0));
				map[i + 1].next.add(new E(map[i], 0));
			}

			int ni = n;
			for (int i = 0; i < n; i++) {
				int j = i - 1; 

				if (j>=0&&map[j].y > map[i].y + 1e-10) {
					// arrenge left
					for (; j >= 0; j--) {
						if (map[j].y <= map[i].y + 1e-10) {
							P pa = map[j + 1];
							P pb = map[j];

							double hi = 1- (pa.y - map[i].y) / (pa.y - pb.y);
							P np = new P(pa.x * hi + pb.x * (1 - hi), pa.y * hi
									+ pb.y * (1 - hi), ni);
							map[i].next.add(new E(np, 1));
							np.next.add(new E(map[i], 1));

							
							
							np.next.add(new E(pa, 0));
							np.next.add(new E(pb, 0));
							pa.next.add(new E(np, 0));
							pb.next.add(new E(np, 0));

							ni++;
							break;
						}
					}
				}

				j = i + 1;					;

				// arrange right
				if (j<n&&map[j].y > map[i].y + 1e-10) {
					for (; j < n; j++) {
						if (map[j].y <= map[i].y + 1e-10) {
							P pa = map[j - 1];
							P pb = map[j];

							double hi = 1- (pa.y - map[i].y) / (pa.y - pb.y);
							P np = new P(pa.x * hi + pb.x * (1 - hi), pa.y * hi
									+ pb.y * (1 - hi), ni);
							map[i].next.add(new E(np, 1));
							np.next.add(new E(map[i], 1));

							np.next.add(new E(pa, 0));
							np.next.add(new E(pb, 0));
							pa.next.add(new E(np, 0));
							pb.next.add(new E(np, 0));

							ni++;
							break;
						}
					}
				}
			}

			double used[] = new double[ni];
			Arrays.fill(used, Double.MAX_VALUE / 3);
			PriorityQueue<S> q = new PriorityQueue<S>();
			q.add(new S(map[0], 0));

			for (;;) {
				if (q.isEmpty()) {
					break;
				}

				S s = q.poll();
				// System.out.println(s.p.id+" " +s.cost);

				if (used[s.p.id] < s.cost + 1e-10) {
					continue;
				}
				used[s.p.id] = s.cost;

				for (E np : s.p.next) {
					double nc = 0;
					double dif = np.to.dif(s.p);
					if (np.type == 1) {
						nc = dif / vc;
					} else {
						nc = dif / vw;
					}

					nc += s.cost;
					if (used[np.to.id] < nc + 1e-10) {
						continue;
					}
					q.add(new S(np.to, nc));
				}
			}
			System.out.printf("%.7f\n",used[n-1]);
		}
	}

	/**
	 * @param args
	 */

	Main() {
		sc = new Scanner(System.in);
		try {
			sc = new Scanner(new File(""));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Main().run();
	}

}
