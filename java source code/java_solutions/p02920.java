import java.util.*;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int size = 1 << n;

		int[] a = new int[size];
		for (int i = 0; i < size; i++) a[i] = sc.nextInt();
		Arrays.sort(a);

		int[] next = new int[size];
		next[size - 1] = a[size - 1];
		a[size - 1] = Integer.MAX_VALUE;

		for (int i = 1; i <= n; i++) {
			int x = size - 1;

			for (int j = 0, k = size - 1; j < 1 << (i - 1); j++, k--) {
				while (0 <= x && next[k] <= a[x]) x--;

				if (x == -1 && Arrays.stream(a).anyMatch((y) -> y != Integer.MAX_VALUE)) {
					System.out.println("No");
					return;
				}
				next[j] = a[x];
				a[x] = Integer.MAX_VALUE;
			}
			Arrays.sort(next);
		}

		System.out.println("Yes");
	}
}
