import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {

	private static int H, W, L, P;
	private static short[][] leftupper;
	private static short[][] rightlower;
	private static short[] fnr;
	private static short[] fnl;

	public static void main(String[] args) throws Exception {

		// xx();
		// System.exit(0);
		ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
		long startCpuTime = threadMXBean.getCurrentThreadCpuTime();

		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		H = scan.nextInt();
		W = scan.nextInt();
		L = scan.nextInt();
		P = scan.nextInt();

		leftupper = new short[W][H];
		rightlower = new short[W][H];
		fnl = new short[Math.min(W, H)];
		fnr = new short[Math.min(W, H)];

		for (int x = 0; x < W; x++)
			for (int y = 0; y < H; y++) {
				leftupper[x][y] = 0;
				rightlower[x][y] = 0;
			}
		for (int i = 0; i < P; i++) {
			int y = scan.nextInt() - 1;
			int x = scan.nextInt() - 1;
			leftupper[x][y] = -1;
			rightlower[x][y] = -1;
		}
		for (int y = 0; y < H; y++) {
			short free = 0;
			for (int x = W - 1; x >= 0; x--)
				if (leftupper[x][y] == -1)
					free = 0;
				else {
					free++;
					leftupper[x][y] = free;
				}
		}
		for (int x = 0; x < W; x++) {
			short free = 0;
			for (int y = H - 1; y >= 0; y--)
				if (leftupper[x][y] == -1)
					free = 0;
				else {
					free++;
					if (leftupper[x][y] > free)
						leftupper[x][y] = free;
				}
		}
		for (int y = 0; y < H; y++) {
			short free = 0;
			for (int x = 0; x < W; x++)
				if (rightlower[x][y] == -1)
					free = 0;
				else {
					free++;
					rightlower[x][y] = free;
				}
		}
		for (int x = 0; x < W; x++) {
			short free = 0;
			for (int y = 0; y < H; y++)
				if (rightlower[x][y] == -1)
					free = 0;
				else {
					free++;
					if (rightlower[x][y] > free)
						rightlower[x][y] = free;
				}
		}

		long st1CpuTime = threadMXBean.getCurrentThreadCpuTime();

		long result = 0;
		long result2 = 0;

		for (int x = 0; x < W; x++) {
			int e = Math.min(W - x, H);
			for (int i = 0; i < e; i++) {
				fnl[i] = leftupper[x + i][i];
				fnr[i] = rightlower[x + i][i];
			}
			result += countMatch(e);
			// result2 += countMatch2(e);
		}

		for (int y = 1; y < H; y++) {
			int e = Math.min(W, H - y);
			for (int i = 0; i < e; i++) {
				fnl[i] = leftupper[i][y + i];
				fnr[i] = rightlower[i][y + i];
			}
			result += countMatch(e);
			// result2 += countMatch2(e);

		}
		// System.out.println("use BIT ");
		System.out.println(result);
		// System.out.println("not use BIT\n" + result2);

		long stopCpuTime = threadMXBean.getCurrentThreadCpuTime();
		// System.out.println((float) (st1CpuTime - startCpuTime) / 1000 / 1000
		// / 1000);
		// System.out.println((float) (stopCpuTime - st1CpuTime) / 1000 / 1000 /
		// 1000);

		System.exit(0);
	}

	private static long countMatch2(int e) {
		int f = 0;
		for (int d = L - 1; d < e; d++) {
			int i = 0;
			int j = d;
			for (; j < e; i++, j++)
				if (fnl[i] > d && fnr[j] > d)
					f++;
		}
		return f;
	}

	private static long countMatch(int e) {
		List<DistancePos> order = new ArrayList<DistancePos>();
		for (int i = 0; i < e; i++) {
			order.add(new DistancePos(fnl[i], i, true));
			order.add(new DistancePos(fnr[i], i, false));
		}

		order.sort(new DistanceCompare());

		BIT bitLeft = new BIT(e);
		BIT bitRight = new BIT(e);

		int result = 0;
		for (int i = 0; i < 2 * e; i++) {
			if (order.get(i).isLeft()) {
				result += bitRight.range(order.get(i).getPos() + L - 1,
						order.get(i).getPos() + order.get(i).getDistance() - 1);
				bitLeft.add(order.get(i).getPos(), 1);
			} else {
				result += bitLeft.range(order.get(i).getPos() - order.get(i).getDistance() + 1,
						order.get(i).getPos() - L + 1);
				bitRight.add(order.get(i).getPos(), 1);

			}
		}
		return result;
	}

	static void xx() {

	}
}

class DistancePos {
	private short distance;
	private int pos;
	private boolean left;

	public DistancePos(short _distance, int _pos, boolean _left) {
		this.distance = _distance;
		this.pos = _pos;
		this.left = _left;
	}

	public int getDistance() {
		return this.distance;
	}

	public int getPos() {
		return this.pos;
	}

	public boolean isLeft() {
		return this.left;
	}
}

class DistanceCompare implements Comparator<DistancePos> {
	public int compare(DistancePos a, DistancePos b) {
		int ai = a.getDistance();
		int bi = b.getDistance();
		if (ai > bi)
			return -1;
		else if (ai == bi)
			return 0;
		else
			return 1;
	}
}

class BIT {
	private int[] bit;

	public BIT(int e) {
		bit = new int[e + 10];
		for (int i = 0; i < bit.length; i++)
			bit[i] = 0;
	}

	public void add(int pos, int val) {
		for (int i = pos; i < bit.length; i |= (i + 1))
			bit[i] += val;
	}

	public int sum(int n) {
		int result = 0;
		for (int i = n - 1; i >= 0; i = (i & (i + 1)) - 1)
			result += bit[i];
		return result;
	}

	public int range(int fromPos, int toPos) {
		if (fromPos > toPos)
			return 0;
		else
			return (this.sum(toPos + 1) - this.sum(fromPos));

	}

}
