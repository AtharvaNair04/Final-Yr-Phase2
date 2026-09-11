import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main{
	public static void main(String[] args) {
		new Main().run();
	}

	void run() {
		// long cur = System.currentTimeMillis();
		solver();
		// System.out.println(System.currentTimeMillis() - cur + "ms");
	}

	int N;

	@SuppressWarnings("rawtypes")
	void solver() {
		Scanner sc = new Scanner(System.in);
		N = sc.nextInt();
		ans = N - 1;
		int[] A = new int[N];
		for (int i = 0; i < N; i++) {
			A[i] = sc.nextInt() - 1;
		}
		pow10[0] = 1;
		for (int i = 1; i <= 10; i++) {
			pow10[i] = 10L * pow10[i - 1];
		}
		factorial[0] = 1;
		for (int i = 1; i <= 10; i++) {
			factorial[i] = factorial[i - 1] *(long) i;
		}

		Arrays.fill(memo, -1);
		enumerate(A);
		Arrays.sort(A);
		second = true;
		enumerate(A).toArray(new Long[0]);
		System.out.println(ans);
	}

	int ans;
	boolean second = false;

	long[] pow10 = new long[11];
	int[] memo = new int[3628800];
	long[] factorial = new long[11];

	ArrayList<Long> enumerate(int[] A) {
		long a = to_long(A);
		ArrayDeque<Long> cur_que = new ArrayDeque<>();
		cur_que.add(a);
		ArrayDeque<Long> next_que = new ArrayDeque<>();
		ArrayList<Long> ret = new ArrayList<>();
		ret.add(a);
		int index = numbering(to_array(a));
		if (memo[index] != -1) {
			ans = Math.min(memo[index], ans);
		}

		memo[index] = 0 + (second ? 10 : 0);
		for (int i = 1; i <= N / 2 - ((N % 2 == 0) ? 1 : 0); i++) {
			while (!cur_que.isEmpty()) {
				long cur = cur_que.poll();
				for (int j = 0; j < N; j++) {
					for (int k = j + 1; k < N; k++) {
						int[] tmp = to_array(cur);
						int s = j, t = k;
						while (s < t) {
							int dummy = tmp[s];
							tmp[s] = tmp[t];
							tmp[t] = dummy;
							s++;
							t--;
						}
						long dummy = to_long(tmp);
						int idx = numbering(tmp);
						if (memo[idx] == -1) {
							next_que.add(dummy);
							memo[idx] = i + (second ? 100 : 0);
						} else if (memo[idx] < 100 && second) {
							ans = Math.min(memo[idx] + i, ans);
						}
					}
				}
			}
			ret.addAll(next_que);
			cur_que.addAll(next_que);
			next_que.clear();
		}
		return ret;
	}

	int numbering(int[] permutation) {
		int ret = 0;
		for (int i = 0; i < permutation.length; i++) {
			int count = 0;
			for (int j = i - 1; j >= 0; j--) {
				if (permutation[i] > permutation[j])
					count++;
			}
			ret += factorial[i] * count;
		}
		return ret;
	}

	long to_long(int[] array) {
		long ret = 0;
		for (int i = 0; i < array.length; i++) {
			ret += pow10[array.length - 1 - i] * array[i];
		}
		return ret;
	}

	int[] to_array(long a) {
		int[] ret = new int[N];
		int pos = N - 1;
		while (a > 0) {
			ret[pos--] = (int) (a % 10);
			a /= 10;
		}
		return ret;
	}

	void tr(Object... o) {
		System.out.println(Arrays.deepToString(o));
	}
}
