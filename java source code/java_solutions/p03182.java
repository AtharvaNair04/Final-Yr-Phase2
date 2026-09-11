import java.util.*;

class Main {

	public static void main(String[] args) throws Exception {
		final Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int m = sc.nextInt();

		PriorityQueue<int[]> pq = new PriorityQueue<>(
				(a, b) -> Integer.compare(a[1], b[1]));

		SegmentTree st = new SegmentTree(n);
		int[][] xs = new int[m][3];
		for (int i = 0; i < m; i++) {
			xs[i][0] = sc.nextInt();
			xs[i][1] = sc.nextInt();
			xs[i][2] = sc.nextInt();
		}
		Arrays.sort(xs, (a, b) -> Integer.compare(a[0], b[0]));

		long answer = 0;
		for (int i = 1, p = 0; i <= n; i++) {
			while (p < m && i >= xs[p][0]) {
				st.add(0, xs[p][0], xs[p][2]);
				pq.offer(xs[p++]);
			}

			long v = st.query(0, i);
			st.add(i, i + 1, v);
			answer = Math.max(answer, v);

			while (!pq.isEmpty() && pq.peek()[1] <= i) {
				int[] u = pq.poll();
				st.add(0, u[0], -u[2]);
			}
		}

		System.out.println(answer);
	}

	static class SegmentTree {
		private static final long MIN = -1_000_000_000_000_000L;
		private final int n;
		private long[] data;
		private long[] add;

		public SegmentTree(int n) {
			int t = 1;
			while (t < n) t <<= 1;
			this.n = t;
			this.data = new long[t * 2 - 1];
			this.add = new long[t * 2 - 1];
		}

		// [l, r) の最大値
		public long query(int a, int b) {
			return query(a, b, 0, 0, n);
		}

		private long query(int a, int b, int k, int l, int r) {
			if (b <= l || r <= a) return MIN;
			if (a <= l && r <= b) return data[k];
			long vl = query(a, b, k * 2 + 1, l, (l + r) / 2);
			long vr = query(a, b, k * 2 + 2, (l + r) / 2, r);
			return Math.max(vl, vr) + add[k];
		}

		// [a, b) に v を追加する
		public void add(int a, int b, long v) {
			add(a, b, v, 0, 0, n);
		}

		private void add(int a, int b, long v, int k, int l, int r) {
			if (b <= l || r <= a) return;
			if (a <= l && r <= b) {
				add[k] += v;
				data[k] += v;
				return;
			}

			add(a, b, v, k * 2 + 1, l, (l + r) / 2);
			add(a, b, v, k * 2 + 2, (l + r) / 2, r);
			data[k] = Math.max(data[k * 2 + 1], data[k * 2 + 2]) + add[k];
		}
	}
}
