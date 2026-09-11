// Submitted By Subhash Yadav
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

//
//300000 300000 1
//3 4
import java.util.*;

public class Main {
	static long hash(int x, int y) {
		return x * 1_000_000_000l + y;
	}

	public static void main(String[] args) {
		FastScanner sc = new FastScanner();
		PrintWriter out = new PrintWriter(System.out);
		int h = sc.nextInt(), w = sc.nextInt(), m = sc.nextInt();
		HashSet<Long> set = new HashSet<>();
		ArrayDeque<Integer> a1 = new ArrayDeque<>();

		int row[] = new int[h + 1];
		int col[] = new int[w + 1];
		long maxx = Long.MIN_VALUE, maxy = Long.MIN_VALUE;
		long count = 0;
		for (int i = 0; i < m; i++) {
			int x = sc.nextInt(), y = sc.nextInt();
			set.add(hash(x, y));

			row[x]++;
			col[y]++;
			maxx = Math.max(row[x], maxx);
			maxy = Math.max(col[y], maxy);
		}

		ArrayList<Integer> candidateXS = new ArrayList<>();
		ArrayList<Integer> candidateYS = new ArrayList<>();

		for (int i = 1; i <= h; i++) {

			if (maxx == row[i])
				candidateXS.add(i);

		}
		
		for(int j=1;j<=w;j++) if(maxy==col[j]) candidateYS.add(j);
		
		long ans=maxx+maxy-1;
		
		
		outer: for(int i:candidateXS) {
			for(int j:candidateYS) {
				if(!set.contains(hash(i, j))) {
					ans++;
					break outer;
				}
			}
		}
		

		

		System.out.println(ans);

		out.close();

	}

	static class FastScanner {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer("");

		String next() {
			while (!st.hasMoreTokens())
				try {
					st = new StringTokenizer(br.readLine());
				} catch (IOException e) {
					e.printStackTrace();
				}
			return st.nextToken();
		}

		int nextInt() {
			return Integer.parseInt(next());
		}

		int[] readArray(int n) {
			int[] a = new int[n];
			for (int i = 0; i < n; i++)
				a[i] = nextInt();
			return a;
		}

		long nextLong() {
			return Long.parseLong(next());
		}
	}
}
