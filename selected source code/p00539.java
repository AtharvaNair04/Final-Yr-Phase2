import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Main {
	static boolean debug = false;

	public static void main(String[] args) throws IOException {

		UserScanner scan = new UserScanner(System.in);
		PrintWriter pwriter = new PrintWriter(System.out);

		int n = scan.nextInt();
		int m = scan.nextInt();
		int c = scan.nextInt();
		Park park = new Park(n, m, c, debug);

		for (int i = 0; i < m; i++)
			park.setRoot(scan.nextInt(), scan.nextInt(), scan.nextInt());

		pwriter.println(park.getCost());

		pwriter.flush();

		scan.close();
		System.exit(0);
	}
}

class Park {
	boolean debug;
	int tunnelCost;

	// use priority queue to get least root from 1st
	class Fix {
		int area;
		long distance;

		public Fix(int i, long d) {
			area = i;
			distance = d;
		}
	}

	class CompPQ implements Comparator<Fix> {

		@Override
		public int compare(Fix o1, Fix o2) {
			if (o1.distance > o2.distance)
				return 1;
			else if (o1.distance == o2.distance)
				return 0;
			else
				return -1;
		}
	}

	PriorityQueue<Fix> pqFix = new PriorityQueue<Fix>(1, new CompPQ());

	// distance from 1st of each area
	boolean[] isFixed;
	long[] distanceFrom;

	// root which from 'from' to 'to' and have 'length'
	// create data from -> to and to -> from to get least pass
	// then set flag to determine the original
	class Root {
		int from;
		ArrayList<Integer> to = new ArrayList<Integer>();
		ArrayList<Integer> length = new ArrayList<Integer>();
		ArrayList<Boolean> original = new ArrayList<Boolean>();

		public Root(int from) {
			this.from = from;
		}
	}

	Root[] root;

	// save tunnel length and road length, then calculate least cost
	class RoadConst {
		long distance;
		int length;

		public RoadConst(long max, Integer len) {
			distance = max;
			length = len;
		}
	}

	ArrayList<RoadConst> road = new ArrayList<RoadConst>();

	class CompRoad implements Comparator<RoadConst> {

		@Override
		public int compare(RoadConst o1, RoadConst o2) {
			if (o1.distance > o2.distance)
				return -1;
			else if (o1.distance == o2.distance)
				return 0;
			else
				return 1;
		}

	}

	public Park(int n, int m, int c, boolean debug) {
		this.debug = debug;
		tunnelCost = c;
		isFixed = new boolean[n + 1];
		distanceFrom = new long[n + 1];
		for (int i = 1; i <= n; i++)
			distanceFrom[i] = Long.MAX_VALUE;
		root = new Root[n + 1];
		for (int i = 1; i <= n; i++)
			root[i] = new Root(i);
	}

	public long getCost() {
		// set 1st area as fixed, and set next nearest area as fixed
		// repeat until all area are fixed
		distanceFrom[1] = 0;
		pqFix.add(new Fix(1, 0));
		setDistance();
		if (debug)
			for (int i = 1; i < distanceFrom.length; i++)
				System.out.println("area=" + i + ", distance=" + distanceFrom[i] + " " + isFixed[i]);

		// get tunnel length and road length
		// and sort by tunnel length as descending order
		setRoad();
		Collections.sort(road, new CompRoad());

		// search least cost, calculating total road length
		return getMinCost();
	}

	private long getMinCost() {
		long minCost = Long.MAX_VALUE;
		long construct = 0;

		for (int i = 0; i < road.size(); i++) {
			if (debug)
				System.out.println("tunnel=" + road.get(i).distance + ", road=" + construct);

			long cost = (long) tunnelCost * road.get(i).distance + construct;
			if (minCost > cost)
				minCost = cost;
			construct += road.get(i).length;
		}
		
		// finally compare with no tunnel build
		return minCost = Math.min(minCost, construct);
	}

	private void setRoad() {
		// if from and to are both in tunnel range
		// this road is not need to construct 
		
		for (int i = 1; i < root.length; i++)
			for (int j = 0; j < root[i].original.size(); j++)
				if (root[i].original.get(j))
					road.add(new RoadConst(Math.max(distanceFrom[i], distanceFrom[root[i].to.get(j)]),
							root[i].length.get(j)));
	}

	private void setDistance() {
		while (!pqFix.isEmpty()) {
			Fix f = pqFix.poll();
			if (isFixed[f.area])
				continue;

			isFixed[f.area] = true;
			long st = distanceFrom[f.area];
			for (int i = 0; i < root[f.area].to.size(); i++) {
				int to = root[f.area].to.get(i);
				int l = root[f.area].length.get(i);
				if (st + l < distanceFrom[to]) {
					distanceFrom[to] = st + l;
					pqFix.add(new Fix(to, distanceFrom[to]));
				}
			}
		}

	}

	public void setRoot(int from, int to, int l) {
		root[from].to.add(to);
		root[from].length.add(l);
		root[from].original.add(true);

		root[to].to.add(from);
		root[to].length.add(l);
		root[to].original.add(false);
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
