import java.util.*;

class Main {
	static int[][] map;
	static int[][] cx;
	public static void main(String[] args) {
		Scanner stdIn = new Scanner(System.in);
		while (true) {
			int n = stdIn.nextInt();
			int m = stdIn.nextInt();
			if (n == 0 && m == 0){
				break;
			}	
			map = new int[n][n];
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					map[i][j] = stdIn.nextInt();
				}
			}
			cx = new int[m][m];
			for (int i = 0; i < m; i++) {
				for (int j = 0; j < m; j++) {
					cx[i][j] = stdIn.nextInt();
				}
			}
			int GX = 999999;
			int GY = 999999;
			for (int i = 0; i < 5; i++) {
				if (check()) {
					if(GY > y) {
						GX = x;
						GY = y;
					}
					else if(GY == y) {
						if(GX > x) {
							GX = x;
						}
					}
					
				}
				rotate(cx);
			}
			if(GX == 999999 && GY == 999999) {
				System.out.println("NA");
			}
			else {
				System.out.println((GX+1) + " " + (GY+1));
			}
			
		}
		
		
	}
	static void rotate(int[][] a) {
		int[][] newMap = new int[a.length][a.length];
		for(int i = 0; i < a.length; i++) {
			for(int j = 0; j < a.length; j++) {
				newMap[a.length - j - 1][i] = a[i][j];
			}
		}
		cx = newMap;
	}
	static int x;
	static int y;
	static boolean check() {
		x = -1;
		y = -1;
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map.length; j++) {
				boolean check = true;
				int lasty = -1;
				int lastx = -1;
				OUT:for(int k = 0; k < cx.length; k++) {
					for(int l = 0; l < cx.length; l++) {
						if(cx[k][l] == -1) continue;
						if(lasty == -1 && lastx == -1) {
							lasty = k;
							lastx = l;
						}
						if(i+k >= map.length || j + l >= map.length) {
							check = false;
							break OUT;
						}
						if(map[i+k][j+l] != cx[k][l]) {
							check = false;
							break OUT;
						}
					}
				}
				if(check) {
					y = i+lasty;
					x = j+lastx;
					return true;
				}
			}
		}
		return false;
	}
}
