import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int nc = sc.nextInt();
		Interval[] c = new Interval[nc];
		for(int i=0;i<nc;i++) {
			c[i] = new Interval(sc.next());
		}
		int n = sc.nextInt();
		Interval[][] t = new Interval[n][];
		for(int i=0;i<n;i++) {
			int k = sc.nextInt();
			t[i] = new Interval[k];
			for(int j=0;j<k;j++) {
				t[i][j] = new Interval(sc.next());
			}
		}
		int m = sc.nextInt();
		Interval[][] s = new Interval[m][];
		for(int i=0;i<m;i++) {
			int k = sc.nextInt();
			s[i] = new Interval[k];
			for(int j=0;j<k;j++) {
				s[i][j] = new Interval(sc.next());
			}
		}

		int[][] count = new int[2][nc];

		for(int i=0;i<n;i++) {
			for(int j=0;j<t[i].length;j++) {
				for(int k=0;k<nc;k++) {
					if (t[i][j].cover(c[k])) {
//						System.out.println(i + "," + j + "," + k);
						count[0][k]++;
					}
				}
			}
		}
//		System.out.println("----");

		for(int i=0;i<m;i++) {
			for(int j=0;j<s[i].length;j++) {
				for(int k=0;k<nc;k++) {
					if (s[i][j].cover(c[k])) {
//						System.out.println(i + "," + j + "," + k);
						count[1][k]++;
					}
				}
			}
		}
//		System.out.println(Arrays.deepToString(count));

		int ans = 0;
		for(int i=0;i<nc;i++) {
			ans += Math.min(count[0][i],count[1][i]);
		}
		System.out.println(ans);
	}
	static class Interval {
		int l,r;
		public Interval(String s) {
			String[] t = s.split("[:-]");
			l = Integer.parseInt(t[0]) * 60 + Integer.parseInt(t[1]);
			r = Integer.parseInt(t[2]) * 60 + Integer.parseInt(t[3]);
		}
		public boolean cover(Interval i) {
			return l <= i.l && i.r <= r;
		}
	}
}
