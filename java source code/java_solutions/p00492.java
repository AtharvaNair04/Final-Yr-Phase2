import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
	
	public static boolean is_ok(int x, int y, int H, int W){
		if(x < 0 || x >= W || y < 0 || y >= H){
			return false;
		}else{
			return true;
		}
	}
	
	public static int[][][] move_dir = new int[][][]{{{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, -1}, {1, 1}}, {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {-1, 1}, {-1, -1}}};
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		final int W = sc.nextInt();
		final int H = sc.nextInt();
		
		boolean[][] is_wall = new boolean[H + 2][W + 2];
		boolean[][] visited = new boolean[H + 2][W + 2];
		
		for(int i = 0; i < H; i++){
			for(int j = 0; j < W; j++){
				is_wall[i + 1][j + 1] = sc.nextInt() == 1;
			}
		}
		
		LinkedList<Integer> x_queue = new LinkedList<Integer>();
		LinkedList<Integer> y_queue = new LinkedList<Integer>();
		
		int wall_count = 0;
		
		x_queue.add(0);
		y_queue.add(0);
		visited[0][0] = true;
		
		//System.out.println(wall_count);
		
		while(!x_queue.isEmpty()){
			final int x = x_queue.poll();
			final int y = y_queue.poll();
			
			for(int[] move : move_dir[1 - y % 2]){
				final int nx = x + move[0];
				final int ny = y + move[1];
				
				if(!is_ok(nx, ny, H + 2, W + 2)){
					continue;
				}else if(visited[ny][nx]){
					continue;
				}else if(is_wall[ny][nx]){
					wall_count++;
					//System.out.println((x + 1) + " " + (y + 1) + " " + (nx + 1) + " " + (ny + 1));
					continue;
				}
				
				x_queue.add(nx);
				y_queue.add(ny);
				visited[ny][nx] = true; 				
			}
		}
		
		System.out.println(wall_count);
	}

}
