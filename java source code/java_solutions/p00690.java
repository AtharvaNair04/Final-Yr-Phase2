import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;

public class Main {
	
	public static int dfs(final int ns, int cur_station, int bit, final int nl, int[][] adj_list, int[][] next){
		
		int max_next_path = Integer.MAX_VALUE;
		int max_next_station = Integer.MAX_VALUE;
		int max_cost = 0;
		
		//System.out.println(cur_station + " " + Integer.toBinaryString(bit));
		
		for(int i = 0; i < nl; i++){
			if((bit & (1 << i)) != 0){
				continue;
			}
			
			final int node1 = adj_list[i][0];
			final int node2 = adj_list[i][1];
			final int cost  = adj_list[i][2];
			
			int next_station = -1;
			if(node1 == cur_station){
				next_station = node2;
			}else if(node2 == cur_station){
				next_station = node1;
			}else{
				continue;
			}
			
			int ret = dfs(ns, next_station, bit | (1 << i), nl, adj_list, next) + cost;
			
			if(ret >= max_cost){
				if(ret > max_cost){
					max_next_path = i;
					max_cost = ret;
					max_next_station = next_station;
					//next[bit] = max_next_path;
				}else if(next_station < max_next_station){
					max_next_path = i;
					//next[bit] = max_next_path;
				}
			}
		}
		
		if(max_next_path != Integer.MAX_VALUE){
			next[cur_station][bit] = max_next_path;
		}
		
		return max_cost;
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int[][] memo_paths = new int[10][1 << 20];
		
		while (true) {
			final int ns = sc.nextInt();
			final int nl = sc.nextInt();
			
			if(ns == 0 && nl == 0){
				break;
			}
			
			int[][] adj_list = new int[nl][3];
			
			for(int i = 0; i < nl; i++){
				for(int j = 0; j < 3; j++){
					adj_list[i][j] = j < 2 ? sc.nextInt() - 1 : sc.nextInt();
				}
			}
			for(int i = 0; i < ns; i++){
				//System.out.println(ns + " " + i);
				Arrays.fill(memo_paths[i], -1);
			}
			
			int max_start = -1;
			int max_cost = 0;
			for(int start = 0; start < ns; start++){
				
				final int cost = dfs(ns, start, 0, nl, adj_list, memo_paths);
				//System.out.println(Arrays.toString(next_path));
			
				if(cost > max_cost){
					max_start = start;
					max_cost = cost;
				}
			}
			
			System.out.println(max_cost);
			boolean first = true;
			int cur_pos = max_start;
			int bit = 0;
			
			do{
				if(first){
					first = false;
					System.out.print((cur_pos + 1));
				}else{
					System.out.print(" " + (cur_pos + 1));
				}
				
				final int next = memo_paths[cur_pos][bit];
				
				//sc.next();
				
				if(next == -1){
					break;
				}else{
					cur_pos = adj_list[next][0] == cur_pos ? adj_list[next][1] : adj_list[next][0];
					bit |= 1 << next;
				}
			}while(true);
			System.out.println();
			
		}

	}

}
