import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int n = in.nextInt();
		int h = in.nextInt();
		while (n != 0 || h != 0) {
			long all = n * n * n - h * n;
			Integer[][] lines = new Integer[h][];
			Set<Integer> aaa = new HashSet<Integer>();
			for (int i = 0; i < h; i++) {
				aaa.clear();
				String o = in.next();
				Integer[] block;
				Integer a = Integer.valueOf(in.nextInt());
				Integer b = Integer.valueOf(in.nextInt());
				if ("xy".equals(o)) {
					block = new Integer[] { a, b, null };
					for (int j = 0; j < i; j++) {
						Integer[] other = lines[j];
						if (other[2] != null && (a.equals(other[0]) || b.equals(other[1]))) {
							aaa.add(other[2]);
						}
					}
				} else if ("xz".equals(o)) {
					block = new Integer[] { a, null, b };
					for (int j = 0; j < i; j++) {
						Integer[] other = lines[j];
						if (other[1] != null && (a.equals(other[0]) || b.equals(other[2]))) {
							aaa.add(other[1]);
						}
					}
				} else {
					block = new Integer[] { null, a, b };
					for (int j = 0; j < i; j++) {
						Integer[] other = lines[j];
						if (other[0] != null && (a.equals(other[1]) || b.equals(other[2]))) {
							aaa.add(other[0]);
						}
					}
				}
				lines[i] = block;
				all += aaa.size();
			}
			Arrays.toString(lines);
			System.out.println(all);
			n = in.nextInt();
			h = in.nextInt();
		}
	}

}
