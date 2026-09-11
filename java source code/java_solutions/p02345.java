import java.io.IOException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws IOException {

		Scanner scan = new Scanner(System.in);

		int n = scan.nextInt();
		RQ rq = new RQ(n, false);

		int q = scan.nextInt();
		for (int i = 0; i < q; i++)
			switch (scan.nextInt()) {
			case (0):
				rq.update(scan.nextInt(), scan.nextInt());
				break;
			case (1):
				System.out.println(rq.find(scan.nextInt(), scan.nextInt()));
				break;
			}

		scan.close();
		System.exit(0);
	}
}

class RQ {
	boolean debug;
	int[] minTable;
	int ixn;

	public RQ(int n, boolean d) {
		debug = d;

		ixn = (int) (Math.log(n) / Math.log(2)) + 1;
		ixn = (int) Math.pow(2, ixn);
		minTable = new int[n + ixn];
		int ini = (int) (Math.pow(2, 31) - 1);
		for (int i = 1; i < minTable.length; i++)
			minTable[i] = ini;
	}

	public int find(int x1, int x2) {
		return idxfind(x1 + ixn, x2 + ixn);
	}

	private int idxfind(int x1, int x2) {
		int ret = Math.min(minTable[x1], minTable[x2]);
		int x1up = (x1 + 1) / 2;
		int x2up = (x2 - 1) / 2;
		if (x1up > x2up)
			return ret;
		else
			return Math.min(ret, idxfind(x1up, x2up));
	}

	public void update(int x, int y) {
		minTable[ixn + x] = y;

		int i = (ixn + x) / 2;
		while (i > 0) {
			int min = minTable[i * 2];
			if (i * 2 + 1 < minTable.length)
				min = Math.min(minTable[i * 2], minTable[i * 2 + 1]);
			if (minTable[i] == min)
				break;
			minTable[i] = min;
			i /= 2;
		}

		if (debug) {
			int j = 1;
			for (int k = 1; k < minTable.length; k++) {
				if (k == j) {
					System.out.println();
					j *= 2;
				}
				System.out.print(" " + k + ":" + minTable[k]);
			}
			System.out.println();
		}
	}
}
