import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {

	public static final int MAX_WIDTH  = 80;
	public static final int MAX_HEIGHT = 1000;
	
	public static void trapezoid(final int ul_x, final int top_y, boolean[][] map, boolean[][] is_visited, TreeMap<Integer, Integer> ans){
		int ur_x = ul_x;
		
		do{
			is_visited[top_y][ur_x] = true;
			ur_x++;
			//System.out.println("t");
		}while(map[top_y][ur_x]);
		ur_x--;
		
		final int left_diff  = (ul_x != 0 && map[top_y + 1][ul_x - 1]) ? -1 : map[top_y + 1][ul_x + 1] ? 1 : 0; 
		final int right_diff =  map[top_y + 1][ur_x + 1] ? 1 : (ur_x != 0 && map[top_y + 1][ur_x - 1]) ? -1 : 0; 
		
		int bottom_y = top_y;
		int dl_x = ul_x;
		int dr_x = ur_x;
		
		//System.out.println(top_y + " " + ul_x + " " + ur_x);
		
		{
			int y = top_y;
			do{
				is_visited[y][dl_x] = true;
				y++;
				dl_x += left_diff;
				//System.out.println("l");
			}while(dl_x >= 0 && map[y][dl_x]);
			y--;
			dl_x -= left_diff;
			bottom_y = y;
		}
		
		{
			int y = top_y;
			//System.out.println(dr_x);
			do{
				is_visited[y][dr_x] = true;
				y++;
				dr_x += right_diff;
				//System.out.println(dr_x + " " + y);
			}while(dr_x >= 0 && map[y][dr_x]);
			y--;
			dr_x -= right_diff;
		}
		
		for(int x = dl_x; x <= dr_x; x++){
			is_visited[bottom_y][x] = true;
		}
		//System.out.println("t");
		
		final int up_length = ur_x - ul_x + 1;
		final int down_length = dr_x - dl_x + 1;
		final int y_length = bottom_y - top_y + 1;
		final int size = (up_length + down_length) * y_length / 2;
		
		//print(is_visited);
		
		if(!ans.containsKey(size)){
			ans.put(size, 1);
			/*
			if(size == 2){
				System.out.println(top_y + " " + bottom_y + " " + ul_x + " " + ur_x + " " + dl_x + " " + dr_x);
			}
			*/
		}else{
			ans.put(size, ans.get(size) + 1);
		}
		
		if(top_y + 2 < bottom_y){
			final int next_ul_x = left_diff  < 0 ? ul_x     : left_diff > 0 ? ul_x + 2 : (ul_x + 1);
			final int next_ur_x = right_diff < 0 ? ur_x - 2 : left_diff > 0 ? ur_x     : (ur_x - 1);
			final int next_dl_x = left_diff  < 0 ? dl_x - 2 : left_diff > 0 ? dl_x     : (dl_x + 1);
			final int next_dr_x = right_diff < 0 ? dr_x     : left_diff > 0 ? dr_x + 2 : (dr_x - 1);
			
			dfs(top_y + 1, bottom_y - 1, next_ul_x, next_ur_x, next_dl_x, next_dr_x, map, is_visited, ans);
		}
		
	}
	
	public static void print(boolean[][] is_visited){
		for(int i = 0; i < 11; i++){
			for(int j = 0; j < MAX_WIDTH; j++){
				System.out.print(is_visited[i][j] ? 'x' : ' ');
			}
			System.out.println();
		}
		System.out.println("----------------------------");
	}
	
	public static void dfs(final int top_y, final int bottom_y,
						   final int ul_x, final int ur_x, final int dl_x, final int dr_x,
						   boolean[][] map, boolean[][] is_visited,
						   TreeMap<Integer, Integer> ans){
		
		int first_x = ul_x;
		int last_x  = ur_x;
		//System.out.println(ul_x + " " + top_y);
		//System.out.println(ans);
		
		final int left_diff  = ul_x < dl_x ? 1 : ul_x > dl_x ? -1 : 0;
		final int right_diff = ur_x < dr_x ? 1 : ur_x > dr_x ? -1 : 0;
		
		for(int y = top_y; y <= bottom_y; y++){
			for(int x = first_x; x <= last_x; x++){
				//System.out.println(y + " " + x);
				
				if(is_visited[y][x]){
					continue;
				}else if(!map[y][x]){
					is_visited[y][x] = true;
				}else{
					trapezoid(x, y, map, is_visited, ans);
				}
			}
			
			first_x += left_diff;
			last_x += right_diff;
		}
	}
	
	
	
	public static void main(String[] args) {
		final Scanner sc = new Scanner(System.in);

		boolean[][] is_visited = new boolean[MAX_HEIGHT + 1][MAX_WIDTH + 1];
		boolean[][] map = new boolean[MAX_HEIGHT + 1][MAX_WIDTH + 1];
		TreeMap<Integer, Integer> ans = new TreeMap<Integer, Integer>();
		
		boolean first = true;
		while (true) {
			final int n = Integer.parseInt(sc.nextLine());
			
			if(n == 0){
				break;
			}
			
			if(!first){
				System.out.println("----------");
			}
			
			ans.clear();
			for(int i = 0; i < n; i++){
				Arrays.fill(is_visited[i], false);
				Arrays.fill(map[i], false);
			}
			
			for(int i = 0; i < n; i++){
				char[] input = sc.nextLine().toCharArray();
				
				for(int j = 0; j < input.length; j++){
					map[i][j] = input[j] == '*';
				}
			}
			/*
			for(int i = 0; i < n; i++){
				for(int j = 0; j < MAX_WIDTH; j++){
					System.out.print(map[i][j] ? '*' : ' ');
				}
				System.out.println();
			}
			*/
			dfs(0, n - 1, 0, MAX_WIDTH - 1, 0, MAX_WIDTH - 1, map, is_visited, ans);
			
			for(Entry<Integer, Integer> entry : ans.entrySet()){
				System.out.println(entry.getKey() + " " + entry.getValue());
			}
			
			if(first){
				first = false;
			}
		}
	}

}
