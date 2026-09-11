import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {

	static Scanner sc = new Scanner(System.in);
	static int N, K;
	static boolean[] ans;
	static char[] buf;
	static int ansC = 0;
	static Random rand = new Random();

	public static void main(String[] args) {
		N = sc.nextInt();
		K = sc.nextInt();
		if (N == K) {
			System.out.print("!1");
			for (int i = 2; i <= N; ++i) {
				System.out.print(" " + i);
			}
			System.out.println();
			System.out.flush();
			return;
		}
		ans = new boolean[N];
		buf = new char[N];
		while (ansC < K) {
			ArrayList<Integer> list = new ArrayList<Integer>();
			for (int i = 0; i < N; ++i) {
				if (!ans[i]) list.add(i);
			}
			solve(list, (K - ansC) % 2 == 0);
		}
		System.out.print("!");
		boolean first = true;
		for (int i = 0; i < N; ++i) {
			if (ans[i]) {
				if (!first) {
					System.out.print(" ");
				}
				System.out.print(i + 1);
				first = false;
			}
		}
		System.out.println();
		System.out.flush();
	}

	static void solve(ArrayList<Integer> list, boolean even) {
		if (ansC == K || list.isEmpty()) return;
		if (list.size() == 1) {
			ans[list.get(0)] = true;
			++ansC;
			return;
		}
		while (true) {
			ArrayList<Integer> one = new ArrayList<Integer>();
			ArrayList<Integer> zero = new ArrayList<Integer>();
			Arrays.fill(buf, '0');
			for (int i = 0; i < list.size(); ++i) {
				if (rand.nextBoolean()) {
					buf[list.get(i)] = '1';
					one.add(list.get(i));
				} else {
					zero.add(list.get(i));
				}
			}
			System.out.println("?" + String.valueOf(buf));
			System.out.flush();
			int res = sc.nextInt();
			if (res == 1) {
				solve(one, false);
				if (even) {
					solve(zero, false);
				}
				break;
			} else {
				if (!even) {
					solve(zero, false);
					break;
				}
			}
		}
	}

}
