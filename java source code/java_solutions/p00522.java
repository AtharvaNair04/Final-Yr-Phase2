import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Main {
	static boolean debug = true;

	public static void main(String[] args) throws IOException {

		UserScanner scan = new UserScanner(System.in);
		PrintWriter pwriter = new PrintWriter(System.out);

		int m = scan.nextInt();
		int n = scan.nextInt();
		Manju manju = new Manju(m, n);
		for (int i = 0; i < m; i++)
			manju.addManju(i, scan.nextInt());
		for (int i = 0; i < n; i++)
			manju.addBox(i, scan.nextInt(), scan.nextInt());

		pwriter.println(manju.getMaxProfit());

		pwriter.flush();

		scan.close();
		System.exit(0);
	}
}

class Manju {
	int[] pr; // price of manju
	int[] ag; // sub total price of manju's ordered by expense

	class Case {
		public Case(int c, int e) {
			ca = c;
			ex = e;
			exu = ex / ca;
		}

		int ca; // capacity of case
		int ex; // expense of case
		double exu; // expense per one manju
	}

	Case[] cs;
	int[][] hs;

	public Manju(int m, int n) {
		pr = new int[m];
		ag = new int[m];
		cs = new Case[n];
		hs = new int[n][m];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < m; j++)
				hs[i][j] = -1;
	}

	class caseComp implements Comparator<Case> {

		@Override
		public int compare(Case o1, Case o2) {
			if (o1.exu > o2.exu)
				return 1;
			else if (o1.exu == o2.exu)
				return 0;
			else
				return -1;
		}

	}

	public int getMaxProfit() {
		// sort natural order and rearrange descending order
		Arrays.sort(pr);
		for (int i = 0, j = pr.length - 1; j > i; i++, j--) {
			int tmp = pr[i];
			pr[i] = pr[j];
			pr[j] = tmp;
		}
		setAggrigatePrice();

		Arrays.sort(cs, new caseComp());

		return getMaxPf(0, 0);
	}

	private void setAggrigatePrice() {
		ag[0] = pr[0];
		for (int i = 1; i < pr.length; i++)
			ag[i] = ag[i - 1] + pr[i];
	}

	private int getMaxPf(int ci, int mj) {
		// 'ci' is case id which are ordered by cheaper
		// 'mj' is number of manju, manju are always apply the higher price
		// return max profit by using cases after 'ci'

		// caution: 'ci' is index, 'mj' is number(length)
		if (ci >= cs.length || mj >= pr.length)
			return 0;

		// if next case expense is larger than manju price, it's not necessary
		// to check anymore
		if (cs[ci].exu > pr[mj])
			return 0;

		// history check
		if (hs[ci][mj] >= 0)
			return hs[ci][mj];

		// return greater value case 'ci' is applied or not
		int ret = Math.max(getMaxPf(ci + 1, mj), getProfit(ci, mj) + getMaxPf(ci + 1, mj + cs[ci].ca));
		hs[ci][mj] = ret;
		return ret;
	}

	private int getProfit(int ci, int mj) {
		// return profit for case 'ci' using manju from 'mj'
		int to = Math.min(mj + cs[ci].ca - 1, pr.length - 1);
		if (mj == 0)
			return ag[to] - cs[ci].ex;
		else
			return ag[to] - ag[mj - 1] - cs[ci].ex;
	}

	public void addBox(int i, int c, int e) {
		cs[i] = new Case(c, e);
	}

	public void addManju(int i, int p) {
		this.pr[i] = p;
	}

}

class UserScanner {
	private InputStream in;
	private final byte[] buffer = new byte[1024];
	private int ptr = 0;
	private int buflen = 0;

	public UserScanner(InputStream inStream) {
		in = inStream;
	}

	private void read() {
		ptr = 0;
		try {
			buflen = in.read(buffer);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(9);
		}
	}

	private byte getByte() {
		if (ptr >= buflen)
			read();
		if (buflen < 0 || isCtlSpace(buffer[ptr])) {
			return -1;
		} else
			return buffer[ptr++];
	}

	private void skipCtlSpace() {
		for (; ptr < buflen; ptr++)
			if (!isCtlSpace(buffer[ptr]))
				return;
		read();
		skipCtlSpace();
	}

	private boolean isCtlSpace(byte b) {
		if (b <= ' ' || b > '~')
			return true;
		else
			return false;
	}

	public void close() {
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(9);
		}
	}

	public String next() {
		skipCtlSpace();
		StringBuilder sb = new StringBuilder();
		byte b;
		while ((b = getByte()) != -1) {
			sb.appendCodePoint(b);
		}
		return sb.toString();
	}

	public int nextInt() {
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
