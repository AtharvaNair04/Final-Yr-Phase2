import java.util.*;

import static java.lang.Math.*;
import static java.util.Arrays.*;

public class Main {

	int INF = 1 << 28;
	int map[][];
	int w, h;
	LinkedList<P> s;
	LinkedList<P> t;
	void run() {
		Scanner sc = new Scanner(System.in);
		for(;;) {
			w = sc.nextInt();
			h = sc.nextInt();
			if((w|h)==0) break;
			map = new int[h][w];
			s = new LinkedList<P>();
			t = new LinkedList<P>();
			for(int i=0;i<h;i++) for(int j=0;j<w;j++) {
				char c = sc.next().charAt(0);
				if( c == 'S' ) {
					map[i][j] = 0;
					s.add(new P(j, i));
				}
				else if( c == 'T' ) {
					map[i][j] = 0;
					t.add(new P(j, i));
				}
				else if(c == 'X' )map[i][j] = -1;
				else map[i][j] = c - '0';
			}
			System.out.println(bfs());
		}
	}
	
	int bfs() {
		PriorityQueue<Pair> que = new PriorityQueue<Pair>(1, new Comp());
		for(P p: s) {
			que.add(new Pair(p, false, 0));
			que.add(new Pair(p, true, 0));
		}
		HashSet<Pair> isVisited = new HashSet<Pair>();
		for(;!que.isEmpty();) {
			Pair now = que.remove();
			P p = now.p;
			if(isGoal(p)) return now.t;
			if(isVisited.contains(now))continue;
			isVisited.add(now);
			for(int i=1;i<=3;i++) {
				for(int dy=-(3-i);dy<=(3-i);dy++) {
					int dx = now.left? i:-i;
					if(canMove(p.x+dx, p.y+dy)) {
						que.add(new Pair(new P(p.x+dx, p.y+dy),
								!now.left, 
								now.t + map[p.y+dy][p.x+dx]));
					}
				}
			}
		}
		
		
		return -1;
	}
	
	boolean isGoal(P p) {
		for(P pos: t) 
			if(p.x == pos.x && p.y == pos.y) return true;
		return false;
	}
	
	boolean canMove(int x, int y) {
		return 0 <= x && 0 <= y && x < w && y < h && map[y][x] >= 0 ;
	}

	public static void main(String[] args) {
		new Main().run();
	}
	
	class P {
		int x, y;
		P(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	class Pair{
		P p;
		boolean left;
		int t;
		Pair(P p, boolean left, int t) {
			this.p = p;
			this.t = t;
			this.left = left;
		}
		@Override
		public int hashCode() {
			// TODO ツ篠ゥツ督ョツ青カツ青ャツつウツづェツつスツδソツッツドツ・ツスツタツブ
			return p.y*60+p.x*2+(left?1:0);
		}
		public boolean equals(Object o) {
			Pair pp = (Pair) o;
			return left&pp.left && p.x == pp.p.x && p.y == pp.p.y;
		}
	}
	
	class Comp implements Comparator<Pair> {
		
		@Override
		public int compare(Pair o1, Pair o2) {
			// TODO ツ篠ゥツ督ョツ青カツ青ャツつウツづェツつスツδソツッツドツ・ツスツタツブ
			Pair p1 = (Pair) o1;
			Pair p2 = (Pair) o2;
			return p1.t-p2.t;
		}
	}
}
