import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		int q = scan.nextInt();
		String[] X = new String[q];
		String[] Y = new String[q];
		for (int i = 0; i < q; i++) {
			X[i] = scan.next();
			Y[i] = scan.next();
		}

		for (int i = 0; i < q; i++) {
			System.out.println(lcs(X[i], Y[i]));
		}

		scan.close();
	}

	public static int lcs(String x, String y) {
		int ans = 0;
		x = ' ' + x;
		y = ' ' + y;
		// c[i][i]???X[i]??¨Y[i]???LCS?????????
		int[][] c = new int[x.length() + 1][y.length() + 1];
		
		for (int i = 0; i < x.length(); i++) {
			for (int j = 0; j < y.length(); j++) {
				if (i == 0 || j == 0) {
					c[i][j] = 0;
				} else if (x.charAt(i) == y.charAt(j)) {
					c[i][j] = c[i - 1][j - 1] + 1;
				}else if(x.charAt(i) != y.charAt(j)){
					c[i][j] = Math.max(c[i-1][j], c[i][j-1]);
				}
				ans = Math.max(ans, c[i][j]);
			}
		}
		
		return ans;
		
	}
}
