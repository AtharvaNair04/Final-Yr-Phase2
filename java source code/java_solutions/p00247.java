import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class Main {
	
	public static int[][] move_dir = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
	
	public static int H;
	public static int W;
	public static int[][] map;
	public static int[] ices;
	
	public static class UnionFind{
		int[] par;
		UnionFind(int n){
			par=new int[n];
			for(int i=0;i<n;i++)par[i]=i;
		}
		public int find(int x){
			if(par[x]==x)return x;
			return par[x]=find(par[x]);
		}
		public Boolean same(int x,int y){
			return find(x)==find(y);
		}
		public void unite(int x,int y){
			if(find(x)==find(y))return;
			par[find(y)]=find(x);
		}
	}
	
	public static int ans = 1000;
	public static boolean[][] is_visited = new boolean[12][12];
	
	public static int dps(int x, int y, int gx, int gy, int px, int py, int time, int lim){
		if(is_visited[y][x]){
			return 0;
		}
		
		if(time >= lim){
			return 0;
		}
		
		if(time + Math.abs(x - gx) + Math.abs(y - gy) >= lim){
			return 0;
		}
		
		if(x == gx && y == gy){
			ans = time;
			return 0;
		}
		
		//check
		for(int[] move : move_dir){
			final int nx = x + move[0];
			final int ny = y + move[1];
			
			if(nx < 0 || nx >= W || ny < 0 || ny >= H){
				continue;
			}else if(px == nx && py == ny){
				continue;
			}else if(is_visited[ny][nx]){
				return 0;
			}
		}
		
		is_visited[y][x] = true;
		//moving
		for(int[] move : move_dir){
			final int nx = x + move[0];
			final int ny = y + move[1];
			
			if(nx < 0 || nx >= W || ny < 0 || ny >= H){
				continue;
			}else if(map[ny][nx] == Integer.MAX_VALUE){
				continue;
			}else if(is_visited[ny][nx]){
				continue;
			}
			
			if(map[ny][nx] > 0){
				if(ices[map[ny][nx]] > 0){
					ices[map[ny][nx]]--;
				}else{
					continue;
				}
			}
			
			dps(nx, ny, gx, gy, x, y, time + 1, lim);
			
			if(map[ny][nx] > 0){
				ices[map[ny][nx]]++;
			}
		}
		is_visited[y][x] = false;
		
		return 0;
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		while (true) {
			W = sc.nextInt();
			H = sc.nextInt();
			
			if(W == 0 && H == 0){
				break;
			}
			
			map = new int[H][W];
			int sx = -1, sy = -1;
			int gx = -1, gy = -1;
			for(int i = 0; i < H; i++){
				char[] in = sc.next().toCharArray();
				
				for(int j = 0; j < W; j++){
					if(in[j] == 'S'){
						sx = j;
						sy = i;
						map[i][j] = 0;
					}else if(in[j] == 'G'){
						gx = j;
						gy = i;
						map[i][j] = 0;
					}else if(in[j] == 'X'){
						map[i][j] = 1;
					}else if(in[j] == '#'){
						map[i][j] = Integer.MAX_VALUE;
					}else{
						map[i][j] = 0;
					}
				}
			}
			
			int number = 1;
			for(int y = 0; y < H; y++){
				for(int x = 0; x < W; x++){
					if(map[y][x] <= 0 || map[y][x] == Integer.MAX_VALUE){
						continue;
					}
					
					if(x == 0 && y == 0){
						map[y][x] = number++;
					}else if(x == 0){
						if(map[y-1][x] > 0 && map[y-1][x] != Integer.MAX_VALUE){
							map[y][x] = map[y-1][x];
						}else{
							map[y][x] = number++;
						}
					}else if(y == 0){
						if(map[y][x-1] > 0 && map[y][x-1] != Integer.MAX_VALUE){
							map[y][x] = map[y][x-1];
						}else{
							map[y][x] = number++;
						}
					}else{
						final int left = map[y][x-1] <= 0 ? Integer.MAX_VALUE : map[y][x-1];
						final int up = map[y-1][x] <= 0 ? Integer.MAX_VALUE : map[y-1][x];
						final int min = Math.min(left, up);
						
						if(min == Integer.MAX_VALUE){
							map[y][x] = number++;
						}else{
							map[y][x] = min;
						}
					}
				}
			}
			
			UnionFind uf = new UnionFind(145);
			for(int y = 0; y < H; y++){
				for(int x = 0; x < W; x++){
					if(map[y][x] <= 0 || map[y][x] == Integer.MAX_VALUE){
						continue;
					}
					
					if(x != 0 && map[y][x-1] > 0 && map[y][x-1] != Integer.MAX_VALUE){
						if(map[y][x] < map[y][x-1]){
							uf.unite(map[y][x], map[y][x-1]);
						}else{
							uf.unite(map[y][x-1], map[y][x]);
						}
					}
					
					if(y != 0 && map[y-1][x] > 0 && map[y-1][x] != Integer.MAX_VALUE){
						if(map[y][x] < map[y-1][x]){
							uf.unite(map[y][x], map[y-1][x]);
						}else{
							uf.unite(map[y-1][x], map[y][x]);
						}
					}
				}
			}
			
			for(int y = 0; y < H; y++){
				for(int x = 0; x < W; x++){
					if(map[y][x] <= 0 || map[y][x] == Integer.MAX_VALUE){
						continue;
					}
					
					map[y][x] = uf.find(map[y][x]);
				}
			}
			
			Set<Integer> hash = new HashSet<Integer>();
			for(int y = 0; y < H; y++){
				for(int x = 0; x < W; x++){
					if(map[y][x] <= 0 || map[y][x] == Integer.MAX_VALUE){
						continue;
					}
					hash.add(map[y][x]);
				}
			}
			hash.add(0);
			
			ArrayList<Integer> list = new ArrayList<Integer>(hash);
			Collections.sort(list);
			
			ices = new int[hash.size()];
			
			for(int y = 0; y < H; y++){
				for(int x = 0; x < W; x++){
					if(map[y][x] <= 0 || map[y][x] == Integer.MAX_VALUE){
						continue;
					}
					map[y][x] = Collections.binarySearch(list, map[y][x]);
					ices[map[y][x]]++;
				}
			}
			
			for(int i = 0; i < ices.length; i++){
				ices[i] /= 2;
			}
			
			/*
			for(int y = 0; y < H; y++){
				for(int x = 0; x < W; x++){
					if(y == sy && x == sx){
						System.out.printf("%s", "_S");
					}else if(y == gy && x == gx){
						System.out.printf("%s", "_G");
					}else if(map[y][x] <= 0 || map[y][x] == Integer.MAX_VALUE){
						System.out.printf("%s", map[y][x] == 0 ? "  " : "XX");
					}else{
						System.out.printf("%02d", map[y][x]);
					}
				}
				System.out.println();
			}
			System.out.println(Arrays.toString(ices));
			System.out.println("--------------------");
			*/
			
			
			
			
			for(int i = 0; i <= 144; i++){
				ans = 1000;
				for(int y = 0; y < H; y++){
					for(int x = 0; x < W; x++){
						is_visited[y][x] = false;
					}
				}
				
				dps(sx, sy, gx, gy, -1, -1, 0, i);
				
				if(ans != 1000){
					System.out.println(ans);
					break;
				}
			}
			
		}
	}

}
