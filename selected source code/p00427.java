import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.BitSet;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {

		new Main().run();
	}

	private void run() throws IOException {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			n = scanner.nextInt();
			k = scanner.nextInt();
			m = scanner.nextInt();
			r = scanner.nextInt();
			if ((n | k | m | r) == 0)
				break;
			if (m == 0)
				System.out.println(BigDecimal.ONE.divide(BigDecimal.valueOf(n),
						r, RoundingMode.DOWN));
			else {
				BitSet bs = new BitSet();
				BigInteger den = BigInteger.ONE;
				for (int i = 2; i <= n; i++) {
					if (!bs.get(i)) {
						long p;
						for (p = i; p <= n; p *= i)
							;
						den = den.multiply(BigInteger.valueOf(p / i));
						for (int j = i; j <= n; j += i)
							bs.set(j);
					}

				}
				BigInteger num = den;
				for (int i = 1; i <= n - 1; i++) {
					num = num.add(den.divide(BigInteger.valueOf(i)));
				}
				System.out.println(new BigDecimal(num).divide(new BigDecimal(
						den.multiply(BigInteger.valueOf(n))), r,
						RoundingMode.DOWN));
			}
		}
	}

	int n, k, m, r;
}
