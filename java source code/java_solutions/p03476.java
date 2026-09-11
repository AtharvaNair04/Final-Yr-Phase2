import java.util.*;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		/**
		 * エラトステネスの篩
		 */
		int MAX = 101010;
		boolean[] isPrime = new boolean[MAX];
		Arrays.fill(isPrime, true);
		isPrime[0] = false;
		isPrime[1] = false;

		for (int i = 2; i <= Math.sqrt(MAX); i++) {
			if (!isPrime[i]) { // 素数でない場合は見ない
				continue;
			}
			for (int j = i * 2; j < MAX; j += i) { // 倍数を除く
				isPrime[j] = false;
			}
		}

		// 2017-like number
		int[] isLike = new int[MAX];
		for (int i = 3; i < MAX; i = i + 2) {
			if (isPrime[i] && isPrime[(i + 1) / 2]) {
				isLike[i] = 1;
			}
		}

		// 累積和
		int[] cs = new int[MAX + 1];
		for (int i = 1; i < MAX; i++) {
			cs[i] = cs[i - 1] + isLike[i];
		}

		int Q = sc.nextInt();
		int[] numLikeCnt = new int[Q];

		for (int i = 0; i < Q; i++) {
			int l = sc.nextInt();
			int r = sc.nextInt();
			numLikeCnt[i] = cs[r] - cs[l - 1];
		}

		for (int c : numLikeCnt) {
			System.out.println(c);
		}

		sc.close();
	}

}
