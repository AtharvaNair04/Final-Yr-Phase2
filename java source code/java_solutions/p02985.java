// practice with kaiboy
import java.io.*;
import java.util.*;

public class Main extends PrintWriter {
	Main() { super(System.out, true); }
	Scanner sc = new Scanner(System.in);
	public static void main(String[] $) {
		Main o = new Main(); o.main(); o.flush();
	}

	static final int MD = 1000000007;
	void main() {
		int n = sc.nextInt();
		int k = sc.nextInt();
		if (n == 1) {
			println(k);
			return;
		}
		int[] dd = new int[n];
		long ans = (long) k * (k - 1) % MD;
		while (--n > 0) {
			int i = sc.nextInt() - 1;
			int j = sc.nextInt() - 1;
			if (++dd[i] >= 2)
				ans = ans * (k - dd[i]) % MD;
			if (++dd[j] >= 2)
				ans = ans * (k - dd[j]) % MD;
		}
		println(ans);
	}
}
