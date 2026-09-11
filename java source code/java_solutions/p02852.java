import java.util.*;
import java.util.stream.Collectors;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int m = sc.nextInt();
		sc.nextLine();
		String s = sc.nextLine();
		int[] dp = new int[n + 1];
		Arrays.fill(dp, Integer.MAX_VALUE);
		dp[n] = 0;

		Queue<Integer> q = new LinkedList<>();
		q.offer(0);
		for (int i = n - 1; i >= 0; i--) {
			while (true) {
				if (q.size() == 0) {
					System.out.println(-1);
					return;
				}
				if (q.peek() != Integer.MAX_VALUE && q.size() <= m) break;
				q.poll();
			}

			if (s.charAt(i) == '0') dp[i] = q.peek() + 1;
			q.offer(dp[i]);
		}

		List<Integer> answeer = new ArrayList<>();
		int x = 0;
		int rest = dp[0];
		while (x < n) {
			--rest;
			int i = 1;
			while (dp[x + i] != rest) ++i;
			answeer.add(i);
			x += i;
		}
		for (int i = 0; i < answeer.size(); i++) {
			System.out.print(answeer.get(i));
			System.out.print(" ");
		}
		System.out.println();
	}
}
