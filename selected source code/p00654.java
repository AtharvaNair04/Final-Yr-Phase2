import java.math.BigInteger;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n;
		long odd[], even[];
		int oid, eid;

		while (true) {
			n = sc.nextInt();
			if (n == 0) {
				break;
			}

			even = new long[n];
			odd = new long[n*(n+1)/2 - n];
			oid = eid = 0;
			for (int i = 0; i < n*(n+1)/2; i++) {
				long k = sc.nextLong();
				if (k % 2 == 0) {
					even[eid++] = k;
				} else {
					odd[oid++] = k;
				}
			}
			Arrays.sort(odd);
			Arrays.sort(even);
			
			BigInteger e0 = new BigInteger(Long.toString(even[0]));
			BigInteger e1 = new BigInteger(Long.toString(even[1]));
			BigInteger o0 = new BigInteger(Long.toString(odd[0]));
			BigInteger e2 = (e0.multiply(e1)).divide(o0);
			long e = (long)Math.sqrt(e2.longValue());
			System.out.println(e);
			String s = "";
			for (int i = 0; i < n; i++) {
				System.out.print(s + even[i] / e);
				s = " ";
			}
			System.out.println();
		}
	}
}
