import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Main implements Runnable{
	int N;
	int[] S;
	public void solve() {
		N = nextInt();
		S = new int[N];
		for(int i = 0;i < N;i++){
			S[i] = nextInt();
		}

		ArrayList<Integer> ts = new ArrayList<Integer>();
		for(int i = 1;i <= N;i++){
			if(N%i==0){
				ts.add(i);
			}
		}
		int ans = 0;
		for(int t : ts){

			int k = N / t;
			boolean flag = false;
			for(int i = 0;i < t;i++){
				for(int j = 0;j < k;j++){
					if(S[j * t + i]==S[i])continue;
					flag = true;
					break;
				}
				if(flag)break;
			}

			if(!flag){
				ans = Math.max(ans, k);
			}
		}

		out.println(ans);
	}

	public static void main(String[] args) {
		new Thread(null,new Main(),"",32 * 1024 * 1024).start();
	}

	/* Input */
	private static final InputStream in = System.in;
	private static final PrintWriter out = new PrintWriter(System.out);
	private final byte[] buffer = new byte[2048];
	private int p = 0;
	private int buflen = 0;

	private boolean hasNextByte() {
		if (p < buflen)
			return true;
		p = 0;
		try {
			buflen = in.read(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (buflen <= 0)
			return false;
		return true;
	}

	public boolean hasNext() {
		while (hasNextByte() && !isPrint(buffer[p])) {
			p++;
		}
		return hasNextByte();
	}

	private boolean isPrint(int ch) {
		if (ch >= '!' && ch <= '~')
			return true;
		return false;
	}

	private int nextByte() {
		if (!hasNextByte())
			return -1;
		return buffer[p++];
	}

	public String next() {
		if (!hasNext())
			throw new NoSuchElementException();
		StringBuilder sb = new StringBuilder();
		int b = -1;
		while (isPrint((b = nextByte()))) {
			sb.appendCodePoint(b);
		}
		return sb.toString();
	}

	public int nextInt() {
		return Integer.parseInt(next());
	}

	public long nextLong() {
		return Long.parseLong(next());
	}

	public double nextDouble() {
		return Double.parseDouble(next());
	}

	@Override
	public void run() {
		out.flush();
		new Main().solve();
		out.close();

	}
}
