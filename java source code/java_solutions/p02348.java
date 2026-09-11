import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws FileNotFoundException {
		new Main().run();
	}

	int[] arr;
	int[] rep;
	boolean[] edge;
	int d;

	int get(int i) {
		if (!edge[i / d]) {
			return rep[i / d];
		} else {
			return arr[i];
		}
	}

	void modify(int src, int dst, int x) {
		for (int i = src / d + 1; i <= dst / d - 1; ++i) {
			edge[i] = false;
			rep[i] = x;
		}
		if (!edge[src / d]) {
			for (int i = src / d * d; i < (src / d + 1) * d; ++i) {
				arr[i] = rep[src / d];
			}
		}
		if (!edge[dst / d]) {
			for (int i = dst / d * d; i < (dst / d + 1) * d; ++i) {
				arr[i] = rep[dst / d];
			}
		}
		edge[src / d] = true;
		edge[dst / d] = true;
		for (int i = src; i <= Math.min((src / d + 1) * d - 1, dst); ++i) {
			arr[i] = x;
		}
		for (int i = Math.max(src, dst / d * d); i <= dst; ++i) {
			arr[i] = x;
		}
	}

	void run() {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int q = sc.nextInt();
		d = (int) Math.sqrt(n);
		arr = new int[n];
		Arrays.fill(arr, Integer.MAX_VALUE);
		edge = new boolean[n / d + 1];
		rep = new int[n / d + 1];
		Arrays.fill(rep, Integer.MAX_VALUE);
		Arrays.fill(edge, true);
		while (q-- > 0) {
			int t = sc.nextInt();
			if (t == 1) {
				System.out.println(get(sc.nextInt()));
			} else {
				int src = sc.nextInt();
				int dst = sc.nextInt();
				int x = sc.nextInt();
				modify(src, dst, x);
			}
		}
	}

	void tr(Object... objects) {
		System.out.println(Arrays.deepToString(objects));
	}
}
