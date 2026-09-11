import java.math.BigInteger;
import java.util.Arrays;
import java.util.Scanner;


public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int N = sc.nextInt(), M = sc.nextInt(), R = sc.nextInt();
		R = R - N * M;
		M = 0;
		if (R <= 0) {
			System.out.println(0);
			return;
		}

		BigInteger L = new BigInteger("1");
		BigInteger Rp = new BigInteger("1"),
					Np = new BigInteger("1"),
					NRp = new BigInteger("1");
		for (int i = 1 ; i < N+R ; i++) {
			L = L.multiply(new BigInteger(String.valueOf(i)));
			if (i == R) Rp = L;
			else if (i == N-1) Np = L;
			else if (i == N+R-1) NRp = L;
		}
		System.out.println(NRp.divide(Rp.multiply(Np)));
	}
}
