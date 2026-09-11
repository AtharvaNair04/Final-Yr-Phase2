import java.util.*;
import java.io.*;
import java.awt.geom.*;
import java.math.*;

public class Main {

	static final Scanner in = new Scanner(System.in);
	static final PrintWriter out = new PrintWriter(System.out,false);

	static int w,h,si,sj,n;
	static int[] di = {0,1,0,-1};
	static int[] dj = {1,0,-1,0};
	static int[][] table;
	static BitSet visited;

	static boolean solve() {
		w = in.nextInt();
		h = in.nextInt();
		if (w == 0 && h == 0) return false;
		table = new int[h][w];
		si = -1; sj = -1;
		n = 0;

		for (int i=0; i<h; i++) {
			for (int j=0; j<w; j++) {
				table[i][j] = in.nextInt();
				if (table[i][j] == 1) {
					n++;
				} else if (table[i][j] == 2) {
					si = i; sj = j;
					table[i][j] = 0;
				}
			}
		}

		visited = new BitSet();
		out.println(rec(si,sj));
		out.flush();
		return true;
	}

	static int rec(int ci, int cj) {

		if (visited.cardinality() == n) {
			return ci == si || cj == sj ? 1 : 0;
		}

		int res = 0;

		for (int k=0; k<4; k++) {
			int ni = ci + di[k];
			int nj = cj + dj[k];
			while (0 <= ni && 0 <= nj && ni < h && nj < w) {
				if (table[ni][nj] > 0 && !visited.get(ni*w+nj)) {
					visited.set(ni*w+nj);
					res += rec(ni, nj);
					visited.set(ni*w+nj, false);
					break;
				}
				ni += di[k];
				nj += dj[k];
			}
		}

		return res;
	}

	public static void main(String[] args) {
		long start = System.currentTimeMillis();

		while(solve());
		out.flush();

		long end = System.currentTimeMillis();
		//trace(end-start + "ms");
		in.close();
		out.close();
	}

	static void trace(Object... o) { System.out.println(Arrays.deepToString(o));}
}
