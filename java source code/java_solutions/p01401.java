import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {
	
	public static final int MAX = 2500;
	
	public static final int INF = Integer.MAX_VALUE / 2 - 1;
	
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		
		int[][] DP = new int[MAX][MAX];
		byte[][] pos_x = new byte[MAX][MAX];
		byte[][] pos_y = new byte[MAX][MAX];
		short[] nodes = new short[MAX];
		
		while(true){
			final byte w = sc.nextByte();
			final byte h = sc.nextByte();
			
			if(w == 0 && h == 0){
				break;
			}
			
			short max_node = 0;
			Arrays.fill(nodes, (short)(0));
			
			byte s_x = -1, s_y = -1, g_x = -1, g_y = -1;
			
			for(byte i = 0; i < h; i++){
				for(byte j = 0; j < w; j++){
					String input = sc.next();
					
					if("S".equals(input)){
						s_x = j;
						s_y = i;
					}else if("G".equals(input)){
						g_x = j;
						g_y = i;
					}else if(!".".equals(input)){
						final short number = Short.parseShort(input);
						
						pos_x[number][nodes[number]] = j;
						pos_y[number][nodes[number]] = i;
						nodes[number]++;
						
						max_node = (short) Math.max(max_node, number);
					}
				}
			}
			
			final int goal_node = max_node + 1;
			
			nodes[0] = 1;
			pos_x[0][0] = s_x;
			pos_y[0][0] = s_y;
			
			nodes[goal_node] = 1;
			pos_x[goal_node][0] = g_x;
			pos_y[goal_node][0] = g_y;
			
			
			for(int i = 1; i <= goal_node; i++){
				for(int j = 0; j < nodes[i]; j++){
					DP[i][j] = Integer.MAX_VALUE;
				}
			}
			DP[0][0] = 0;
			
			for(int cur_node = 0; cur_node < goal_node; cur_node++){
				final int next_node = cur_node + 1;
				
				for(int from = 0; from < nodes[cur_node]; from++){
					final int from_x = pos_x[cur_node][from];
					final int from_y = pos_y[cur_node][from];
					
					for(int to = 0; to < nodes[next_node]; to++){
						final int to_x = pos_x[next_node][to];
						final int to_y = pos_y[next_node][to];
						
						int cost = Math.abs(from_x - to_x) + Math.abs(from_y - to_y);
						
						DP[next_node][to] = Math.min(DP[next_node][to], DP[cur_node][from] + cost);
					}
				}
			}
			
			System.out.println(DP[goal_node][0]);
			
		}
	}

}
