import java.util.Scanner;

public class Main {
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		int n;
		long x, y, z;
		int a1, a2, a3;
		int m1, m2, m3;
		long countX, countY, countZ;

		while (true) {
			a1 = sc.nextInt();
			m1 = sc.nextInt();
			a2 = sc.nextInt();
			m2 = sc.nextInt();
			a3 = sc.nextInt();
			m3 = sc.nextInt();
			if ((a1 | a2 | a3 | m1 | m2 | m3) == 0) {
				break;
			}

			x = 1;
			y = 1;
			z = 1;
			countX = 0;
			countY = 0;
			countZ = 0;
			while (true) {
				x = (a1 * x) % m1;
				countX++;
				if (x == 1) {
					break;
				}
			}
			while (true) {
				y = (a2 * y) % m2;
				countY++;
				if (y == 1) {
					break;
				}
			}
			while (true) {
				z = (a3 * z) % m3;
				countZ++;
				if (z == 1) {
					break;
				}
			}
			long lcm1 = countX * countY / gcd(countX, countY);
			long lcm2 = lcm1 * countZ / gcd(countZ, lcm1);
			System.out.println(lcm2);
		}
	}

	public static long gcd(long x, long y) {
		if (y == 0) {
			return x;
		}
		long swap = x + y;
		x = Math.max(x, y);
		y = swap - x;
		return gcd(y, x % y);
	}
}
