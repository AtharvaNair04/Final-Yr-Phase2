import java.util.Scanner;

public class Main {
	public static void main(String[] args) {

		Scanner stdIn = new Scanner(System.in);

		int n = stdIn.nextInt();
		int k = stdIn.nextInt();

		int[] numArray = new int[n + 1];
		for (int i = 0; i < numArray.length; i++) {
			numArray[i] = i;
		}

		long cnt = 0;

		for (int i = k; i <= n; i++) {
			long min = min(i);
			long max = max(i, n);

			cnt += max - min + 1;
		}

		//i=n+1のときは単純に+1
		cnt += 1;

		System.out.println(cnt % 1000000007L);
		stdIn.close();

	}


	private static long max(long k, long n) {

		long sum = n * (n+1) / 2;
		long sub = (n-k) * (n-k+1)/2;

		return sum - sub;
	}

	private static long min(long k) {

		return k * (k - 1) / 2;
	}

}
