import java.math.BigInteger;
import java.util.*;
public class Main {
	//模範解答の理解度確認
	static public HashMap<Long, BigInteger> memo = new HashMap<Long, BigInteger>();
	static public long a, b, c, d;

	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		// 文字列の入力
		String[] lineString = sc.nextLine().split(" ");
		int t = Integer.parseInt(lineString[0]);

		for (int test = 0; test < t; test++) {
			lineString = sc.nextLine().split(" ");
			memo.clear();
			a = Long.parseLong(lineString[1]);
			b = Long.parseLong(lineString[2]);
			c = Long.parseLong(lineString[3]);
			d = Long.parseLong(lineString[4]);
			System.out.println(solve(Long.parseLong(lineString[0])).toString());
		}

		// 出力
		sc.close();
	}

	static public BigInteger solve(long n) {
		if (n == 0) {
			return BigInteger.ZERO;
		} else if (n == 1) {
			return BigInteger.valueOf(d);
		} else if (memo.containsKey(n)) {
			return memo.get(n);
		} else {
			long l2 = n / 2;
			long r2 = l2 + 1;
			long l3 = n / 3;
			long r3 = l3 + 1;
			long l5 = n / 5;
			long r5 = l5 + 1;

			BigInteger result = BigInteger.valueOf(n).multiply(BigInteger.valueOf(d));

			result = result.min(BigInteger.valueOf(Math.abs(n - l2 * 2)).multiply(BigInteger.valueOf(d)).add(BigInteger.valueOf(a)).add(solve(l2)));
			if (n % 2 > 0) {
				result = result.min(BigInteger.valueOf(Math.abs(n - r2 * 2)).multiply(BigInteger.valueOf(d)).add(BigInteger.valueOf(a)).add(solve(r2)));
			}

			result = result.min(BigInteger.valueOf(Math.abs(n - l3 * 3)).multiply(BigInteger.valueOf(d)).add(BigInteger.valueOf(b)).add(solve(l3)));
			if (n % 3 > 0) {
				result = result.min(BigInteger.valueOf(Math.abs(n - r3 * 3)).multiply(BigInteger.valueOf(d)).add(BigInteger.valueOf(b)).add(solve(r3)));
			}

			result = result.min(BigInteger.valueOf(Math.abs(n - l5 * 5)).multiply(BigInteger.valueOf(d)).add(BigInteger.valueOf(c)).add(solve(l5)));
			if (n % 5 > 0) {
				result = result.min(BigInteger.valueOf(Math.abs(n - r5 * 5)).multiply(BigInteger.valueOf(d)).add(BigInteger.valueOf(c)).add(solve(r5)));
			}

			memo.put(n, result);
			return result;
		}
	}
}
