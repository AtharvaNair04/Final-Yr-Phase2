import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Arrays;

public class Main {
	static InputStream is;
	static PrintWriter out;
	static String INPUT = "";

	public static void main(String[] args) throws Exception {
		new Main().run();
	}

	void solver() {
		int V = ni();
		int E=0;
		boolean[][] a = new boolean[V][V];
		for (int i = 0; i < V; i++) {
			char[] tmp = ns().toCharArray();
			for (int j = 0; j < V; j++){
				if(tmp[j]=='Y'){
					a[i][j] = true;
					E++;
				}
			}
		}
		E/=2;

		int now = -1;
		int[] color = new int[V];
		Arrays.fill(color, -1);
		ArrayDeque<Integer> que=new ArrayDeque<>();
		for(int i=0;i<V;i++){
			if(color[i]==-1){
				++now;
				que.add(i);
				while(!que.isEmpty()){
					int v=que.poll();
					color[v]=now;
					for(int j=0;j<V;j++){
						if(a[v][j]&&color[j]==-1){
							que.add(j);
							color[j]=now;
						}
					}
				}
			}
		}
		now++;
		int[] Vs = new int[now];
		for (int i = 0; i < V; i++) {
			Vs[color[i]]++;
		}
		int odd = 0, even = 0;
		int res = V * (V - 1) / 2 - E;
		for (int i = 0; i < now; i++) {
			if (Vs[i] % 2 == 0) {
				even++;
			} else {
				odd++;
			}
		}
		if (V % 2 == 0) {
			if (res % 2 == 1) {
				out.println((odd <= (even + odd - 1) / 2 * 2)&&E!=0 ? "Taro" : "Hanako");
			} else {
				out.println(!(odd <= (even + odd - 2) / 2 * 2)&&E!=0 ? "Taro" : "Hanako");
			}
		} else if (V % 2 == 1) {
			out.println(res % 2 == 1 ? "Taro" : "Hanako");
		}
	}

	void run() {
		is = INPUT.isEmpty() ? System.in : new ByteArrayInputStream(INPUT.getBytes());
		out = new PrintWriter(System.out);
		solver();
		out.flush();
	}

	static long nl() {
		try {
			long num = 0;
			boolean minus = false;
			while ((num = is.read()) != -1 && !((num >= '0' && num <= '9') || num == '-'))
				;
			if (num == '-') {
				num = 0;
				minus = true;
			} else {
				num -= '0';
			}

			while (true) {
				int b = is.read();
				if (b >= '0' && b <= '9') {
					num = num * 10 + (b - '0');
				} else {
					return minus ? -num : num;
				}
			}
		} catch (IOException e) {
		}
		return -1;
	}

	static char nc() {
		try {
			int b = skip();
			if (b == -1)
				return 0;
			return (char) b;
		} catch (IOException e) {
		}
		return 0;
	}

	static double nd() {
		try {
			return Double.parseDouble(ns());
		} catch (Exception e) {
		}
		return 0;
	}

	static String ns() {
		try {
			int b = skip();
			StringBuilder sb = new StringBuilder();
			if (b == -1)
				return "";
			sb.append((char) b);
			while (true) {
				b = is.read();
				if (b == -1)
					return sb.toString();
				if (b <= ' ')
					return sb.toString();
				sb.append((char) b);
			}
		} catch (IOException e) {
		}
		return "";
	}

	public static char[] ns(int n) {
		char[] buf = new char[n];
		try {
			int b = skip(), p = 0;
			if (b == -1)
				return null;
			buf[p++] = (char) b;
			while (p < n) {
				b = is.read();
				if (b == -1 || b <= ' ')
					break;
				buf[p++] = (char) b;
			}
			return Arrays.copyOf(buf, p);
		} catch (IOException e) {
		}
		return null;
	}

	public static byte[] nse(int n) {
		byte[] buf = new byte[n];
		try {
			int b = skip();
			if (b == -1)
				return null;
			is.read(buf);
			return buf;
		} catch (IOException e) {
		}
		return null;
	}

	static int skip() throws IOException {
		int b;
		while ((b = is.read()) != -1 && !(b >= 33 && b <= 126))
			;
		return b;
	}

	static boolean eof() {
		try {
			is.mark(1000);
			int b = skip();
			is.reset();
			return b == -1;
		} catch (IOException e) {
			return true;
		}
	}

	static int ni() {
		try {
			int num = 0;
			boolean minus = false;
			while ((num = is.read()) != -1 && !((num >= '0' && num <= '9') || num == '-'))
				;
			if (num == '-') {
				num = 0;
				minus = true;
			} else {
				num -= '0';
			}

			while (true) {
				int b = is.read();
				if (b >= '0' && b <= '9') {
					num = num * 10 + (b - '0');
				} else {
					return minus ? -num : num;
				}
			}
		} catch (IOException e) {
		}
		return -1;
	}

	static void tr(Object... o) {
		System.out.println(Arrays.deepToString(o));
	}
}
