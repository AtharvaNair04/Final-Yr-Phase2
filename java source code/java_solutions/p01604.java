import java.util.*;
import java.io.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.lang.Math.*;

public class Main {

	int INF = 1 << 28;
	//long INF = 1L << 62;
	double EPS = 1e-10;

	void run() {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt(); sc.nextLine();
		int[] to = new int[n+2];
		to[0] = 1;
		String[][] com = new String[n][];
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for(int i=0;i<n;i++) {
			com[i] = sc.nextLine().split(" ");
			if( com[i].length == 1 ) {
				map.put(com[i][0].substring(0, com[i][0].length()-1), i+1);
				to[i+1] = i+2;
			}
			else to[i+1] = -1;
		}
		
		for(int i=0;i<n;i++) if(to[i+1] == -1) {
			to[i+1] = map.get(com[i][1].substring(0, com[i][1].length()-1));
		}
//		debug(to);
		PriorityQueue<P> q = new PriorityQueue<P>();
		q.add(new P(0, 0));
		int[] mem = new int[n+2]; fill(mem, INF);
		mem[0] = 0; 
		for(;;) {
			P cur = q.remove();
//			debug(cur.p, cur.c);
			if( to[cur.p] == 0 ) {
				System.out.println(cur.c);
				break;
			}
			if( mem[cur.p] != cur.c ) continue;
			if( to[cur.p] != cur.p + 1 && mem[cur.p+1] > cur.c+1 ) {
				q.add(new P(cur.p+1, cur.c+1));
				mem[cur.p+1] = cur.c+1; 
			}
			if(mem[to[cur.p]] > cur.c) {
				q.add(new P(to[cur.p], cur.c));
				mem[to[cur.p]] = cur.c;
			}
		}
	}
	
	class P implements Comparable<P>{
		int p, c;
		P(int p, int c) {
			this.p = p;
			this.c = c;
		}
		@Override
		public int compareTo(P arg0) {
			// TODO 自動生成されたメソッド・スタブ
			return c-arg0.c;
		}
	}

	void debug(Object... os) {
		System.err.println(Arrays.deepToString(os));
	}

	public static void main(String[] args) {
		new Main().run();
	}
}
