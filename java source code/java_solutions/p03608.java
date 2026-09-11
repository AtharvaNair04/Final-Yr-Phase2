import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.function.LongSupplier;
import java.util.function.ToLongFunction;

public class Main{ // 
	static class ConsoleScanner {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer("");

		String next() {
			try {
				while (!st.hasMoreElements())
					st = new StringTokenizer(br.readLine());
				return st.nextToken();
			} catch (IOException e) {
				throw new AssertionError(e);
			}
		}

		int nextInt() {
			return Integer.parseInt(next());
		}

		long nextLong() {
			return Long.parseLong(next());
		}
	}

	static LongSupplier now = new LongSupplier() {
		private long t = System.currentTimeMillis();

		@Override
		public long getAsLong() {
			long a = t;
			t = System.currentTimeMillis();
			long ans = t - a;
			return ans;
		}
	};

	public static void main(String args[]) {
		ConsoleScanner cin = new ConsoleScanner();
		PrintWriter cout = new PrintWriter(System.out);
		solve(cin, cout);
		cout.flush();
	}

	static int tr(Object... objects) {
		System.out.println("tr:" + Arrays.deepToString(objects));
		return 0;
	}

	private static void solve(ConsoleScanner cin, PrintWriter cout) {
		// assert solve(1) == 1;

		int n = cin.nextInt();
		int m = cin.nextInt();
		int r = cin.nextInt();

		int[] s = new int[r];
		for (int i = 0; i < s.length; i++)
			s[i] = cin.nextInt() - 1;
		long[][] dist = new long[n][n];
		for (long[] d : dist)
			Arrays.fill(d, Long.MAX_VALUE / 2);
		for (int i = 0; i < n; i++)
			dist[i][i] = 0;
		for (int i = 0; i < m; i++) {
			int a = cin.nextInt() - 1;
			int b = cin.nextInt() - 1;
			int c = cin.nextInt();
			dist[a][b] = dist[b][a] = c;
		}

		now.getAsLong();

		long ans = solve(n, r, s, dist);
		cout.println(ans);

		assert 0 == tr("elapsed", now.getAsLong());
	}

	private static long solve(int n, int r, int[] s, long[][] d) {

		assert 0 == tr(d);
		// Warshall–Floyd Algorithm
		for (int k = 0; k < n; k++)
			for (int i = 0; i < n; i++)
				for (int j = 0; j < n; j++)
					if (d[i][j] > d[i][k] + d[k][j])
						d[i][j] = d[i][k] + d[k][j];

		assert 0 == tr(d);
		
		// 2<=R<=min(8,N)
		ToLongFunction<Integer> minimize = new ToLongFunction<Integer>() {
			@Override
			public long applyAsLong(Integer k) {
				if (k == n - 1) {
					// evaluate
					long ans = 0;
					for (int i = 1; i < r; i++)
						ans += d[s[i - 1]][s[i]];
					return ans;
				}
				// permutate
				long ans = Long.MAX_VALUE;
				for (int i = k; i < r; i++) {
					int t = s[i];
					s[i] = s[k];
					s[k] = t;
					long subans = this.applyAsLong(k + 1);
					ans = Math.min(ans, subans);
					t = s[i];
					s[i] = s[k];
					s[k] = t;
				}
				return ans;
			}

		};
//		long ans = minimize.applyAsLong(0);
//		return ans;
		
		long min = dfs(0, new boolean[r], s, d, -1);
        return min;
	}
	
	
	 
    private static long dfs(int index, boolean[] used, int[] r, long[][] distances, int from) {
        if (index >= r.length) {
            return 0;
        }

        long min = (int) 1e9;

        for (int i = 0; i < r.length; i++) {
            if (used[i]) {
                continue;
            }

            used[i] = true;

            int to = r[i];
            long distance = (from == -1 ? 0 : distances[from][to]) + dfs(index + 1, used, r, distances, to);
            if (distance < min) {
                min = distance;
            }

            used[i] = false;

        }

        return min;
    }

}
