import java.awt.geom.Line2D;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
	
	void run() throws Exception
	{
		is = INPUT.isEmpty() ? System.in : new ByteArrayInputStream(INPUT.getBytes());
		out = new PrintWriter(System.out);
		
		long s = System.currentTimeMillis();
		solver();
		out.flush();
		if(!INPUT.isEmpty())tr(System.currentTimeMillis()-s+"ms");
//		Thread t = new Thread(null, null, "~", Runtime.getRuntime().maxMemory()){
//			@Override
//			public void run() {
//				long s = System.currentTimeMillis();
//				solve();
//				out.flush();
//				if(!INPUT.isEmpty())tr(System.currentTimeMillis()-s+"ms");
//			}
//		};
//		t.start();
//		t.join();
	}
	
	public static void main(String[] args) throws Exception { new Main().run(); }
	
	private byte[] inbuf = new byte[1024];
	private int lenbuf = 0, ptrbuf = 0;
	
	private int readByte()
	{
		if(lenbuf == -1)throw new InputMismatchException();
		if(ptrbuf >= lenbuf){
			ptrbuf = 0;
			try { lenbuf = is.read(inbuf); } catch (IOException e) { throw new InputMismatchException(); }
			if(lenbuf <= 0)return -1;
		}
		return inbuf[ptrbuf++];
	}
	
	private boolean isSpaceChar(int c) { return !(c >= 33 && c <= 126); }
	private int skip() { int b; while((b = readByte()) != -1 && isSpaceChar(b)); return b; }
	
	private double nd() { return Double.parseDouble(ns()); }
	private char nc() { return (char)skip(); }
	
	private String ns()
	{
		int b = skip();
		StringBuilder sb = new StringBuilder();
		while(!(isSpaceChar(b))){ // when nextLine, (isSpaceChar(b) && b != ' ')
			sb.appendCodePoint(b);
			b = readByte();
		}
		return sb.toString();
	}
	
	private char[] ns(int n)
	{
		char[] buf = new char[n];
		int b = skip(), p = 0;
		while(p < n && !(isSpaceChar(b))){
			buf[p++] = (char)b;
			b = readByte();
		}
		return n == p ? buf : Arrays.copyOf(buf, p);
	}
	
	private int[] na(int n)
	{
		int[] a = new int[n];
		for(int i = 0;i < n;i++)a[i] = ni();
		return a;
	}
	
	private long[] nal(int n)
	{
		long[] a = new long[n];
		for(int i = 0;i < n;i++)a[i] = nl();
		return a;
	}
	
	private char[][] nm(int n, int m) {
		char[][] map = new char[n][];
		for(int i = 0;i < n;i++)map[i] = ns(m);
		return map;
	}
	
	private int[][] nmi(int n, int m) {
		int[][] map = new int[n][];
		for(int i = 0;i < n;i++)map[i] = na(m);
		return map;
	}
	
	private int ni() { return (int)nl(); }
	
	private long nl()
	{
		long num = 0;
		int b;
		boolean minus = false;
		while((b = readByte()) != -1 && !((b >= '0' && b <= '9') || b == '-'));
		if(b == '-'){
			minus = true;
			b = readByte();
		}
		
		while(true){
			if(b >= '0' && b <= '9'){
				num = num * 10 + (b - '0');
			}else{
				return minus ? -num : num;
			}
			b = readByte();
		}
	}
	

	
	InputStream is;
	PrintWriter out;
	String INPUT = "";

	double EPS = 1e-7;

	void solver() {
		int N = ni();
		double gx = 0, gy = 0;
		double[] x = new double[N];
		double[] y = new double[N];
		for (int i = 0; i < N; i++) {
			x[i] = ni();
			y[i] = ni();
			gx += x[i];
			gy += y[i];
		}
		gx /= N;
		gy /= N;
		int[] ord = convexHull(x, y);
		if (ord.length < 3) {
			out.println("No");
			return;
		}
		boolean[] vertex_of_convexHull = new boolean[N];
		for (int i = 0; i < ord.length; i++) {
			vertex_of_convexHull[ord[i]] = true;
		}

		double[] angle = new double[ord.length];

		for (int i = 0; i < ord.length; i++) {
			int n0 = ord[i];
			int n1 = ord[(i - 1 + ord.length) % ord.length];
			int n2 = ord[(i + 1) % ord.length];
			angle[i] = angle((x[n1] - x[n0]), (y[n1] - y[n0]), (x[n2] - x[n0]), (y[n2] - y[n0]));
		}
		
		for (int i = 0; i < angle.length; i++) {

			// the middle point between p_(i+1) and p_(i-1)
			double[] p = { (x[ord[(i + 1) % angle.length]] + x[ord[(i - 1 + angle.length) % angle.length]]) / 2.0,
					(y[ord[(i + 1) % angle.length]] + y[ord[(i - 1 + angle.length) % angle.length]]) / 2.0 };
			// the formula for the pseudo symmetric line
			double dy = (p[1] - y[ord[(i + 1) % ord.length]]);
			double dx = p[0] - x[ord[(i + 1) % ord.length]];
			double a, b, c;
			// ax+b-cy=0;
			// ax+b-cy=0;
			if (dy == 0) {
				a = 1;
				b = -p[0];
				c = 0;
			} else {
				a = -dx / dy;
				b = -a * p[0] + p[1];
				c = 1;
			}
			
			if (!onLine(a, b, c, gx, gy))
				continue;
			boolean flag = true;
			int on_symmetric_line = 1;
			for (int j = 1; j <= ((angle.length % 2 == 0) ? -1 : 0) + angle.length / 2; j++) {
				if (!eq(angle[(i + j) % angle.length], angle[(i - j + angle.length) % angle.length])) {
					flag = false;
					break;
				}
				if (onLine(a, b, c, x[ord[(i + j) % angle.length]], y[ord[(i + j) % angle.length]])) {
					on_symmetric_line++;
					if (on_symmetric_line >= 3) {
						flag = false;
						break;
					}
				}
			}
			if (angle.length % 2 == 0 && !onLine(a, b, c, x[ord[(i + angle.length / 2) % angle.length]],
					y[ord[(i + angle.length / 2) % angle.length]])) {
				continue;
			} else
				on_symmetric_line++;
			out: if (flag) {
				ArrayList<Pair> list = new ArrayList<>();
				for (int j = 0; j < N; j++) {
					if (!vertex_of_convexHull[j]) {
						list.add(new Pair(
								(x[j] - x[ord[i]]) * (x[j] - x[ord[i]]) + (y[j] - y[ord[i]]) * (y[j] - y[ord[i]]),
								Math.abs(a * x[j] + b - c * y[j]) / (Math.sqrt(a * a + c * c))));
					}
				}
				list.sort(null);
				for (int j = 0; j < list.size(); j++) {
					while (list.size() > 0 && eq(list.get(j).dist, 0)) {
						list.remove(j);
						on_symmetric_line++;
						if (on_symmetric_line >= 3)
							break out;
					}
					while (j + 1 < list.size() && eq(list.get(j).angle, list.get(j + 1).angle)
							&& eq(list.get(j).dist, list.get(j + 1).dist)) {
						list.remove(j);
						list.remove(j);
					}
				}
				if (list.isEmpty()) {
					out.println("Yes");
					return;
				}
			}
		}

		for (int i = 0; i < angle.length; i++) {
			// the middle point between p_(i+1) and p_(i-1)
			double[] p = { (x[ord[i]] + x[ord[(i - 1 + angle.length) % angle.length]]) / 2.0,
					(y[ord[i]] + y[ord[(i - 1 + angle.length) % angle.length]]) / 2.0 };
			// The symmetric line must go on the center of gravity
			double dy = p[1] - y[ord[i]];
			double dx = p[0] - x[ord[i]];
			double a, b, c;
			// ax+b-cy=0;
			if (dy == 0) {
				a = 1;
				b = -p[0];
				c = 0;
			} else {
				a = -dx / dy;
				b = -a * p[0] + p[1];
				c = 1;
			}
			if (!onLine(a, b, c, gx, gy))
				continue;
			boolean flag = true;
			int on_symmetric_line = 0;
			for (int j = 0; j < angle.length / 2 + (angle.length % 2 == 1 ? -1 : 0); j++) {
				if (!eq(angle[(i + j) % angle.length], angle[((i - 1) - j + angle.length) % angle.length])) {
					flag = false;
					break;
				}
				if (onLine(a, b, c, x[ord[(i + j) % angle.length]], y[ord[(i + j) % angle.length]])) {
					on_symmetric_line++;
					if (on_symmetric_line >= 3) {
						flag = false;
						break;
					}
				}
			}
			if (angle.length % 2 == 1 && !onLine(a, b, c, x[ord[(i + angle.length / 2) % angle.length]],
					y[ord[(i + angle.length / 2) % angle.length]])) {
				continue;
			}
			out: if (flag) {
				ArrayList<Pair> list = new ArrayList<>();
				for (int j = 0; j < N; j++) {
					if (!vertex_of_convexHull[j]) {
						list.add(new Pair((x[j] - p[0]) * (x[j] - p[0]) + (y[j] - p[1]) * (y[j] - p[1]),
								Math.abs(a * x[j] + b - c * y[j]) / (Math.sqrt(a * a + c * c))));
					}
				}
				list.sort(null);
//				showList(list);
				for (int j = 0; j < list.size(); j++) {
					while (list.size() > 0 && eq(list.get(j).dist, 0)) {
						list.remove(j);
						on_symmetric_line++;
						if (on_symmetric_line >= 3)
							break out;
					}
					while (j + 1 < list.size() && eq(list.get(j).angle, list.get(j + 1).angle)
							&& eq(list.get(j).dist, list.get(j + 1).dist)) {
						list.remove(j);
						list.remove(j);
					}
				}
				
				if (list.isEmpty()) {
					out.println("Yes");
					return;
				}
			}
		}
		out.println("No");

	}

	double angle(double x1, double y1, double x2, double y2) {
		// double rad = Math
		// .acos(dot_product(x1, y1, x2, y2) / (Math.sqrt(x1 * x1 + y1 * y1) *
		// Math.sqrt(x2 * x2 + y2 * y2)));
		// return Math.toDegrees(rad);
		// System.out.println(rad);
		// return rad;
		return dot_product(x1, y1, x2, y2) / (Math.sqrt(x1 * x1 + y1 * y1) * Math.sqrt(x2 * x2 + y2 * y2));
		// return dot_product(x1, y1, x2, y2) * dot_product(x1, y1, x2, y2) /
		// ((x1 * x1 + y1 * y1) * (x2 * x2 + y2 * y2));
	}

	double dot_product(double x1, double y1, double x2, double y2) {
		return x1 * x2 + y1 * y2;
	}

	public static int[] convexHull(final double[] x, final double[] y) {
		int n = x.length;
		Integer[] ord = new Integer[n];
		for (int i = 0; i < n; i++)
			ord[i] = i;
		Arrays.sort(ord, new Comparator<Integer>() {
			@Override
			public int compare(Integer a, Integer b) {
				if (x[a] != x[b])
					return x[a] < x[b] ? -1 : 1;
				if (y[a] != y[b])
					return y[a] < y[b] ? -1 : 1;
				return 0;
			}
		});

		int[] ret = new int[n + 1];
		int p = 0;
		for (int i = 0; i < n; i++) {
			if (p >= 1 && x[ret[p - 1]] == x[ord[i]] && y[ret[p - 1]] == y[ord[i]])
				continue;
			while (p >= 2 && Line2D.relativeCCW(x[ret[p - 2]], y[ret[p - 2]], x[ret[p - 1]], y[ret[p - 1]], x[ord[i]],
					y[ord[i]]) == 1)
				p--;
			ret[p++] = ord[i];
		}

		int inf = p + 1;
		for (int i = n - 2; i >= 0; i--) {
			if (x[ret[p - 1]] == x[ord[i]] && y[ret[p - 1]] == y[ord[i]])
				continue;
			while (p >= inf && Line2D.relativeCCW(x[ret[p - 2]], y[ret[p - 2]], x[ret[p - 1]], y[ret[p - 1]], x[ord[i]],
					y[ord[i]]) == 1)
				p--;
			ret[p++] = ord[i];
		}

		return Arrays.copyOf(ret, p - 1);
	}

	void tr(Object... o) {
		System.out.println(Arrays.deepToString(o));
	}

	class Pair implements Comparable<Pair> {
		double angle;
		double dist;

		Pair(double angle, double dist) {
			this.angle = angle;
			this.dist = dist;
		}

		@Override
		public int compareTo(Pair o) {
			int ret = Double.compare(this.dist, o.dist);
			if(eq(this.dist,o.dist))ret=0;
			return (ret != 0) ? ret : Double.compare(this.angle, o.angle);
		}
	}

	boolean eq(double a, double b) {
		return Math.abs(a - b) < EPS;
	}

	boolean onLine(double a, double b, double c, double x, double y) {
		return eq(c * y - a * x - b, 0);
	}

	void showList(ArrayList<Pair> list) {
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).angle + " " + list.get(i).dist);
		}
	}
}
