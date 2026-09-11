import java.util.Scanner;

public class Main {
	
	static int h;
	static int w;
	static int c;
	static int cnt;
	static int[][] p;
	static int[][] tmp;
	static int[][] move = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
	static int[] order;
	static boolean[][] jud;
	
	static void e() {
		for(int k=0; k<5; k++) {
			if(tmp[0][0] == order[k]) continue;
			f(0, 0, tmp[0][0], order[k]);
		}
		cnt = 0;
		jud = new boolean[h][w];
		v(0, 0);
	}
	
	static void v(int i, int j) {
		if(tmp[i][j] != c) return;
		cnt++;
		jud[i][j] = true;
		for(int k=0; k<4; k++) {
			int newi = i + move[k][0];
			int newj = j + move[k][1];
			if(0 <= newi && newi < h && 0 <= newj && newj < w && !jud[newi][newj]) {
				v(newi, newj);
			}
		}
	}
	
	static void f(int i, int j, int from, int to) {
		if(tmp[i][j] != from) return;
		tmp[i][j] = to;
		for(int k=0; k<4; k++) {
			int newi = i + move[k][0];
			int newj = j + move[k][1];
			if(0 <= newi && newi < h && 0 <= newj && newj < w) {
				f(newi, newj, from, to);
			}
		}
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while(sc.hasNext()) {
			h = sc.nextInt();
			w = sc.nextInt();
			c = sc.nextInt();
			if(h + w + c == 0) break;
			p = new int[h][w];
			tmp = new int[h][w];
			for(int i=0; i<h; i++) {
				for(int j=0; j<w; j++) {
					p[i][j] = sc.nextInt();
				}
			}
			int max = 0;
			order = new int[5];
			for(int i=1; i<=6; i++) {
				for(int j=1; j<=6; j++) {
					for(int k=1; k<=6; k++) {
						for(int l=1; l<=6; l++) {
							order[0] = i;
							order[1] = j;
							order[2] = k;
							order[3] = l;
							order[4] = c;
							for(int x=0; x<h; x++) {
								for(int y=0; y<w; y++) {
									tmp[x][y] = p[x][y];
								}
							}
							e();
							max = Math.max(max, cnt);
						}
					}
				}
			}
			System.out.println(max);
		}
	}
}
