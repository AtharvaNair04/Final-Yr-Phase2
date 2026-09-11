import java.util.*;
public class Main {
	int INF = 1 << 24;
	int [] vx = {0,1,0,-1};
	int [] vy = {1,0,-1,0};
	int w, h;
	
	class C implements Comparable<C>{
		int x, y, cx, cy, step;
		public C(int nowx, int nowy, int cx, int cy, int step) {
			this.x = nowx;
			this.y = nowy;
			this.cx = cx;
			this.cy = cy;
			this.step = step;
		}
		@Override
		public String toString() {
			return "C [x=" + x + ", y=" + y + ", cx=" + cx + ", cy=" + cy
					+ ", step=" + step + "]";
		}
		@Override
		public int compareTo(C o) {
			if(this.step < o.step) return -1;
			if(this.step > o.step) return 1;
			return 0;
		}
		
	}
	
	private void doit(){
		Scanner sc = new Scanner(System.in);
		while(true){
			w = sc.nextInt();
			h = sc.nextInt();
			if((w|h) == 0) break;
			int [][] map = new int[h][w];
			int sx = -1, sy = -1, cx = -1, cy = -1;
			for(int i = 0; i < h; i++){
				for(int j = 0; j < w; j++){
					int input = sc.nextInt();
					if(input == 4){
						map[i][j] = 0;
						sx = j;
						sy = i;
					}
					else if(input == 2){
						map[i][j] = 0;
						cx = j;
						cy = i;
					}
					else{
						map[i][j] = input;
					}
				}
			}
			
			PriorityQueue<C> open = new PriorityQueue<C>();
			open.add(new C(sx, sy, cx, cy, 0));
			boolean [][][][] close = new boolean[h][w][h][w];
			close[sy][sx][cy][cx] = true;
			int ans = INF;
			
			while(! open.isEmpty()){
				C now = open.poll();
				
				for(int i = 0; i < 4; i++){
					int nextx = now.x + vx[i];
					int nexty = now.y + vy[i];
					if(isOUT(nextx, nexty)) continue;
					if(map[nexty][nextx] == 1) continue;
					
					if(nexty == now.cy && nextx == now.cx){
						int xx = nextx + vx[i];
						int yy = nexty + vy[i];
						if(isOUT(xx, yy)) continue;
						if(map[yy][xx] == 1) continue;
						
						if(map[yy][xx] == 3){
							ans = now.step + 1;
							continue;
						}
						if(close[nexty][nextx][yy][xx]) continue;
						close[nexty][nextx][yy][xx] = true;
						C next = new C(nextx, nexty, xx, yy, now.step + 1);
						open.add(next);
					}
					else{
						int xx = now.cx;
						int yy = now.cy;
						if(close[nexty][nextx][yy][xx]) continue;
						close[nexty][nextx][yy][xx] = true;
						C next = new C(nextx, nexty, now.cx, now.cy, now.step);
						open.add(next);
					}
				}
			}
			System.out.println(ans == INF ? -1 : ans);
		}
	}
	
	private boolean isOUT(int nextx, int nexty) {
		if(0 <= nextx && nextx < w && 0 <= nexty && nexty < h){
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		Main obj = new Main();
		obj.doit();
	}
}
