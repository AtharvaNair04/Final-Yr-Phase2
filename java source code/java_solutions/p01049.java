import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Main {
	
	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		
		final int N = sc.nextInt();
		final long a = sc.nextLong();
		final long d = sc.nextLong();
		
		final int M = sc.nextInt();
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for(int i = 0; i < M; i++){
			final int x = sc.nextInt();
			final int y = sc.nextInt() - 1;
			final int z = sc.nextInt() - 1;
			
			if(!map.containsKey(y)){
				map.put(y, y);
			}
			if(!map.containsKey(z)){
				map.put(z, z);
			}
			
			final int y_value = map.get(y);
			final int z_value = map.get(z);
			
			map.put(y, z_value);
			if(x == 0){
				map.put(z, y_value);
			}
		}
		
		final int K = sc.nextInt() - 1;
		final int index = map.containsKey(K) ? map.get(K) : K;
		//System.out.println(map);
		
		System.out.println(a + index * d);
		
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

		public void close() throws IOException {
			br.close();
		}
	}
}
