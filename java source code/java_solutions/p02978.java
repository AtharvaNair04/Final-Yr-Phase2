import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
	
	public static void main(String[] args) throws IOException {
		MyScanner sc = new MyScanner();
		PrintWriter out = new PrintWriter(System.out);
		N = sc.nextInt() - 2;
		values = new long[N + 1];
		long ans = sc.nextLong();
		for (int i = 0; i < N; i++) {
			values[i] = sc.nextLong();
		}
		ans += sc.nextLong();
		
		next = new int[1 << N][N+1];
		for (int mask = 1; mask < 1 << N; mask++) {
			int prev = -1;
			for (int i = 0; i < N; i++) if (contains(mask, i)) {
				if (prev != -1) next[mask][prev] = i;
				else next[mask][N] = i;
				prev = i;
			}
			next[mask][prev] = N;
		}
		next[0][N] = N;
		
		recurse((1 << N) - 1, (1 << N) - 1, 0, 0, false, -1, 0);
		ans += best;
		out.println(ans);
		
		out.flush();
	}
	
	static int[][] next;
	static long best = Long.MAX_VALUE, sum = 0;
	static long[] values;
	static int N;
	
	static void recurse(int remainingMask, int availableMask, int myMask, int pos, boolean gotLast, int prev, int nextAvailableMask) {
		if (pos == N) {
			if (remainingMask != 0 && myMask == 0) return;
			int nextRemainingMask = remainingMask & ~myMask;
			if (gotLast) {
				sum += values[prev];
			}
			if (Integer.bitCount(nextRemainingMask) <= 3) {
				int tmp1 = next[nextRemainingMask][N];
				int tmp2 = next[nextRemainingMask][tmp1];
				int tmp3 = next[nextRemainingMask][tmp2];
				if (tmp3 == tmp1) {
					tmp1 = N;
				}
				if (values[tmp1] > values[tmp3]) {
					int tmp = tmp1;
					tmp1 = tmp3;
					tmp3 = tmp;
				}
				long last = 2 * (values[tmp1] + values[tmp2] + values[tmp3]) + values[tmp1] + Math.min(3 * values[tmp2], Math.min(values[tmp1] + values[tmp2], values[tmp3]));
				best = Math.min(best, sum + last);
			} else {
				recurse(nextRemainingMask, nextAvailableMask, 0, next[nextRemainingMask][N], false, -1, 0);
			}
			if (gotLast) {
				sum -= values[prev];
			}
			return;
		}
		int nPos = next[remainingMask][pos];
		if (!gotLast && contains(availableMask, pos)) {
			int nAvaMask = nextAvailableMask;
			if (prev == -1) {
				sum += values[pos];
			} else {
				values[prev] += values[pos];
				nAvaMask |= 1 << prev;
			}
			recurse(remainingMask, availableMask, myMask | (1 << pos), nPos, true, pos, nAvaMask);
			if (prev == -1) {
				sum -= values[pos];
			} else {
				values[prev] -= values[pos];
			}
		}
		int nAvaMask = nextAvailableMask;
		if (gotLast) {
			values[pos] += values[prev];
			nAvaMask |= 1 << pos;
		}
		recurse(remainingMask, availableMask, myMask, nPos, false, pos, nAvaMask);
		if (gotLast) values[pos] -= values[prev];
	}
	
	static boolean contains(int mask, int pos) {
		return (mask & (1 << pos)) != 0;
	}
	
	static class MyScanner {
		private BufferedReader br;
		private StringTokenizer tokenizer;
		
		public MyScanner() {
			br = new BufferedReader(new InputStreamReader(System.in));
		}
		
		public String next() {
			while (tokenizer == null || !tokenizer.hasMoreTokens()) {
				try {
					tokenizer = new StringTokenizer(br.readLine());
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			return tokenizer.nextToken();
		}
		
		public int nextInt() {
			return Integer.parseInt(next());
		}
		
		public long nextLong() {
			return Long.parseLong(next());
		}
	}
}
