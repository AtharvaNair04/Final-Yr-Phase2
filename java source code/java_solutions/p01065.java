import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.NoSuchElementException;

public class Main implements Runnable{
	int N,X,Y;
	int[] a,b,c,d;
	int[][][] dp;

	public int dfs(int n,int x,int y){

		if(n >= N){
			if(x < 300 || y < 300)return -1;
			return 0;
		}

		if(dp[n][x][y] != Integer.MIN_VALUE){
			return dp[n][x][y];
		}

		int ret = -1;

		{
			int tmp = dfs(n + 1,x - a[n],y + b[n]);
			if(tmp != -1){
				ret = Math.max(ret,tmp);
			}
		}

		{
			int tmp = dfs(n + 1,x,y - c[n]);

			if(tmp != -1){
				ret = Math.max(ret,tmp + d[n]);
			}
		}

		{
			int tmp = dfs(n + 1,x,y);

			if(tmp != -1){
				ret = Math.max(ret,tmp);
			}
		}
		return dp[n][x][y] = ret;
	}

	public void solve() {
		N = nextInt();
		X = nextInt();
		Y = nextInt();


		a = new int[N];
		b = new int[N];
		c = new int[N];
		d = new int[N];
		for(int i = 0;i < N;i++){
			a[i] = nextInt();
			b[i] = nextInt();
			c[i] = nextInt();
			d[i] = nextInt();
		}

		dp = new int[N][901][901];
		for(int i = 0;i < N;i++){
			for(int j = 0;j < 901;j++){
				for(int k = 0;k < 901;k++){
					dp[i][j][k] = Integer.MIN_VALUE;
				}
			}
		}
		int ans = dfs(0,X + 300,Y + 300);
		out.println(ans == -1 ? 0 : ans);
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
