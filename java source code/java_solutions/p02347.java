import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;

public class Main {
	public static void main(String[] args) throws IOException {

		UserScanner scan = new UserScanner();
		PrintWriter pwriter = new PrintWriter(System.out);

		int n = scan.nextInt();
		KDTree kdt = new KDTree(n, false);

		for (int i = 0; i < n; i++)
			kdt.add(i, scan.nextInt(), scan.nextInt());

		kdt.build();

		int[] ans;

		int q = scan.nextInt();
		for (int i = 0; i < q; i++) {
			ans = kdt.judge(scan.nextInt(), scan.nextInt(), scan.nextInt(), scan.nextInt());
			for (int j = 1; j < ans[0]; j++)
				pwriter.println(ans[j]);
			pwriter.println();
		}
		pwriter.flush();
		scan.close();
		System.exit(0);
	}
}

class KDTree {
	boolean debug;

	class Pt {
		int id;
		int x, y;
		boolean xJudge; // if true, divide left/right children by x-axis
						// otherwise by y-axis.
		int leftC, rightC;

		public Pt(int id, int x, int y) {
			this.id = id;
			this.x = x;
			this.y = y;
		}

		public void swap(Pt p) {
			int t = this.x;
			this.x = p.x;
			p.x = t;
			t = this.y;
			this.y = p.y;
			p.y = t;
			t = this.id;
			this.id = p.id;
			p.id = t;
		}

		public int key(boolean xjudge) {
			if (xjudge)
				return this.x;
			else
				return this.y;
		}
	}

	Pt[] pt;
	int root;
	int[] ans;

	public KDTree(int n, boolean b) {
		debug = b;
		pt = new Pt[n];
		ans = new int[n + 1];
	}

	public int[] judge(int sx, int tx, int sy, int ty) {
		ans[0] = 1;
		countIn(root, sx, tx, sy, ty);
		Arrays.sort(ans, 1, ans[0]);
		return ans;
	}

	private int countIn(int p, int sx, int tx, int sy, int ty) {
		if (p == Integer.MAX_VALUE)
			return 0;
		int r = 0;
		if (pt[p].x >= sx && pt[p].x <= tx && pt[p].y >= sy && pt[p].y <= ty) {
			ans[ans[0]++] = pt[p].id;
			r++;
		}
		if (pt[p].xJudge) {
			if (pt[p].x >= sx)
				r += countIn(pt[p].leftC, sx, tx, sy, ty);
			if (pt[p].x < tx)
				r += countIn(pt[p].rightC, sx, tx, sy, ty);
		} else {
			if (pt[p].y >= sy)
				r += countIn(pt[p].leftC, sx, tx, sy, ty);
			if (pt[p].y < ty)
				r += countIn(pt[p].rightC, sx, tx, sy, ty);
		}
		return r;
	}

	public void add(int id, int x, int y) {
		pt[id] = new Pt(id, x, y);
	}

	public void build() {
		root = buildTree(0, pt.length, true, 0);
	}

	// Divide elements of st, len into 2 group, and return the middle value
	// element id. Middle value is not the median, but about? middle value.
	// These elements are rearranged into less or equal value group as
	// left,middle
	// and greater as right.

	// And make the binary tree by recursively processing.

	private int buildTree(int st, int len, boolean xjudge, int depth) {
		if (len == 0)
			return Integer.MAX_VALUE;
		if (len == 1) {
			pt[st].xJudge = xjudge;
			pt[st].leftC = Integer.MAX_VALUE;
			pt[st].rightC = Integer.MAX_VALUE;
			return st;
		}

		int mid = pt[st + (int) (len * Math.random())].key(xjudge);
		// if (pt[st].key(xjudge) != pt[st + len - 1].key(xjudge))
		// mid = (pt[st].key(xjudge) + pt[st + len - 1].key(xjudge)) / 2;
		// else
		// mid = pt[st + len / 2].key(xjudge);
		int lp = searchMiddle(st, len, mid, xjudge);
		if (len > 10 && lp == st + len - 1)
			return buildTree(st, len, !xjudge, depth);

		if (debug)
			System.out.println("depth " + depth + "\tlen " + len + "\tleft " + (lp - st) + "\tright "
					+ (len - lp + st - 1) + "\tsample " + pt[st].key(xjudge) + ":" + pt[st + len - 1].key(xjudge) + ":"
					+ pt[st + len / 2].key(xjudge));

		pt[lp].xJudge = xjudge;
		pt[lp].leftC = buildTree(st, lp - st, !xjudge, depth + 1);
		pt[lp].rightC = buildTree(lp + 1, len - (lp - st) - 1, !xjudge, depth + 1);
		return lp;
	}

	// sort is too heavy load in JAVA? then CPU time exceed.
	// trying another method.
	private int searchMiddle(int st, int len, int mid, boolean xjudge) {

		int left = st, right = st + len - 1;
		while (true) {

			for (; left != right && pt[left].key(xjudge) <= mid; left++) {
				if (left > st && pt[left - 1].key(xjudge) > pt[left].key(xjudge))
					pt[left - 1].swap(pt[left]);
			}

			for (; left != right && pt[right].key(xjudge) > mid; right--)
				;

			if (left != right) {
				pt[left].swap(pt[right]);
				continue;
			} else if (pt[left].key(xjudge) > mid) {
				if (left > st)
					left--;
				break;
			} else {
				if (left > st && pt[left - 1].key(xjudge) > pt[left].key(xjudge))
					pt[left - 1].swap(pt[left]);
				break;
			}
		}
		return left;
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
