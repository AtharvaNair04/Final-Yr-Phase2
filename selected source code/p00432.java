import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {

		Scanner scan = new Scanner(System.in);
		// Scanner scan = new Scanner(new File("c:\\0509-input.txt"));

		while (true) {
			int n = scan.nextInt();
			int r = scan.nextInt();
			if (n == 0 && r == 0)
				break;

			int[] x1 = new int[n];
			int[] y1 = new int[n];
			int[] x2 = new int[n];
			int[] y2 = new int[n];
			for (int i = 0; i < n; i++) {
				x1[i] = scan.nextInt();
				y1[i] = scan.nextInt();
				x2[i] = scan.nextInt();
				y2[i] = scan.nextInt();
			}
			Rects rect = new Rects();
			for (int i = 0; i < n; i++) {
				rect.setRect(x1[i], y1[i], x2[i], y2[i]);
			}
			System.out.println(rect.area());
			if (r == 1)
				continue;
			long side = rect.side();

			rect = null;
			rect = new Rects();
			for (int i = 0; i < n; i++) {
				rect.setRect(y1[i], x1[i], y2[i], x2[i]);
			}
			side += rect.side();
			System.out.println(side);

			// rect.debug();
		}

		scan.close();
		System.exit(0);
	}
}

class Vbar {
	public int y1;
	public int y2;

	public Vbar(int _y1, int _y2) {
		y1 = _y1;
		y2 = _y2;
	}

	public long getLength() {
		return y2 - y1;
	}
}

class VbarList {
	List<Vbar> bar = new ArrayList<Vbar>();

	public int getSide() {
		return bar.size() * 2;
	}

	public int getLength() {
		int len = 0;
		for (int i = 0; i < bar.size(); i++)
			len += bar.get(i).getLength();
		return len;
	}

	public void setRect(int y1, int y2) {
		for (int i = 0; i < bar.size(); i++) {
			if (y2 < bar.get(i).y1) {
				bar.add(i, new Vbar(y1, y2));
				return;
			}
			if (y1 <= bar.get(i).y2) {
				bar.get(i).y1 = Math.min(bar.get(i).y1, y1);
				bar.get(i).y2 = Math.max(bar.get(i).y2, y2);
				this.margeRect(i);
				return;
			}
		}
		bar.add(new Vbar(y1, y2));
	}

	private void margeRect(int st) {
		int i = st + 1;
		while (i < bar.size()) {
			if (bar.get(st).y2 < bar.get(i).y1)
				break;
			bar.get(st).y2 = Math.max(bar.get(st).y2, bar.get(i).y2);
			bar.remove(i);
		}
	}

	public void debug(int x) {
		for (int i = 0; i < bar.size(); i++)
			System.out.println(x + ":" + i + " " + bar.get(i).y1 + "-" + bar.get(i).y2);
	}
}

class Rects {
	private VbarList[] vb = new VbarList[10000];

	Rects() {
		for (int x = 0; x < vb.length; x++)
			vb[x] = new VbarList();
	}

	public void debug() {
		for (int x = 0; x < vb.length; x++)
			vb[x].debug(x);
	}

	public long side() {
		long result = 0;
		for (int x = 0; x < vb.length; x++)
			result += vb[x].getSide();
		return result;
	}

	public long area() {
		long result = 0;
		for (int x = 0; x < vb.length; x++)
			result += vb[x].getLength();
		return result;
	}

	public void setRect(int x1, int y1, int x2, int y2) {
		for (int x = x1; x < x2; x++)
			vb[x].setRect(y1, y2);
	}

}
