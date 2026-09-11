import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Vector;

public class Main {

	public static void main(String[] args) throws IOException {
		new Main().solve();
	}

	private void solve() throws IOException {
		try {
			solveA();
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.flush();
				out.close();
			}
		}

	}

	private void solveA() {
		int v = nextInt();
		int e = nextInt();
		//		List<Integer>[] adj = new List[v];
		List<List<Integer>> adj = new ArrayList<List<Integer>>();
		for (int i = 0; i < v; i++) {
			adj.add(new ArrayList<Integer>());
		}
		for (int i = 0; i < e; i++) {
			int from = nextInt();
			int to = nextInt();
			//			adj[from].add(to);
			adj.get(from).add(to);
		}
		// Create a array to store indegrees of all
		// vertices. Initialize all indegrees as 0.
		int indegree[] = new int[v];

		// Traverse adjacency lists to fill indegrees of
		// vertices. This step takes O(V+E) time
		for (int i = 0; i < v; i++) {
			//			List<Integer> temp = adj[i];
			List<Integer> temp = adj.get(i);
			for (int node : temp) {
				indegree[node]++;
			}
		}

		// Create a queue and enqueue all vertices with
		// indegree 0
		ArrayDeque<Integer> q = new ArrayDeque<Integer>();
		for (int i = 0; i < v; i++) {
			if (indegree[i] == 0)
				q.addLast(i);
		}

		// Initialize count of visited vertices
		int cnt = 0;

		// Create a vector to store result (A topological
		// ordering of the vertices)
		Vector<Integer> topOrder = new Vector<Integer>();
		while (!q.isEmpty()) {
			// Extract front of queue (or perform dequeue)
			// and add it to topological order
			int u = q.removeFirst();
			topOrder.add(u);

			// Iterate through all its neighbouring nodes
			// of dequeued node u and decrease their in-degree
			// by 1
			//			for (int node : adj[u]) {
			for (int node : adj.get(u)) {
				// If in-degree becomes zero, add it to queue
				if (--indegree[node] == 0)
					q.addFirst(node);
			}
			cnt++;
		}

		// Check if there was a cycle
		if (cnt != v) {
			System.out.println("There exists a cycle in the graph");
			return;
		}

		// Print topological order
		for (int i : topOrder) {
			out.println(i);
		}
	}

	private final PrintWriter out = new PrintWriter(System.out);
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

	public boolean hasNext() {
		skipUnprintable();
		return hasNextByte();
	}

	public int nextInt() {
		return Integer.parseInt(next());
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
}
