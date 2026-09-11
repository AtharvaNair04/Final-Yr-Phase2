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
	public static int[][] next_dir = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {0, 0}};
	
	public static int H;
	public static int W;
	public static int n;
	public static int[] start;
	public static int[] end;
	public static Set<Integer> set;
	public static List<Integer> list;
	public static int[][] map;
	public static int[] price_table;
	
	public static int[][][][] memo = new int[20][20][101][1 << 10];
	
	public static int dps(int x, int y, int bit, int time){
		boolean over = true;
		
		for(int i = 0; i < n; i++){
			if((bit & (1 << i)) == 0 && end[i] > time){
				over = false;
				break;
			}
		}
		
		if(over){
			memo[y][x][time][bit] = price_table[bit];
			return price_table[bit];
		}
		
		for(int[] move : move_dir){
			final int nx = x - move[0];
			final int ny = y - move[1];
				
			if(nx < 0 || nx >= W || ny < 0 || ny >= H){
				continue;
			}else if(map[ny][nx] < 0){
				continue;
			}else if(!set.contains(map[ny][nx])){
				continue;
			}
			
			final int pos = Collections.binarySearch(list, map[ny][nx]);
			
			if(time < start[pos]){
				continue;
			}else if(time >= end[pos]){
				continue;
			}else if((bit & (1 << pos)) != 0){
				continue;
			}
			
			//buy
			bit |= (1 << pos);
		}
		
		if(memo[y][x][time][bit] >= 0){
			return memo[y][x][time][bit];
		}
		
		//next
		int max = 0;
		for(int[] move : next_dir){
			final int nx = x - move[0];
			final int ny = y - move[1];
			
			if(nx < 0 || nx >= W || ny < 0 || ny >= H){
				continue;
			}else if(map[ny][nx] >= 0){
				continue;
			}
			
			max = Math.max(max, dps(nx, ny, bit, time + 1));
		}
		
		memo[y][x][time][bit] = max;
		return max;
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
			for(int i = 0; i < H; i++){
				for(int j = 0; j < W; j++){
					char[] in = sc.next().toCharArray();
					
					if('0' <= in[0] && in[0] <= '9'){
						map[i][j] = (in[0] - '0');
					}else if(in[0] == 'P'){
						map[i][j] = -1;
						sx = j;
						sy = i;
					}else{
						map[i][j] = -1;
					}
				}
			}
			
			n = sc.nextInt();
			
			int[] prices = new int[10];
			start = new int[10];
			end = new int[10];
			set = new HashSet<Integer>();
			list = new ArrayList<Integer>();
			int max_time = 0;
			for(int i = 0; i < n; i++){
				final int g = sc.nextInt();
				
				prices[g] = sc.nextInt();
				start[g] = sc.nextInt();
				end[g] = sc.nextInt();
				set.add(g);
				list.add(g);
				
				max_time = Math.max(max_time, end[g]);
			}
			
			price_table = new int[1 << n];
			for(int i = 0; i < 1 << n; i++){
				int p = 0;
				
				for(int b = 0; b < n; b++){
					if((i & (1 << b)) != 0){
						p += prices[b];
					}
				}
				
				price_table[i] = p;
			}
			
			for(int y = 0; y < H; y++){
				for(int x = 0; x < W; x++){
					for(int t = 0; t <= 100; t++){
						for(int b = 0; b < (1 << n); b++){
							memo[y][x][t][b] = -1;
						}
					}
				}
			}
			
			System.out.println(dps(sx, sy, 0, 0));
		}
	}

}
