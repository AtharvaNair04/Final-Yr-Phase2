import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
	public static void main(String[] args) {
		new Main().run();
	}

	private void run() {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int d[] = new int[n];
		for (int i = 0; i < d.length; i++) {
			d[i] = sc.nextInt();
		}
		Set<String> set = new HashSet<String>();
		for (int i = 0; i < d.length; i++) {
			String val = Integer.toString(d[i]);
			set.add(val);
			for (int j = i + 1; j < Math.min(d.length, i + 10); j++) {
				val += d[j];
				set.add(val);
			}
		}
		for (long i = 0; i < Long.MAX_VALUE; i++) {
			if (!set.contains(Long.toString(i))) {
				System.out.println(i);
				break;
			}
		}
		sc.close();
	}
}
