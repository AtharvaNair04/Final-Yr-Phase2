import java.util.PriorityQueue;
import java.util.Scanner;


public class Main {
	
	public static class Mover implements Comparable<Mover>{
		int time;
		int x, y;
		int dir;
		
		public Mover(int time, int x, int y, int dir) {
			super();
			this.time = time;
			this.x = x;
			this.y = y;
			this.dir = dir;
		}

		@Override
		public int compareTo(Mover arg0) {
			return this.time - arg0.time;
		}
	}
	
	public static final int MAX_TIME = 101;
	public static final int MAX_SIZE = 20;
	
	public static final int[][] move_dir = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
	public static final int[]   rev_dir  = new int[]{1, 0, 3, 2};
	public static final int MAX_DIR  = move_dir.length;
	
	public static final int INF = Integer.MAX_VALUE / 2 - 1;
	
	public static boolean is_ok(int x, int y, int w, int h){
		if(x < 0 || x >= w || y < 0 || y >= h){
			return false;
		}else{
			return true;
		}
	}
	
	public static void main(String args[]){
		Scanner sc = new Scanner(System.in);
		
		int[][][][] adj = new int[MAX_SIZE][MAX_SIZE][MAX_SIZE][MAX_SIZE];
		int[][] signals = new int[MAX_SIZE][MAX_SIZE];
		boolean[][][][] is_visited = new boolean[MAX_SIZE][MAX_SIZE][MAX_TIME][4];
		
		while(true){
			final int M = sc.nextInt();
			final int N = sc.nextInt();
			
			if(M == 0 && N == 0){
				break;
			}
			
			final int D = sc.nextInt();
			
			//init
			for(int y = 0; y < M; y++){
				for(int x = 0; x < N; x++){
					signals[y][x] = 0;
					
					for(int ty = 0; ty < M; ty++){
						for(int tx = 0; tx < N; tx++){
							adj[y][x][ty][tx] = INF;
						}
					}
						
					for(int[] move : move_dir){
						final int nx = x + move[0];
						final int ny = y + move[1];
						
						if(is_ok(nx, ny, N, M)){
							adj[y][x][ny][nx] = D;
						}
					}
					
					for(int t = 0; t < MAX_TIME; t++){
						for(int d = 0; d < MAX_DIR; d++){
							is_visited[y][x][t][d] = false;
						}
					}
				}
			}
			
			//signal
			final int ns = sc.nextInt();
			for(int i = 0; i < ns; i++){
				String[] input = sc.next().split("-");
				final int x = Integer.parseInt(input[1]) - 1;
				final int y = input[0].toCharArray()[0] - 'a';
				final int k = sc.nextInt();
				
				signals[y][x] = k;
			}
			
			//under const
			final int nc = sc.nextInt();
			for(int i = 0; i < nc; i++){
				String[] input1 = sc.next().split("-");
				final int x1 = Integer.parseInt(input1[1]) - 1;
				final int y1 = input1[0].toCharArray()[0] - 'a';
				
				String[] input2 = sc.next().split("-");
				final int x2 = Integer.parseInt(input2[1]) - 1;
				final int y2 = input2[0].toCharArray()[0] - 'a';
				
				adj[y1][x1][y2][x2] = adj[y2][x2][y1][x1] = INF;
			}
			
			//nj
			final int nj = sc.nextInt();
			for(int i = 0; i < nj; i++){
				String[] input1 = sc.next().split("-");
				final int x1 = Integer.parseInt(input1[1]) - 1;
				final int y1 = input1[0].toCharArray()[0] - 'a';
				
				String[] input2 = sc.next().split("-");
				final int x2 = Integer.parseInt(input2[1]) - 1;
				final int y2 = input2[0].toCharArray()[0] - 'a';
				
				final int d = sc.nextInt();
				
				adj[y1][x1][y2][x2] += d;
				adj[y2][x2][y1][x1] += d;
			}
			
			int s_x = 0, s_y = 0, g_x = 0, g_y = 0;
			String[] input1 = sc.next().split("-");
			s_x = Integer.parseInt(input1[1]) - 1;
			s_y= input1[0].toCharArray()[0] - 'a';
			String[] input2 = sc.next().split("-");
			g_x = Integer.parseInt(input2[1]) - 1;
			g_y = input2[0].toCharArray()[0] - 'a';
			
			PriorityQueue<Mover> queue = new PriorityQueue<Mover>();
			queue.add(new Mover(0, s_x, s_y, 0));
			
			while(!queue.isEmpty()){
				Mover mover = queue.poll();
				
				if(mover.time >= MAX_TIME){
					continue;
				}else if(is_visited[mover.y][mover.x][mover.time][mover.dir]){
					continue;
				}else{
					is_visited[mover.y][mover.x][mover.time][mover.dir] = true;
				}
				
				//System.out.println(mover.x + " " + mover.y + " " + mover.time);
				
				if(mover.x == g_x && mover.y == g_y){
					System.out.println(mover.time);
					break;
				}
				
				if(signals[mover.y][mover.x] != 0){
					final boolean use_ud = mover.dir >= 2;
					final int freq = signals[mover.y][mover.x] * 2;
					
					final int mod = mover.time % freq;
					final boolean can_ud= mod < signals[mover.y][mover.x];
					
					//System.out.println(mover.time + " " + mover.x + " " + mover.y + " " + mover.dir + " " + use_ud + " " + can_ud + " " + freq + " " + mod + " " + (signals[mover.y][mover.x] - (mover.time % signals[mover.y][mover.x])));
					
					if(use_ud != can_ud){
						continue;
					}
				}
				
				for(int dir = 0; dir < MAX_DIR; dir++){
					if(rev_dir[mover.dir] == dir){
						continue;
					}
					
					final int nx = mover.x + move_dir[dir][0];
					final int ny = mover.y + move_dir[dir][1];
					
					if(!is_ok(nx, ny, N, M)){
						continue;
					}else if(adj[mover.y][mover.x][ny][nx] >= INF){
						continue;
					}
					
					final int next_time = mover.time + adj[mover.y][mover.x][ny][nx];
					
					if(next_time >= MAX_TIME){
						continue;
					}
					
					if(is_visited[ny][nx][next_time][dir]){
						continue;
					}
					
					queue.add(new Mover(next_time, nx, ny, dir));
				}
			}
		}
	}
}
