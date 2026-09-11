import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
	Scanner sc;

	class P implements Comparable<P> {
		double x;
		double y;

		double r;
		double theta;

		P(double xx, double yy) {
			x = xx;
			y = yy;
		}

		void org(P t) {
			x -= t.x;
			y -= t.y;
		}

		void st() {
			r = Math.sqrt(x * x + y * y);
			theta = Math.atan2(y, x);
			rot(0);
		}

		P sum(P t) {
			return new P(x + t.x, y + t.y);
		}

		P div(double v) {
			return new P(x / v, y / v);
		}

		public int compareTo(P t) {
			if (Math.abs(theta - t.theta) < 1e-11) {
				if ((r - t.r) < 0) {
					return -1;

				}
				return 1;

			}
			if (theta - t.theta > 0) {
				return 1;
			}
			return -1;
		}

		void rot(double theta) {
			this.theta += theta;
			while (this.theta > Math.PI * 2) {
				this.theta -= Math.PI * 2;
			}
			while (this.theta < 0) {
				this.theta += Math.PI * 2;
			}
		}
	}

	class Poly {
		P[] E;
		int n;

		Poly(int n, Scanner sc) {
			E = new P[n];
			this.n = n;
			for (int i = 0; i < n; i++) {
				E[i] = new P(sc.nextDouble(), sc.nextDouble());
			}
		}

		void set() {
			P sum = new P(0, 0);
			for (P p : E) {
				sum = sum.sum(p);
			}
			P org = sum.div(n);

			for (P p : E) {
				p.org(org);
				p.st();
			}

			Arrays.sort(E);
		}

		boolean check(double theta, Poly target) {
			for (P p : E) {
				p.rot(theta);
			}
			Arrays.sort(E);

			boolean ok = true;
			Poly a = target;
			Poly b = this;
			for (int i = 0; i < n; i++) {
	//			System.out.println(a.E[i].r + " " + a.E[i].theta);
	//			System.out.println(b.E[i].r + " " + b.E[i].theta);
			}
			for (int i = 0; i < n; i++) {
				if (Math.abs(E[i].r - target.E[i].r) > 1e-9 ) {
					ok = false;
					break;
				}
				if (Math.abs(E[i].theta - target.E[i].theta) > 1e-9 && Math.abs(Math.abs(E[i].theta - target.E[i].theta ) - Math.PI*2) > 1e-9) {
					ok = false;
					break;
				}
			}

			for (P p : E) {
				p.rot(-theta);
			}
			Arrays.sort(E);
			return ok;
		}
	}

	void run() {
		for (;;) {
			int n = sc.nextInt();
			if (n == 0) {
				break;
			}
			Poly a = new Poly(n, sc);
			Poly b = new Poly(n, sc);
			a.set();
			b.set();

			double t1 = 100;
			for (int i = 0; i < n; i++) {
				double t = -a.E[0].theta + b.E[i].theta;
				if (b.check(-t, a)) {
					
					while (t > Math.PI*2 ) {
						t -= Math.PI*2;
					}
					while (t < 0) {
						t += Math.PI*2 ;
					}

					t = Math.min(2*Math.PI-t, t);
					
					t1 = Math.min(t,t1);
				}
			}
			System.out.printf("%.8f\n",t1);
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
