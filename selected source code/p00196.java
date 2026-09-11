import java.util.*;

public class Main {
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		for (int n; (n = s.nextInt()) > 0;) {
			char[] c = new char[n];
			int[][] a = new int[n][3];
			for (int i = 0; i < n; ++i) {
				c[i] = s.next().charAt(0);
				a[i][0] = i;
				for (int j = 1; j < n; ++j) {
					int x = s.nextInt();
					if (x == 0) {
						++a[i][1];
					} else if (x == 1) {
						++a[i][2];
					}
				}
			}
			Arrays.sort(a, new Comparator<int[]>() {
				@Override
				public int compare(int[] o1, int[] o2) {
					if (o2[1] == o1[1]) {
						if (o1[2] == o2[2]) {
							return o1[0] - o2[0];
						} else {
							return o1[2] - o2[2];
						}
					}
					return o2[1] - o1[1];
				}
			});
			for(int i = 0; i < n; ++i){
				System.out.println(c[a[i][0]]);
			}
		}
	}
}
