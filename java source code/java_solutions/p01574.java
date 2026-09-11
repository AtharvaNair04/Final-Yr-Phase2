import java.util.*;
import java.lang.*;
import java.math.*;
import java.io.*;
import static java.lang.Math.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;

public class Main {
	Scanner sc = new Scanner(System.in);

	int INF = 1 << 28;
	double EPS = 1e-9;

	int n, m;
	int[] a;

	void run() {
		n = sc.nextInt();
		m = sc.nextInt();
		a = new int[m];
		for (int i = 0; i < m; i++) {
			a[i] = sc.nextInt();
		}
		solve();
	}

	void solve() {
		int gcd = a[0];
		for (int i = 1; i < m; i++) {
			gcd = gcd(gcd, a[i]);
		}
		println(gcd(gcd, n) == 1 ? "Yes" : "No");
	}

	int gcd(int a, int b) {
		return a == 0 ? b : gcd(b % a, a);
	}

	void println(String s) {
		System.out.println(s);
	}

	void print(String s) {
		System.out.print(s);
	}

	void debug(Object... os) {
		System.err.println(deepToString(os));
	}

	public static void main(String[] args) {
		new Main().run();
	}
}
