import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.NoSuchElementException;

public class Main implements Runnable{
	int[][] table;
	int[] d;
	int ans;
	public int check(int[] d,int len){

		int prev = 0;

		for(int i = 0;i < len;i++){
			prev = table[prev][d[i]];
		}

		return prev;
	}

	public boolean single(int[] d){

		for(int i = 0;i < 5;i++){

			int prev = d[i];

			for(int j = 0;j < 10;j++){

				if(prev == j)continue;
				d[i] = j;
				if(check(d,5) == 0)return false;
				d[i] = prev;
			}
		}
		return true;
	}

	public boolean adjacent(int[] d){

		for(int i = 0;i < 4;i++){
			if(d[i] == d[i+1])continue;
			{
				int tmp = d[i];
				d[i] = d[i+1];
				d[i+1] = tmp;
			}

			if(check(d,5) == 0)return false;

			{
				int tmp = d[i];
				d[i] = d[i+1];
				d[i+1] = tmp;
			}
		}

		return true;

	}

	public void solve() {
		table = new int[10][10];
		for(int i = 0;i < 10;i++){
			for(int j = 0;j < 10;j++){
				table[i][j] = nextInt();
			}
		}

		d = new int[5];
		ans = 0;
		for(int i = 0;i < 10;i++){
			for(int j = 0;j < 10;j++){
				for(int k = 0;k < 10;k++){
					for(int l = 0;l < 10;l++){

						d[0] = i;
						d[1] = j;
						d[2] = k;
						d[3] = l;
						d[4] = check(d,4);

						if(!single(d) || !adjacent(d)){
							ans++;
						}
					}
				}
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
