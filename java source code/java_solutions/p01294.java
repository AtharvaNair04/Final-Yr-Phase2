import java.util.Scanner;

public class Main {
	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		while (true) {
			int N = sc.nextInt();
			int D = sc.nextInt();
			if (N == 0) break;
			int sm = 0;
			Country[] cs = new Country[N];
			int mp = 0;
			int mc = 1;
			for (int i = 0; i < N; ++i) {
				cs[i] = new Country();
				sm += cs[i].m.length;
				if (cs[i].p > mp) {
					mp = cs[i].p;
					mc = 1;
				} else if (cs[i].p == mp) {
					++mc;
				}
			}
			boolean ok = true;
			for (int i = 0; i < sm; ++i) {
				int ci = -1;
				for (int j = 0; j < N && ci == -1; ++j) {
					Country c = cs[j];
					if (c.pos == -1) continue;
					if (c.p == mp && mc == 1) {
						int nmp = 0;
						for (int k = 0; k < N; ++k) {
							if (k == j) continue;
							nmp = Math.max(nmp, cs[k].p);
						}
						if (c.p - c.m[c.pos] >= nmp - D) {
							ci = j;
						}
					} else {
						if (c.p - c.m[c.pos] >= mp - D) {
							ci = j;
						}
					}
				}
				if (ci == -1) {
					ok = false;
					break;
				}
				cs[ci].p -= cs[ci].m[cs[ci].pos];
				cs[ci].pos--;
				mp = 0;
				mc = 0;
				for (int j = 0; j < N; ++j) {
					if (cs[j].p > mp) {
						mp = cs[j].p;
						mc = 1;
					} else if (cs[j].p == mp) {
						++mc;
					}
				}
			}
			System.out.println(ok ? "Yes" : "No");
		}
	}

	static class Country {
		int[] m;
		int p, pos;

		Country() {
			int s = sc.nextInt();
			m = new int[s];
			for (int i = 0; i < s; ++i) {
				m[i] = sc.nextInt();
				p += m[i];
			}
			pos = m.length - 1;
		}
	}

}
