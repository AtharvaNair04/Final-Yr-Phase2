import java.math.BigInteger;
import java.util.Scanner;

public class Main {
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		BigInteger big, big100000;
		long n;
		int count;

		n = sc.nextLong();
		while (n != 0) {
			count = 0;
			big = new BigInteger("1");
			big100000 = new BigInteger("100000");
			for (int i = 2; i <= n; i++) {
				big = big.multiply(new BigInteger(Integer.toString(i)));
			}
			
			while (big.remainder(big100000).equals(BigInteger.ZERO)) {
				big = big.divide(big100000);
				count += 5;
			}
			while (big.remainder(BigInteger.TEN).equals(BigInteger.ZERO)) {
				big = big.divide(BigInteger.TEN);
				count++;
			}

			System.out.println(count);
			n = sc.nextLong();
		}
	}
}
