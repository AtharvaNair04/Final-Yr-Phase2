import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.TreeSet;

class Scanner {
    private final InputStream in = System.in;
    private final byte[] buffer = new byte[1024];
    private int ptr = 0;
    private int buflen = 0;

    private boolean hasNextByte() {
        if (ptr < buflen) {
            return true;
        } else {
            ptr = 0;
            try {
                buflen = in.read(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (buflen <= 0) {
                return false;
            }
        }
        return true;
    }

    private int readByte() {
        return hasNextByte() ? buffer[ptr++] : -1;
    }

    private boolean isPrintableChar(int c) {
        return 33 <= c && c <= 126;
    }

    private void skipUnprintable() {
        while (hasNextByte() && !isPrintableChar(buffer[ptr])) ptr++;
    }

    public boolean hasNext() {
        skipUnprintable();
        return hasNextByte();
    }

    public String next() {
        if (!hasNext()) throw new NoSuchElementException();
        StringBuilder sb = new StringBuilder();
        int b = readByte();
        while (isPrintableChar(b)) {
            sb.appendCodePoint(b);
            b = readByte();
        }
        return sb.toString();
    }

    public int nextInt() {
        return (int) nextLong();
    }

    public long nextLong() {
        if (!hasNext()) throw new NoSuchElementException();
        long n = 0;
        boolean minus = false;
        int b = readByte();
        if (b == '-') {
            minus = true;
            b = readByte();
        }
        if (b < '0' || '9' < b) {
            throw new NumberFormatException();
        }
        while (true) {
            if ('0' <= b && b <= '9') {
                n *= 10;
                n += b - '0';
            } else if (b == -1 || !isPrintableChar(b)) {
                return minus ? -n : n;
            } else {
                throw new NumberFormatException();
            }
            b = readByte();
        }
    }
}

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner();
	    int n = sc.nextInt();
	    int k = sc.nextInt();
	    int[] ps = new int[n];
	    for (int i = 0; i < n; ++i) {
	    	ps[i] = sc.nextInt();
	    }
	    int ans = solve(n, k, ps);
	    System.out.println(ans);
	}

	public static int solve(int n, int k, int[] ps) {
		LinkedList<Integer> list = new LinkedList<>();
		TreeSet<Integer> set = new TreeSet<>();
		for (int i = 0; i < k; ++i) {
			list.add(ps[i]);
			set.add(ps[i]);
		}

		int m = 0;
		int mi[] = new int[n - k + 1];
		for(int i = k; i < n; ++i) {
			int top = list.poll();
			set.remove(top);
			if (set.floor(top) == null
					&& set.ceiling(ps[i]) == null) {
				m += 1;
				mi[i - k + 1] = 1;
			}

			set.add(ps[i]);
			list.add(ps[i]);
		}

		int[] s = new int[n];
		s[0] = 0;
		for (int i = 1; i < n ; ++i) {
			if (ps[i-1] < ps[i]) {
				s[i] = s[i-1] + 1;
			} else {
				s[i] = 0;
			}
		}
		int ns = (int)Arrays.stream(s).filter(e -> e >= k - 1).count();
		int[] si = new int[n - k + 1];
		for (int i = k - 1; i < n; ++i) {
			if (s[i] >= k - 1) si[i - k + 1] = 1;
		}

		int notCount = 0;
		for (int i = 0; i < n - k + 1; ++i) {
			if (si[i] == 1 || mi[i] == 1) notCount += 1;
		}


//		return n - k + 1 - notCount;

		if (ns > 0) return n - k + 2 - notCount;
		else return n - k + 1 - notCount;
	}


}
