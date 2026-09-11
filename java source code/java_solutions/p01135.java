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

		Scanner scan = new Scanner(System.in);
		// Scanner scan = new Scanner(new
		// File("D:\\UserArea\\J0124567\\Downloads\\2010-input.txt"));

		boolean first = true;
		while (scan.hasNext()) {

			int n = scan.nextInt();
			int m = scan.nextInt();
			if (n == 0 && m == 0)
				break;
			Map mp = new Map(n, m);
			for (int i = 0; i < m; i++)
				mp.addRoot(scan.nextInt(), scan.nextInt(), scan.nextInt());
			int l = scan.nextInt();
			for (int i = 0; i < l; i++)
				mp.addMail(i, scan.nextInt(), scan.nextInt(), scan.nextInt(), scan.next());
			if (!first)
				System.out.println();
			first = false;
			mp.process();
		}

		scan.close();
		System.exit(0);
	}
}

class Map {
	class Evt {
		public Evt(int tm, int fr, char r) {
			time = tm;
			pof = fr;
			cd = r;
		}

		int time;
		int pof;
		char cd;
	}

	class EvTime implements Comparator<Evt> {

		@Override
		public int compare(Evt o1, Evt o2) {
			if (o1.time > o2.time)
				return 1;
			else if (o1.time == o2.time)
				if (o1.pof > o2.pof)
					return 1;
				else if (o1.pof == o2.pof)
					return 0;
				else
					return -1;
			else
				return -1;
		}
	}

	PriorityQueue<Evt> evt = new PriorityQueue<Evt>(100, new EvTime());

	int N;
	int[] delv;
	int[][] dist;
	int[][] next;

	class Mail {
		int cur;
		int next;
		int to;
		int arrive;
		String lab;

		public Mail(int _cur, int _next, int _to, int _arrive, String lb) {
			cur = _cur;
			next = _next;
			to = _to;
			arrive = _arrive;
			lab = new String(lb);
		}

		public void deliver(int time) {
			arrive = time + dist[cur][next];
			delv[cur] = time + dist[cur][next] * 2;
			evt.add(new Evt(delv[cur], cur, 'o'));
			if (next == to) {
				cur = -1;
				next = -1;
			} else {
				cur = next;
				next = getNext(next, to);
				evt.add(new Evt(arrive, cur, 'M'));
			}
			// @@@@@@@@@@@@@@@@
			// System.out.println(cur + " " + next + " " + to + " " + arrive + "
			// will_arrive " + lab);
		}
	}

	List<Mail> mail = new ArrayList<Mail>();

	public void addMail(int id, int fr, int to, int tm, String lab) {
		mail.add(new Mail(fr, this.getNext(fr, to), to, tm, lab));
		evt.add(new Evt(tm, fr, 'I'));
	}

	public Map(int n, int m) {
		N = n;
		delv = new int[n + 1];
		dist = new int[n + 1][n + 1];
		next = new int[n + 1][n + 1];

	}

	public void process() {
		Evt e;
		while ((e = evt.poll()) != null) {
			// @@@@@@@@@@@@@@@@
			// System.out.println("event " + e.time + " " + e.pof + " " + e.cd);
			if (delv[e.pof] > e.time)
				continue;
			int minNext = Integer.MAX_VALUE;
			int minTime = Integer.MAX_VALUE;
			int mail_ID = 0;
			for (int i = 0; i < mail.size(); i++)
				if (mail.get(i).cur == e.pof && mail.get(i).arrive <= e.time && (mail.get(i).arrive < minTime
						|| (mail.get(i).arrive == minTime && mail.get(i).next < minNext))) {
					minNext = mail.get(i).next;
					minTime = mail.get(i).arrive;
					mail_ID = i;
				}

			if (minTime != Integer.MAX_VALUE) {
				mail.get(mail_ID).deliver(e.time);

				for (int i = 0; i < mail.size(); i++)
					if (i != mail_ID && mail.get(i).cur == e.pof && mail.get(i).arrive <= e.time
							&& mail.get(i).next == minNext)
						mail.get(i).deliver(e.time);
			}
		}

		this.printMail();
	}

	class mailSort implements Comparator<Mail> {

		@Override
		public int compare(Mail o1, Mail o2) {
			if (o1.arrive > o2.arrive)
				return 1;
			else if (o1.arrive == o2.arrive)
				if (o1.lab.compareTo(o2.lab) > 0)
					return 1;
				else if (o1.lab.compareTo(o2.lab) == 0)
					return 0;
				else
					return -1;
			else
				return -1;
		}
	}

	private void printMail() {
		Collections.sort(mail, new mailSort());
		for (int i = 0; i < mail.size(); i++)
			System.out.println(mail.get(i).lab + " " + mail.get(i).arrive);
	}

	public int getNext(int fr, int to) {
		if (fr == to)
			return to;
		if (next[fr][to] > 0)
			return next[fr][to];

		int min = Integer.MAX_VALUE;
		tempdist = Integer.MAX_VALUE;
		int nx = 0;
		for (int i = 1; i < next.length; i++) {
			int[] r = new int[dist.length];
			r[fr] = -1;
			if (dist[fr][i] > 0) {
				int d = this.leastDist(i, to, r, dist[fr][i]);
				if (d > 0 && d < min) {
					min = d;
					nx = i;
				}
			}
		}
		next[fr][to] = nx;
		// @@@@@@@@@@@@@@@@@@@@@@@
		// System.out.println(fr + " " + to + " by " + nx);
		return nx;
	}

	int tempdist;

	private int leastDist(int fr, int to, int[] r, int ps) {
		if (fr == to) {
			if (tempdist > ps)
				tempdist = ps;
			return ps;
		}
		if (ps > tempdist)
			return -1;
		r[fr] = -1;
		int min = Integer.MAX_VALUE;
		for (int i = 1; i < dist[fr].length; i++)
			if (dist[fr][i] > 0 && r[i] != -1) {
				int d = leastDist(i, to, r, ps + dist[fr][i]);
				if (d > 0 && d < min)
					min = d;
			}
		r[fr] = 0;
		if (min == Integer.MAX_VALUE)
			return -1;
		else
			return min;
	}

	public void addRoot(int fr, int to, int ds) {
		dist[fr][to] = ds;
		dist[to][fr] = ds;
	}
}
