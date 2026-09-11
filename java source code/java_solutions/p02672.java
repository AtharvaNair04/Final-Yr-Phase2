import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

public class Main {
	
	static MyScanner sc = new MyScanner();
	static PrintWriter out = new PrintWriter(System.out);
	static int L = 128;
	static char[] vals;
	
	static {
		vals = new char[62];
		int idx = 0;
		for (char c = 'a'; c <= 'z'; c++) {
			vals[idx++] = c;
		}
		for (char c = 'A'; c <= 'Z'; c++) {
			vals[idx++] = c;
		}
		for (char c = '0'; c <= '9'; c++) {
			vals[idx++] = c;
		}
	}
	
	static char[] merge(char[] a, char[] b) {
		char[] ret = new char[a.length + b.length];
		int aIdx = 0, bIdx = 0, idx = 0;
		
		while (aIdx < a.length && bIdx < b.length) {
			char[] tmp = new char[idx + 1 + b.length - bIdx];
			int i;
			for (i = 0; i < idx; i++) {
				tmp[i] = ret[i];
			}
			tmp[i++] = a[aIdx];
			for (int j = bIdx; j < b.length; j++) {
				tmp[i++] = b[j];
			}
			if (subString(tmp)) {
				ret[idx++] = a[aIdx++];
			} else {
				ret[idx++] = b[bIdx++];
			}
		}
		while (aIdx < a.length) {
			ret[idx++] = a[aIdx++];
		}
		while (bIdx < b.length) {
			ret[idx++] = b[bIdx++];
		}
		return ret;
	}
	
	static char[] joinAll(List<char[]> vals) {
		while (vals.size() > 1) {
			Collections.sort(vals, Comparator.comparingInt(a -> a.length));
			vals.set(0, merge(vals.get(0), vals.get(1)));
			vals.remove(1);
		}
		return vals.get(0);
	}
	
	static int findSize(char v) {
		return L - query(getRep(v, L));
	}
	
	static int findSize(char v, int first) {
		int from = first;
		int dif = 1;
		while (subString(getRep(v, from))) {
			from += dif;
			dif *= 2;
		}
		int left = from == first ? 0 : from - dif / 2, right = Math.min(from, L + 1);
		while (left + 1 < right) {
			int med = (left + right) / 2;
			
			if (subString(getRep(v, med))) {
				left = med;
			} else {
				right = med;
			}
		}
		return left;
	}
	
	static char[] getRep(char c, int size) {
		char[] ret = new char[size];
		Arrays.fill(ret, c);
		return ret;
	}
	
	static boolean subString(char[] s) {
		if (s.length > L) return false;
		int val = query(s);
		return val + s.length == L;
	}
	
	static int query(char[] s) {
		out.println("? " + new String(s));
		out.flush();
		return sc.nextInt();
	}
	
	public static void main(String[] args) throws IOException {
		List<char[]> subs = new ArrayList<>();
		int rem = L;
		for (int i = 0; i < vals.length && rem > 0; i++) {
			int size = findSize(vals[i]);
			if (size > 0) {
				subs.add(getRep(vals[i], size));
				rem -= size;
			}
		}
		L -= rem;
		char[] ret = joinAll(subs);
		out.println("! " + new String(ret));
		
		out.flush();
	}
	
	static class MyScanner {
		private BufferedReader br;
		private StringTokenizer tokenizer;
		
		public MyScanner() {
			br = new BufferedReader(new InputStreamReader(System.in));
		}
		
		public String next() {
			while (tokenizer == null || !tokenizer.hasMoreTokens()) {
				try {
					tokenizer = new StringTokenizer(br.readLine());
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			return tokenizer.nextToken();
		}
		
		public int nextInt() {
			return Integer.parseInt(next());
		}
		
		public long nextLong() {
			return Long.parseLong(next());
		}
	}
}
