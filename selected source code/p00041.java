import java.util.Scanner;

public class Main {
	public static String f(int a, int b, int c, int d, int k) {
		String str;

		if ((str = check(a, b, c, d, k)) != null) {
			return str;
		}
		if ((str = check(b, c, d, a, k)) != null) {
			return str;
		}
		if ((str = check(c, d, a, b, k)) != null) {
			return str;
		}
		if ((str = check(d, a, b, c, k)) != null) {
			return str;
		}

		if ((str = g(a, b, c, d, k)) != null) {
			return str;
		}
		if ((str = g(a, c, b, d, k)) != null) {
			return str;
		}
		if ((str = g(a, d, b, c, k)) != null) {
			return str;
		}

		return "0";
	}

	public static String f(int a, int b, int c, int k) {
		String str;

		if ((str = check(a, b, c, k)) != null) {
			return str;
		}
		if ((str = check(b, c, a, k)) != null) {
			return str;
		}
		if ((str = check(c, a, b, k)) != null) {
			return str;
		}
		return null;
	}

	public static String g(int a, int b, int c, int d, int k) {
		String str1, str2;

		for (int i = -100; i <= 100; i++) {
			if ((str1 = check(a, b, i)) != null && (str2 = check(c, d, 10 - i)) != null) {
				return "(" + str1 + " + " + str2 + ")";
			}
			if ((str1 = check(a, b, i)) != null && (str2 = check(c, d, i - 10)) != null) {
				return "(" + str1 + " - " + str2 + ")";
			}
			if ((str1 = check(a, b, i)) != null && (str2 = check(c, d, 10 + i)) != null) {
				return "(" + str2 + " - " + str1 + ")";
			}
			if (i != 0 && 10 % i == 0 && (str1 = check(a, b, i)) != null && (str2 = check(c, d, 10 / i)) != null) {
				return "(" + str1 + " * " + str2 + ")";
			}
		}

		return null;
	}

	public static String check(int a, int b, int c, int d, int k) {
		String str;

		if ((str = f(a, b, c, k - d)) != null) {
			return "(" + str + " + " + Integer.toString(d) + ")";
		}
		if ((str = f(a, b, c, k + d)) != null) {
			return "(" + str + " - " + Integer.toString(d) + ")";
		}
		if ((str = f(a, b, c, d - k))!= null) {
			return "(" + Integer.toString(d) + " - " + str + ")";
		}
		if (k % d == 0 && (str = f(a, b, c, k / d)) != null) {
			return "(" + str + " * " + Integer.toString(d) + ")";
		}
		return null;
	}

	public static String check(int a, int b, int c, int k) {
		String str;

		if ((str = check(a, b, k - c)) != null) {
			return  "(" + str + " + " + Integer.toString(c) + ")";
		}
		if ((str = check(a, b, k + c)) != null) {
			return "(" + str + " - " + Integer.toString(c) + ")";
		}
		if ((str = check(a, b, c - k)) != null) {
			return "(" + Integer.toString(c) + " - " + str + ")";
		}
		if (k % c == 0 && (str = check(a, b, k / c)) != null) {
			return "(" + str + " * " + Integer.toString(c) + ")";
		}

		return null;
	}

	public static String check(int a, int b, int k) {
		if (a + b == k) {
			return "(" + Integer.toString(a) + " + " + Integer.toString(b) + ")";
		}
		if (a - b == k) {
			return "(" + Integer.toString(a) + " - " + Integer.toString(b) + ")";
		}
		if (b - a == k) {
			return "(" + Integer.toString(b) + " - " + Integer.toString(a) + ")";
		}
		if (a * b == k) {
			return "(" + Integer.toString(a) + " * " + Integer.toString(b) + ")";
		}

		return null;
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int a, b, c, d;

		while (true) {
			a = sc.nextInt();
			b = sc.nextInt();
			c = sc.nextInt();
			d = sc.nextInt();
			if((a | b | c | d) == 0) {
				break;
			}
			System.out.println(f(a, b, c, d, 10));
		}
	}
}
