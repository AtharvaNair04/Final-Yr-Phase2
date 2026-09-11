import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;


public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int a = sc.nextInt(), b = sc.nextInt(), k = sc.nextInt();
		char[][] cs = new char[a][b];
		int sx = -1, sy = -1;
		for(int i=0; i<a; i++) {
			String s = sc.next();
			if(s.indexOf('S') != -1) {
				sx = i;
				sy = s.indexOf('S');
			}
			cs[i] = s.toCharArray();
		}
		
		int[] dx = new int[]{0,0,1,-1};
		int[] dy = new int[]{1,-1,0,0};
		Queue<int[]> q = new LinkedList<int[]>();
		Queue<int[]> qq = new LinkedList<int[]>();
		q.add(new int[]{sx, sy});
		int min = get(cs, sx, sy);
		boolean[][] marked = new boolean[a][b];
		marked[sx][sy] = true;
		
		for(int j=0; j<k; j++) {
			for(int[] p : q) {
				for(int i=0; i<4; i++) {
					int newi=p[0]+dx[i];
					int newj=p[1]+dy[i];
					if(ok(cs, newi, newj) && !marked[newi][newj]) {
						marked[newi][newj] = true;
						qq.add(new int[]{newi, newj});
						int t = get(cs, newi, newj);
						if(t < min)	
							min = t;
					}
				}
			}
			q = qq;
			qq = new LinkedList<int[]>();
		}
		
		System.out.println(1 + (int)Math.ceil((0.0+min)/k));
	}

	private static int get(char[][] cs, int newi, int newj) {
		int row  = Math.min(newi, cs.length-1-newi);
		int col  = Math.min(newj, cs[0].length-1-newj);
		return Math.min(row, col);
	}

	private static boolean ok(char[][] cs, int newi, int newj) {
		if(newi<0 || newj<0 || newi>=cs.length || newj>=cs[0].length)	return false;
		if(cs[newi][newj] == '#')	return false;
		return true;
	}
}
