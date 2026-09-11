import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

public class Main {
  public static void main(String[] args) {
    FastScanner sc = new FastScanner();
    long n = sc.nextLong();
    int m = sc.nextInt();
    int q = sc.nextInt();
    int[] a = sc.nextIntList(q);
    
    if (m % 7 != 0) {
      int[] b = new int[q * 7];
      for (int i = 0; i < q; i ++) {
        for (int j = 0; j < 7; j ++) {
          b[j * q + i] = a[i] + m * j;
        }
      }
      m *= 7;
      a = b;
    }
    
    int s1 = count(a, m, false);
    int s2 = count(a, m, true);
    
    long t1 = s2 - s1;
    long t2 = s1 - t1;
    long f = n * 7 / m;
    
    System.out.println(f * t1 + t2);
    
  }
  
  
  private static int count(int[] a, int m, boolean twice) {
    int n = m / 7 * (twice ? 2 : 1);
    
    int[][] map = new int[n][7];

    for (int x : a) {
      map[x / 7][x % 7] = -1;
      if (twice) {
        map[x / 7 + m / 7][x % 7] = -1;
      }
    }

    int[][] dir = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    int count = 0;
    Deque<int[]> queue = new ArrayDeque<>();
    for (int sx = 0; sx < 7; sx ++) {
      for (int sy = 0; sy < n; sy ++) {
        if (map[sy][sx] == 0) {
          count ++;
          map[sy][sx] = count;
        } else {
          continue;
        }

        queue.add(new int[]{sy, sx});
        while (queue.size() > 0) {
          int[] p = queue.poll();
          for (int[] d : dir) {
            int y = p[0] + d[0];
            int x = p[1] + d[1];
            
            if (0 <= x && x < 7 && 0 <= y && y < n && map[y][x] == 0) {
              queue.add(new int[]{y, x});
              map[y][x] = count;
            }
          }
        }
      }
    }
    return count;
  }
}




class FastScanner {
	public static String debug = null;

	private final InputStream in = System.in;
	private int ptr = 0;
	private int buflen = 0;
	private byte[] buffer = new byte[1024];
	private boolean eos = false;

	private boolean hasNextByte() {
		if (ptr < buflen) {
			return true;
		} else {
			ptr = 0;
			try {
				if (debug != null) {
					buflen = debug.length();
					buffer = debug.getBytes();
					debug = "";
					eos = true;
				} else {
					buflen = in.read(buffer);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (buflen < 0) {
				eos = true;
				return false;
			} else if (buflen == 0) {
				return false;
			}
		}
		return true;
	}

	private int readByte() {
		if (hasNextByte())
			return buffer[ptr++];
		else
			return -1;
	}

	private static boolean isPrintableChar(int c) {
		return 33 <= c && c <= 126;
	}

	private void skipUnprintable() {
		while (hasNextByte() && !isPrintableChar(buffer[ptr]))
			ptr++;
	}

	public boolean isEOS() {
		return this.eos;
	}

	public boolean hasNext() {
		skipUnprintable();
		return hasNextByte();
	}

	public String next() {
		if (!hasNext())
			throw new NoSuchElementException();
		StringBuilder sb = new StringBuilder();
		int b = readByte();
		while (isPrintableChar(b)) {
			sb.appendCodePoint(b);
			b = readByte();
		}
		return sb.toString();
	}

	public long nextLong() {
		if (!hasNext())
			throw new NoSuchElementException();
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

	public int nextInt() {
		return (int) nextLong();
	}

	public long[] nextLongList(int n) {
		return nextLongTable(1, n)[0];
	}

	public int[] nextIntList(int n) {
		return nextIntTable(1, n)[0];
	}

	public long[][] nextLongTable(int n, int m) {
		long[][] ret = new long[n][m];
		for (int i = 0; i < n; i ++) {
			for (int j = 0; j < m; j ++) {
				ret[i][j] = nextLong();
			}
		}
		return ret;
	}

	public int[][] nextIntTable(int n, int m) {
		int[][] ret = new int[n][m];
		for (int i = 0; i < n; i ++) {
			for (int j = 0; j < m; j ++) {
				ret[i][j] = nextInt();
			}
		}
		return ret;
	}
}
