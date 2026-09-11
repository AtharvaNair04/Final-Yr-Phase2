import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

public class Main {
	static boolean debug = false;

	public static void main(String[] args) throws IOException {

		UserScanner scan = new UserScanner();
		PrintWriter pwriter = new PrintWriter(System.out);

		int vn = scan.nextInt();
		int en = scan.nextInt();

		Travel t = new Travel(vn, en);

		for (int i = 0; i < en; i++)
			t.addRoot(scan.nextInt(), scan.nextInt(), scan.nextInt());

		pwriter.println(t.getDistance());

		pwriter.flush();
		scan.close();
		System.exit(0);
	}
}

class Travel {
	int[][] dist;
	int goal;
	int[] ix;
	int[][] hist;

	public Travel(int vn, int en) {
		dist = new int[vn][vn];
		for (int st = 0; st < vn; st++)
			for (int ed = 0; ed < vn; ed++)
				dist[st][ed] = -1;

		// index number for each vertices
		ix = new int[vn];
		ix[1] = 1;
		for (int i = 2; i < vn; i++)
			ix[i] = ix[i - 1] << 1;
		// all vertices is reached
		goal = (ix[vn - 1] << 1) - 1;

		hist = new int[vn][goal + 1];
		for (int i = 0; i < vn; i++)
			for (int j = 0; j < goal + 1; j++)
				hist[i][j] = Integer.MAX_VALUE;
	}

	public int getDistance() {
		return minDist(0, 0);
	}

	private int minDist(int st, int passed) {
		// st is start vertices, passed is sum of reached vertices index
		if (passed == goal)
			if (dist[st][0] >= 0)
				return dist[st][0];
			else
				return -1;

		if (hist[st][passed] < Integer.MAX_VALUE)
			return hist[st][passed];

		int min = -1;
		for (int i = 1; i < ix.length; i++) {
			if ((passed & ix[i]) != 0 || dist[st][i] == -1)
				continue;
			int d = minDist(i, (passed | ix[i]));
			if (d < 0)
				continue;
			d += dist[st][i];
			if (d >= 0 && (d < min || min == -1))
				min = d;
		}
	
		hist[st][passed] = min;
		return min;
	}

	public void addRoot(int s, int t, int d) {
		dist[s][t] = d;
	}

}

class UserScanner {
	private final InputStream in = System.in;
	private final byte[] buffer = new byte[1024];
	private int ptr = 0;
	private int buflen = 0;

	private boolean read() throws IOException {
		ptr = 0;
		buflen = in.read(buffer);
		if (buflen <= 0)
			return false;
		else
			return true;
	}

	private byte getByte() throws IOException {
		if (ptr >= buflen)
			read();
		if (isCtlSpace(buffer[ptr])) {
			return -1;
		} else
			return buffer[ptr++];
	}

	private void skipCtlSpace() throws IOException {
		if (ptr >= buflen)
			read();
		if (isCtlSpace(buffer[ptr])) {
			ptr++;
			skipCtlSpace();
		}
	}

	private boolean isCtlSpace(byte b) {
		if (b <= ' ' || b > '~')
			return true;
		else
			return false;
	}

	public void close() throws IOException {
		in.close();
	}

	public String next() throws IOException {
		skipCtlSpace();
		StringBuilder sb = new StringBuilder();
		byte b;
		while ((b = getByte()) != -1) {
			sb.appendCodePoint(b);
		}
		return sb.toString();
	}

	public int nextInt() throws IOException {
		skipCtlSpace();
		int n = 0;
		boolean minus = false;
		byte b;
		while ((b = getByte()) != -1) {
			if (b == '-')
				minus = true;
			else {
				n *= 10;
				n += (b - '0');
			}
		}
		if (minus)
			return n * -1;
		else
			return n;
	}
}
