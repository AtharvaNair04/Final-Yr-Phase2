import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;


public class Main {
	
	public static final int DIRS = 4;
	public static final int UP = 0;
	public static final int DOWN = 3;
	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	public static final String[] DIR_STR = {"UP", "LEFT", "RIGHT", "DOWN"};
	
	public static final int[][] move_dirs = {{0, -1}, {-1, 0}, {1, 0}, {0, 1}};
	
	public static class Walk implements Comparable<Walk> {
		int x, y;
		long cost;

		public Walk(int x, int y, long cost) {
			super();
			this.x = x;
			this.y = y;
			this.cost = cost;
		}

		@Override
		public int compareTo(Walk o) {
			if(this.cost < o.cost){
				return -1;
			}else if(this.cost > o.cost){
				return 1;
			} else{
				return 0;
			}
		}
	}
	
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		
		final int W = sc.nextInt();
		final int H = sc.nextInt();
		final int N = sc.nextInt();
		
		final int sx = sc.nextInt();
		final int sy = sc.nextInt();
		final int gx = sc.nextInt();
		final int gy = sc.nextInt();

		Map<Character, Integer> map = new HashMap<Character, Integer>();
		map.put('U', UP);
		map.put('D', DOWN);
		map.put('L', LEFT);
		map.put('R', RIGHT);		
		
		int[] tx = new int[N];
		int[] ty = new int[N];
		int[] tt = new int[N];
		int[] dt = new int[N];
		int[][] dr = new int[N][];
		int[][] dx = new int[N][];
		int[][] dy = new int[N][];
		
		for(int i = 0; i < N; i++){
			tx[i] = sc.nextInt();
			ty[i] = sc.nextInt();
			
			tt[i] = sc.nextInt();
			final char[] input = sc.next().toCharArray();

			dt[i] = input.length;
			dr[i] = new int[input.length];
			dx[i] = new int[input.length];
			dy[i] = new int[input.length];
			
			for(int j = 0; j < input.length; j++){
				dr[i][j] = map.get(input[j]);
				dx[i][j] = input[j] == 'L' ? -1 : input[j] == 'R' ? 1 : 0;
				dy[i][j] = input[j] == 'U' ? -1 : input[j] == 'D' ? 1 : 0;
			}
		}
		
		long[][][] edge_counts = new long[H + 1][W + 1][DIRS];
		for(int i = 0; i < N; i++){
			int x = tx[i], y = ty[i];
			
			for(int t = 0; t < tt[i]; t++){
				for(int d = 0; d < dt[i]; d++){
					final int dir = dr[i][d];
					final int nx = x + dx[i][d];
					final int ny = y + dy[i][d];
					
					if(nx < 0 || nx >= W || ny < 0 || ny >= H){
						continue;
					}
					
					//System.out.println(x + " " + y);
					
					switch(dir){
					case UP:
						edge_counts[y][x][RIGHT]++;
						edge_counts[y][x + 1][LEFT]++;
						break;
					case DOWN:
						edge_counts[y + 1][x][RIGHT]++;
						edge_counts[y + 1][x + 1][LEFT]++;
						break;
					case LEFT:
						edge_counts[y][x][DOWN]++;
						edge_counts[y + 1][x][UP]++;
						break;
					case RIGHT:
						edge_counts[y][x + 1][DOWN]++;
						edge_counts[y + 1][x + 1][UP]++;
						break;
					}
					
					x += dx[i][d];
					y += dy[i][d];
				}
			}
		}
		
		/*
		for(int i = 0; i <= H; i++){
			for(int j = 0; j <= W; j++){
				System.out.printf("o%d", edge_counts[i][j][RIGHT]);
			}
			System.out.println();
			if(i != H){
				for(int j = 0; j <= W; j++){
					System.out.printf("%do", edge_counts[i][j][DOWN]);
				}
				System.out.println();
			}
			
		}
		
		System.out.println();System.out.println();
		for(int i = 0; i <= H; i++){
			System.out.print("o");
			for(int j = 0; j < W; j++){
				System.out.printf("%do", edge_counts[i][j + 1][LEFT]);
			}
			System.out.printf("%d", edge_counts[i][W][RIGHT]);
			System.out.println();
			
			if(i != H){
				for(int j = 0; j <= W; j++){
					System.out.printf("%do", edge_counts[i + 1][j][UP]);
				}
				System.out.println();
			}

		}
		*/
		
		PriorityQueue<Walk> queue = new PriorityQueue<Main.Walk>();
		boolean[][] visited = new boolean[H + 1][W + 1];
		
		queue.add(new Walk(sx, sy, 0));
		while(!queue.isEmpty()){
			final Walk walk = queue.poll();
			
			if(visited[walk.y][walk.x]){
				continue;
			}else{
				visited[walk.y][walk.x] = true;
			}
			
			//System.out.println(walk.x + " " + walk.y + " " + walk.cost);
			
			if(walk.x == gx && walk.y == gy){
				System.out.println(walk.cost);
				break;
			}
			
			for(int dir = 0; dir < DIRS; dir++){
				final int[] move = move_dirs[dir];
				final int nx = walk.x + move[0];
				final int ny = walk.y + move[1];
				
				if(nx < 0 || nx >= W + 1 || ny < 0 || ny >= H + 1){
					continue;
				}else if(visited[ny][nx]){
					continue;
				}else{
					final long next_cost = walk.cost + edge_counts[walk.y][walk.x][dir];
					//System.out.println(nx + " " + ny + " "  + walk.x + " " + walk.y + " " + DIR_STR[dir] + " " + next_cost);
					queue.add(new Walk(nx, ny, next_cost));
				}
			}
		}
	}
	
}
