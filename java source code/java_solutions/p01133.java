import java.io.File;
import java.io.FileNotFoundException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {

		ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
		long startCpuTime = threadMXBean.getCurrentThreadCpuTime(), stopCpuTime = 0;

		Scanner scan = new Scanner(System.in);
		// Scanner scan = new Scanner(new
		// File("D:\\UserArea\\J0124567\\Downloads\\2008-input.txt"));

		while (scan.hasNext()) {

			int n = scan.nextInt();
			int hx = scan.nextInt();
			int hy = scan.nextInt();
			int dx = scan.nextInt();
			int dy = scan.nextInt();
			if (n == 0)
				break;
			Map mp = new Map(n, hx, hy, dx, dy);
			for (int i = 0; i < n; i++)
				mp.add(i, scan.nextInt(), scan.nextInt());
			if (mp.result(0, n, hx, hy))
				System.out.println("YES");
			else
				System.out.println("NO");
			// stopCpuTime = threadMXBean.getCurrentThreadCpuTime();

		}

		scan.close();
		// stopCpuTime = threadMXBean.getCurrentThreadCpuTime();
		// System.out.println("CPU=" + (stopCpuTime - startCpuTime) / 1000000);

		System.exit(0);
	}
}

class Map {
	int Dx, Dy;
	int[] Cx, Cy;
	boolean[] Cs;
	double[] Ct;

	public Map(int n, int hx, int hy, int dx, int dy) {

		Dx = dx;
		Dy = dy;
		Cx = new int[n];
		Cy = new int[n];
		Cs = new boolean[n];
		Ct = new double[n];
	}

	public boolean result(double t, int n, int hx, int hy) {
		// System.out.println(n + ":" + hx + " " + hy);
		if (n == 0)
			return true;

		for (int i = 0; i < Cs.length; i++)
			if (Cs[i]) {
				double nt = Math.sqrt(Math.pow(Cx[i] - hx, 2) + Math.pow(Cy[i] - hy, 2));
				if (t + nt >= Ct[i])
					return false;
			}

		for (int i = 0; i < Cs.length; i++)
			if (Cs[i]) {
				double nt = Math.sqrt(Math.pow(Cx[i] - hx, 2) + Math.pow(Cy[i] - hy, 2));
				Cs[i] = false;
				if (result(t + nt, n - 1, Cx[i], Cy[i]))
					return true;
				Cs[i] = true;
			}

		return false;
	}

	public void add(int i, int cx, int cy) {
		Cx[i] = cx;
		Cy[i] = cy;
		Cs[i] = true;
		Ct[i] = Math.sqrt(Math.pow(cx - Dx, 2) + Math.pow(cy - Dy, 2));
	}
}
