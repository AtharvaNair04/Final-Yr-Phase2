import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {

		 Scanner scan = new Scanner(System.in);
		//Scanner scan = new Scanner(new File("D:\\UserArea\\J0124567\\Downloads\\0536-input.txt"));

		while (scan.hasNext()) {

			int n = scan.nextInt();
			if (n == 0)
				break;
			int m = scan.nextInt();
			int p = scan.nextInt();
			int q = scan.nextInt();
			int r = scan.nextInt();

			Card c = new Card(n, m);
			for (int i = 0; i < m; i++) {
				c.shuffle(scan.nextInt(), scan.nextInt(), n);
				// c.print();
			}

			System.out.println(c.count(p, q, r));
		}

		scan.close();
		System.exit(0);

	}
}

class Card {
	class Bulk {
		public Bulk(int _st, int _pcs, int _next) {
			st = _st;
			pcs = _pcs;
			next = _next;
		}

		int st;
		int pcs;
		int next;
	}

	int top;
	int empty;
	Bulk[] bu;

	public Card(int n, int m) {
		bu = new Bulk[m * 3];
		bu[0] = new Bulk(1, n, -1);
		top = 0;
		empty = 1;
	}

	public void print() {
		int i = top;
		while (true) {

			System.out.print(" {" + bu[i].st + " " + (bu[i].st + bu[i].pcs - 1) + "}");
			i = bu[i].next;
			if (i == -1)
				break;
		}
		System.out.println();
	}

	public int count(int p, int q, int r) {
		int i = top;
		int pcs = 0;
		int cto = 0;
		int cfr;
		while (true) {
			cfr = cto + 1;
			cto += bu[i].pcs;
			if (cfr > q)
				break;
			if (cto < p) {
				i = bu[i].next;
				continue;
			}
			int a, b;
			if (cfr < p)
				a = p - cfr;
			else
				a = 0;
			if (cto > q)
				b = bu[i].pcs - (cto - q) - 1;
			else
				b = bu[i].pcs - 1;
			if (bu[i].st + a <= r)
				if (bu[i].st + b <= r)
					pcs += (b - a + 1);
				else
					pcs += (r - (bu[i].st + a) + 1);

			i = bu[i].next;

		}
		return pcs;
	}

	public void shuffle(int x, int y, int n) {
		// this.print();

		int i = top;
		int cto = 0, cfr = 0;
		int xlast = 0, ylast = 0, zlast = 0;
		while (true) {
			cfr = cto + 1;
			cto += bu[i].pcs;
			if (cfr <= x && x <= cto) {
				if (x < cto) {
					this.devide(i, cto - x);
					cto = x;
				}
				xlast = i;
			}
			if (cfr <= y && y <= cto) {
				if (y < cto) {
					this.devide(i, cto - y);
					cto = y;
				}
				ylast = i;
			}

			if (bu[i].next == -1) {
				zlast = i;
				break;
			}
			i = bu[i].next;
		}
		int temp = top;
		top = bu[ylast].next;
		bu[ylast].next = temp;
		bu[zlast].next = bu[xlast].next;
		bu[xlast].next = -1;

	}

	private int devide(int i, int pcs) {
		bu[empty] = new Bulk(bu[i].st + bu[i].pcs - pcs, pcs, bu[i].next);
		bu[i].pcs -= pcs;
		bu[i].next = empty;
		return empty++;
	}

}
