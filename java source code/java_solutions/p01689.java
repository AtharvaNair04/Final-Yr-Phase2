import java.util.*;
import java.io.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.lang.Math.*;

public class Main {

	int INF = 1 << 28;
	//long INF = 1L << 62;
	double EPS = 1e-10;
	int h, w, d, n;
	boolean[][] map, map2;
	int[] r;
	int[] X, Y, s;
	int sx, sy;
	
	final String YES = "Yes", NO = "No", UNKNOWN = "Unknown", BROKEN = "Broken";
	
	void run() {
		Scanner sc = new Scanner(System.in);
		h = sc.nextInt(); w = sc.nextInt(); d = sc.nextInt(); n = sc.nextInt();
		map2 = new boolean[h+2][w+2];
		map = new boolean[h+2][w+2];
		for (int y=1;y<=h;y++) {
			String s = sc.next();
			for (int x=0;x<w;x++) {
				switch (s.charAt(x)) {
				case '#':
					map[y][x+1] = map2[y][x+1] = false;
					break;
				case 'D':
					sx = x+1; sy = y;
				default:
					map[y][x+1] = map2[y][x+1] = true;
				}
			}
		}
		
		r = new int[d];
		for (int i=0;i<d;i++) r[i] = sc.nextInt();
		
		X = new int[n]; Y = new int[n]; s = new int[n];
		for (int i=0;i<n;i++) {
			X[i] = sc.nextInt() + 1; Y[i] = sc.nextInt() + 1; s[i] = sc.nextInt();
		}
//		debug(X, Y);
		System.out.println(solve());
	}
	
	String solve() {
		boolean yes = false, no = false;
		dfs(sx, sy);
		for (int x = 1;x <= w;x++) for (int y = 1; y <= h;y++) if (map2[y][x]){
			boolean f = ok(x, y);
			yes |= f & ! map[y][x];
			no  |= f & map[y][x];
			if (f) {
//				debug("ok", x, y);
			}
		}
		
		if (yes&no) return UNKNOWN;
		if (yes) return YES;
		if (no) return NO;
		return BROKEN;
		
	}
	
	int[] dx = {-1,0,1,0}, dy = {0,-1,0,1};
	void dfs(int x, int y) {
		map[y][x] = false;
		
		for (int i=0;i<4;i++) if (map[y+dy[i]][x+dx[i]]) 
			dfs(x+dx[i], y+dy[i]);
	}
	
	boolean ok(int x, int y) {
		for (int i=0;i<n;i++) {
			if (s[i] == r.length) {
//				debug("out", x, y, abs(x - X[i]), abs(y - Y[i]), r[r.length-1]);
				if (max(abs(x - X[i]), abs(y - Y[i]) ) <= r[r.length-1]) return false;
			}
			else if (s[i] == 0){
//				debug("in ", x, y, abs(x - X[i]), abs(y - Y[i]), r[s[i]]);
				if (max(abs(x - X[i]), abs(y - Y[i]) ) > r[s[i]]) return false; 
			}
			else {
				if (max(abs(x - X[i]), abs(y - Y[i]) ) > r[s[i]] || max(abs(x - X[i]), abs(y - Y[i]) ) <= r[s[i]-1]) return false; 
			}
		}
		return true;
	}

	void debug(Object... os) {
		System.err.println(Arrays.deepToString(os));
	}

	public static void main(String[] args) {
		new Main().run();
	}
}
