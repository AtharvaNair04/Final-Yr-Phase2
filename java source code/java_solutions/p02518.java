import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	static Scanner sc = new Scanner(System.in);
	static int N;
	static final int MOD = 100000007;
	static long[] pows = new long[101];

	public static void main(String[] args) throws Exception {
		for (int i = 1; i <= 100; ++i) {
			pows[i] = pow(i, MOD - 2);
		}
		while (true) {
			N = sc.nextInt();
			if (N == 0) return;
			Rational[] p = new Rational[N];
			int[] id = new int[N];
			Rational[] w = new Rational[N];
			for (int i = 0; i < N; ++i) {
				p[i] = strToRational(sc.next());
				id[i] = sc.nextInt();
				w[i] = strToRational(sc.next());
			}
			boolean[] visit = new boolean[N];
			long mul = 1;
			ArrayList<Integer> counts = new ArrayList<Integer>();
			Rational sum = new Rational(1, 0);
			for (int i = 0; i < N; ++i) {
				if (visit[i]) continue;
				int cur = i;
				ArrayList<Integer> circle = new ArrayList<Integer>();
				while (!visit[cur]) {
					visit[cur] = true;
					circle.add(cur);
					cur = id[cur];
				}
				Rational min = null;
				int dup = 0;
				Rational all = new Rational(1, 0);
				for (int j = 0; j < circle.size(); ++j) {
					all = all.add(w[circle.get(j)]);
				}
				for (int j = 0; j < circle.size(); ++j) {
					Rational time = all.add(p[circle.get(j)]).sub(w[circle.get(j)]);
					if (min == null || min.compareTo(time) > 0) {
						min = time;
						dup = 1;
					} else if (min.compareTo(time) == 0) {
						dup++;
					}
				}
				mul *= dup;
				mul %= MOD;
				sum = sum.add(min);
				counts.add(circle.size());
			}
			long ans = polycomb(counts);
			ans *= mul;
			ans %= MOD;
			System.out.print(new BigDecimal(sum.num.doubleValue() / sum.den.doubleValue()).toPlainString());
			System.out.println(" " + ans);
		}
	}

	static long polycomb(ArrayList<Integer> counts) {
		long ret = 1;
		for (int i = 1; i <= N; ++i) {
			ret *= i;
			ret %= MOD;
		}
		for (int c : counts) {
			for (int i = 2; i <= c; ++i) {
				ret *= pows[i];
				ret %= MOD;
			}
		}
		return ret;
	}

	static long pow(long base, int p) {
		if (p == 0) return 1;
		if (p == 1) return base;
		long ret = pow(base, p / 2);
		ret *= ret;
		ret %= MOD;
		if (p % 2 != 0) {
			ret *= base;
			ret %= MOD;
		}
		return ret;
	}

	static Rational strToRational(String num) {
		int v = num.charAt(0) - '0';
		for (int i = 2; i < 5; ++i) {
			v *= 10;
			v += num.charAt(i) - '0';
		}
		return new Rational(v, 1000);
	}

	static class Rational {
		BigInteger den;
		BigInteger num;

		Rational(int den, int num) {
			int gcd = gcd(den, num);
			this.den = BigInteger.valueOf(den / gcd);
			this.num = BigInteger.valueOf(num / gcd);
		}

		Rational(BigInteger den, BigInteger num) {
			BigInteger gcd = den.gcd(num);
			this.den = den.divide(gcd);
			this.num = num.divide(gcd);
		}

		Rational add(Rational o) {
			BigInteger nden = this.den.multiply(o.den);
			BigInteger nnum = this.num.multiply(o.den).add(o.num.multiply(this.den));
			return new Rational(nden, nnum);
		}

		Rational sub(Rational o) {
			BigInteger nden = this.den.multiply(o.den);
			BigInteger nnum = this.num.multiply(o.den).subtract(o.num.multiply(this.den));
			return new Rational(nden, nnum);
		}

		Rational mul(Rational o) {
			BigInteger nden = this.den.multiply(o.den);
			BigInteger nnum = this.num.multiply(o.num);
			return new Rational(nden, nnum);
		}

		public boolean equals(Rational o) {
			return o.num.equals(this.num) && o.den.equals(this.den);
		}

		int compareTo(Rational o) {
			return this.num.multiply(o.den).compareTo(this.den.multiply(o.num));
		}

		public String toString() {
			return this.num.toString() + "/" + this.den.toString();
		}
	}

	static int gcd(int a, int b) {
		return b == 0 ? a : gcd(b, a % b);
	}

}
