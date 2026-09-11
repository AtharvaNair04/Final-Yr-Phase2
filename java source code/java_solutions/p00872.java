import java.util.*;
import java.lang.*;
import java.math.*;

public class Main {
	Scanner sc = new Scanner(System.in);

	class primary {
		boolean pr;
		E ch;
		int value;
		boolean x;

		int[] eval() {
			if (pr) {
				return ch.eval();
			} else {
				if (x) {
					return new int[] { 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
				}
//				System.out.println(value);
				return new int[] { value, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			}
		}

		primary(String buffer) {
			System.err.println("p " + buffer);

			if (buffer.contains("(")) {
				pr = true;
				ch = new E(buffer.substring(1, buffer.length() - 1));
			} else {
				pr = false;

				if (buffer.equals("x")) {
					x = true;
				} else {
					x = false;
					value = 1;
					if (buffer.startsWith("+")) {
						buffer = buffer.substring(1);
						value = 1;
					}
					if (buffer.startsWith("-")) {
						buffer = buffer.substring(1);
						value = -1;
					}
					if (buffer.length() == 0) {
						value *= 1;
					} else {
						value *= Integer.valueOf(buffer);
//						System.out.println(buffer+" :: "+value);
					}
				}
			}

		}
	}

	class factor {
		primary p;
		int deg;

		int[] eval() {
			int[] ans = new int[12];

			int[] a = null;
			for (int i = 0; i < deg; i++) {
				a = mul(a, p.eval());
			}
//			System.err.println(Arrays.toString(a));
			for (int i = 0; i < a.length && i < ans.length; i++) {
				ans[i] = a[i];
			}

			return ans;
		}

		factor(String buffer) {
			System.err.println("f " + buffer);

			int depth = 0;
			String a = "";
			int i = 0;
			for (i = 0; i < buffer.length(); i++) {
				if (buffer.charAt(i) == '(') {
					depth++;
				}
				if (buffer.charAt(i) == ')') {
					depth--;
				}

				if (depth == 0) {
					if (buffer.charAt(i) == '^') {
						a = buffer.substring(0, i);
						break;
					}
				}
			}
			if (i == buffer.length()) {
				this.deg = 1;
				a = buffer;
			} else {
				this.deg = Integer.valueOf(buffer.substring(i + 1,
						buffer.length()));
			}

			p = new primary(a);
		}
	}

	int[] shift(int[] a, int s) {
		int ans[] = new int[12];
		for (int i = 0; i + s < 12; i++) {
			ans[i + s] = a[i];
		}
		return ans;
	}

	int[] mul(int[] a, int b) {
		int ans[] = new int[12];
		for (int i = 0; i < a.length; i++) {
			ans[i] = a[i] * b;
		}
		return ans;
	}

	int[] add(int[] a, int[] b) {
		int[] ans = new int[12];
		for (int i = 0; i < a.length && i < b.length && i < 12; i++) {
			ans[i] = a[i] + b[i];
		}
		return ans;
	}

	int[] mul(int[] a, int[] b) { // TODO
		if (a == null) {
			return b;
		}
		if (b == null) {
			return a;
		}
		int ans[] = new int[12];
		for (int i = 0; i < a.length; i++) {
			ans = add(ans, shift(mul(a, b[i]), i));
		}

		return ans;
	}

	class term {
		LinkedList<factor> l = new LinkedList<factor>();

		int[] eval() {
			int ans[] = new int[12];
			int[] a = null;
			for (factor f : l) {
				a = mul(a, f.eval());
			}
			for (int i = 0; i < a.length && i < 12; i++) {
				ans[i] = a[i];
			}
			return ans;
		}

		term(String buffer) {
			 System.err.println("t " + buffer);

			int depth = 0;

			String z = "";
			for (int i = 0; i < buffer.length(); i++) {
				if (depth == 0) {
					if ((buffer.charAt(i)) == 'x' || buffer.charAt(i) == '(') {
						if (!z.equals("")) {
							factor f = new factor(z);
							l.add(f);
						}
						z = "";
					}
				}
				if (buffer.charAt(i) == '(') {
					depth++;
				}
				if (buffer.charAt(i) == ')') {
					depth--;
				}
				z += buffer.charAt(i);
			}
			if (!z.equals("")) {
				factor f = new factor(z);
				l.add(f);
			}
		}
	}

	class E {
		LinkedList<term> m = new LinkedList<term>();

		int[] eval() {
			int ans[] = new int[12];

			for (term t : m) {
				int[] v = t.eval();
				for (int i = 0; i < 12; i++) {
					ans[i] += v[i];
				}
			}
			return ans;
		}

		E(String buffer) {
			System.err.println("e " + buffer);

			int depth = 0;

			String z = "";
			for (int i = 0; i < buffer.length(); i++) {
				if (depth == 0) {
					if ((buffer.charAt(i)) == '+' || buffer.charAt(i) == '-') {
						if (!z.equals("")) {
							term t = new term(z);
							m.add(t);
						}
						z = "";
					}
				}
				if (buffer.charAt(i) == '(') {
					depth++;
				}
				if (buffer.charAt(i) == ')') {
					depth--;
				}
				z += buffer.charAt(i);
			}
			if (!z.equals("")) {
				term t = new term(z);
				m.add(t);
			}
		}
	}

	int[] GCD(int[] a, int[] b) {
		int ai = 0;
		int bi = 0;
		int[] c = b.clone();

//		 System.out.println(Arrays.toString(a));
//		 System.out.println(Arrays.toString(b));
//		 System.out.println("----");
		for (int i = 0; i < 12; i++) {
			if (a[i] != 0) {
				ai = i;
			}
			if (b[i] != 0) {
				bi = i;
			}
		}

		if (ai == 0) {
			return new int[] { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		}
		if (bi == 0) {
			return new int[] { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		}

		if (bi > ai) {
			c = a.clone();
			a = shift(a, bi - ai);
			ai = bi;
		}

		if (bi < ai) {
			b = shift(b, ai - bi);
			bi = ai;
		}
		// System.out.println(a[ai] + " : "+b[bi]);
		boolean sign = true;
		int g = 0;
		if(a[ai] < 0){
			a = mul(a,-1);
		}
		if(b[bi] < 0){
			b = mul(b,-1);
		}
		g = GCD(a[ai], b[bi]);
		int am = b[bi] / g;
		int bm = a[bi] / g;

		int[] diff = add(mul(a, am), mul(b, -bm));
		boolean ok = true;
		for (int i = 0; i < 12; i++) {
			if (diff[i] != 0) {
				ok = false;
				break;
			}
		}
		if (ok) {
			return c;
		}
		return GCD(c, diff);
	}

	int GCD(int a, int b) {
		if (a == 0) {
			return b;
		}
		if (b == 0) {
			return a;
		}
		if (a == b) {
			return a;
		} else {
			return GCD(Math.min(a, b), Math.max(a, b) % Math.min(a, b));
		}
	}

	String f(int[] a) {
		int g = a[0];
		int z = 0;
		for (int i = 1; i < a.length; i++) {
			if (g * a[i] < 0) {
				g =- GCD(Math.abs(g),Math.abs( a[i]));
			}else{
				g = GCD(Math.abs(g),Math.abs(a[i]));
			}
			if(a[i]!=0){
				z = i;
			}
		}

		if(a[z] < 0){
			g = -g;
		}
		for (int i = 0; i < a.length; i++) {
			a[i] /= g;
		}

		String ans = "";
		for (int i = 0; i < a.length; i++) {
			String t = "x^" + i;
			if(i == 1){
				t = "x";
			}
			if (a[i] == 0) {
				continue;
			}
			if(a[i] == -1){
				t = "-" + t;
			}else if (a[i] != 1) {
				t = a[i] + t;
			}
			if (i == 0) {
				t = "" + a[i];
			}
			if (a[i] > 0) {
				t = "+" + t;
			}
			ans = t + ans;
		}
		if (ans.startsWith("+")) {
			ans = ans.substring(1);
		}
		return ans;
	}

	void run() {
		for (;;) {
			String buffer = sc.nextLine();

			if (buffer.equals(".")) {
				break;
			}

			E E1 = new E(buffer);
			int[] e1v = E1.eval();
//			System.out.println(Arrays.toString(e1v));
			E E2 = new E(sc.nextLine());
			int[] e2v = E2.eval();
			System.out.println(f(GCD(e1v, e2v)));

		}

	}

	public static void main(String[] args) {
		Main m = new Main();
		System.err.close();
		m.run();
	}
}
