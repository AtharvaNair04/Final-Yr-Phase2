import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingDeque;

public class Main {
	static class Pair {
		int fst, snd;

		Pair(int fst, int snd) {
			this.fst = fst;
			this.snd = snd;
		}
	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		int N = s.nextInt();
		int M = s.nextInt();
		int[] bits = new int[N];
		int[] targets = new int[M];
		for (int i = 0; i < N; i++) {
			bits[i] = s.nextInt();
		}
		for (int i = 0; i < M; i++) {
			targets[i] = s.nextInt();
		}

		int initial = bitToNum(N, bits);
		Pair ans = getAnswers(targets);
		
		if (initial == ans.fst || initial == ans.snd) {
			System.out.println("0");
			return;
		}
		
		boolean[] reached = new boolean[1 << N];
		Arrays.fill(reached, false);

		Queue<Pair> q = new LinkedBlockingDeque<Pair>();
		q.add(new Pair(initial, 0));
		reached[initial] = true;
		while (!q.isEmpty()) {
			Pair pair = q.poll();
			for (int r = 0; r < N - 1; r++) {
				int l = r + 1;
				if (isSameBit(pair.fst, r, l)) {
					continue;
				}
				// System.out.println("l = " + l + " , r = "+ r);
				// System.out.println("lmsk = " + (pair.fst & (1 << l)) + " , rm = "+ (pair.fst & (1 << r)));
				int mask = (1 << l) + (1 << r);
				int value = pair.fst ^ mask;
				// System.out.printf("from: %d, to: %d mask: %d%n", pair.fst, value, mask);
				if (value == ans.fst || value == ans.snd) {
					System.out.println(pair.snd + 1);
					return;
				}
				if (!reached[value]) {
					reached[value] = true;
					// System.out.println(":- " + value + ", " + pair.snd);
					q.add(new Pair(value, pair.snd + 1));
				}

			}
		}
	}

	private static boolean isSameBit(int value, int r, int l) {
		int left = value & (1 << l);
		int right = value & (1 << r);
		return (left == 0 && right == 0) || (left > 0 && right > 0);
	}

	private static Pair getAnswers(int[] targets) {
		List<Integer> xs = new ArrayList<Integer>();
		int bit = 0;
		for (int i = 0; i < targets.length; i++) {
			for (int j = 0; j < targets[i]; j++) {
				xs.add(bit);
			}
			bit = (bit == 0) ? 1 : 0;
		}
		List<Integer> ys = new ArrayList<Integer>();
		bit = 1;
		for (int i = 0; i < targets.length; i++) {
			for (int j = 0; j < targets[i]; j++) {
				ys.add(bit);
			}
			bit = (bit == 0) ? 1 : 0;
		}

		return new Pair(bitToNum(xs), bitToNum(ys));
	}

	private static int bitToNum(List<Integer> xs) {
		int N = xs.size();
		int[] bits = new int[N];
		for (int i = 0; i < N; i++) {
			bits[i] = xs.get(i);
		}
		return bitToNum(N, bits);
	}

	private static int bitToNum(int N, int[] bits) {
		int initial = 0;
		for (int i = 0; i < N; i++) {
			initial += (bits[N - i - 1] << i);
		}
		return initial;
	}
}
