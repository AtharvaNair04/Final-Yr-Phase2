import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

public class Main {
	int N, M;
	ArrayList<ArrayList<E>> graph;

	private class E implements Comparable<E>{
		int v, c;
		public E(int v, int c) {
			this.v = v;
			this.c = c;
		}

		public int compareTo(E e){
			return this.c - e.c;
		}
	}
	public void solve() {
		N = nextInt();
		M = nextInt();
		graph = new ArrayList<ArrayList<E>>();
		for (int i = 0; i < N; i++) {
			graph.add(new ArrayList<E>());
		}
		for (int i = 0; i < M; i++) {
			int a = nextInt() - 1;
			int b = nextInt() - 1;
			int c = nextInt();
			graph.get(a).add(new E(b, c));
		}

		PriorityQueue<E> pq = new PriorityQueue<E>();
		boolean[] used = new boolean[N];
		int ans = -1;
		pq.add(new E(0,0));
		while(pq.size() > 0){
			E  e = pq.poll();

			if(e.v == N - 1){
				ans = Math.max(ans, e.c);
			}

			if(used[e.v])continue;
			used[e.v] = true;

			for(E ee : graph.get(e.v)){
				if(e.c <= ee.c){
					pq.add(new E(ee.v,ee.c));
				}
			}
		}
		out.println(ans);
	}

	public static void main(String[] args) {
		out.flush();
		new Main().solve();
		out.close();
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
}
