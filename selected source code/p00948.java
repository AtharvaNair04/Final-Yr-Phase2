import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.StringTokenizer;

public class Main {
	
	public static class Pair implements Comparable<Pair> {
		int x, y;
		
		public Pair(int x, int y){
			this.x = x;
			this.y = y;
		}
		
		@Override public int compareTo(Pair arg0){
			return Integer.compare(this.x, arg0.x);
		}
	}

	public static void main(String[] args) throws IOException {
		final Scanner sc = new Scanner(System.in);
		
		final int n = sc.nextInt();
		final int m = sc.nextInt();
		
		int[] upper = new int[n];
		int[] lower = new int[n];
		for(int i = 0; i < n; i++){
			upper[i] = lower[i] = i;
		}
		
		Pair[] pair = new Pair[m];
		for(int i = 0; i < m; i++){
			final int x = sc.nextInt();
			final int y = sc.nextInt() - 1;
			
			pair[i] = new Pair(x, y);
		}
		Arrays.sort(pair);
		
		for(int i = 0; i < m; i++){
			//final int x = pair[i].x;
			final int y = pair[i].y;
			
			upper[y] = Math.max(upper[y], upper[y + 1]);
			lower[y + 1] = Math.min(lower[y], lower[y + 1]);
		}
		
		//System.out.println(Arrays.toString(upper));
		//System.out.println(Arrays.toString(lower));
		
		//System.out.println(upper_most + " " + lower_most);
		
		
		PrintWriter pw = new PrintWriter(System.out);
		for(int i = 0; i < n; i++){
			//pw.println(upper[i] + ", " + lower[i]);
			//*
			pw.print(i == 0 ? "" : " ");
			pw.print((upper[i] - lower[i]) + 1);
			//*/
		}
		pw.println();
		pw.flush();
		pw.close();
	}

	public static class Scanner implements Closeable {
		private BufferedReader br;
		private StringTokenizer tok;

		public Scanner(InputStream is) throws IOException {
			br = new BufferedReader(new InputStreamReader(is));
		}

		private void getLine() throws IOException {
			while (!hasNext()) {
				tok = new StringTokenizer(br.readLine());
			}
		}

		private boolean hasNext() {
			return tok != null && tok.hasMoreTokens();
		}

		public String next() throws IOException {
			getLine();
			return tok.nextToken();
		}

		public int nextInt() throws IOException {
			return Integer.parseInt(next());
		}

		public long nextLong() throws IOException {
			return Long.parseLong(next());
		}

		public double nextDouble() throws IOException {
			return Double.parseDouble(next());
		}

		public void close() throws IOException {
			br.close();
		}
	}
}
