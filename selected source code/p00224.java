import java.util.Arrays;
import java.util.Scanner;

public class Main {
	
	public static final int INF = Integer.MAX_VALUE / 2 - 1;
	public static final int M_MAX = 6;
	public static final int N_MAX = 100;
	public static final int MAX = M_MAX + N_MAX + 2;
	
	public static int node_num(int n, int m, String in){
		//System.out.println(in);
		
		// 0 ~ m - 1 >= cakes
		// m => home
		// m + 1 to n + m + 1 => Landmark
		// n + m + 2 => goal
		
		if("H".equals(in)){
			return m;
		}else if("D".equals(in)){
			return n + m + 1;
		}else{
			return Integer.parseInt(in.substring(1)) + (in.charAt(0) == 'L' ? m : -1);
		}
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		int[][] adj = new int[MAX][MAX];
		int[][] cal_array  = new int[MAX][1 << M_MAX];
		int[] cal_in = new int[M_MAX];
		
		while(true){
			final int m = sc.nextInt();
			final int n = sc.nextInt();
			final int k = sc.nextInt();
			final int d = sc.nextInt();
			
			if(n == 0 && m == 0 && k == 0 && d == 0){
				break;
			}
			
			Arrays.fill(cal_in, 0);
	
			for(int i = 0; i < m; i++){
				cal_in[i] = sc.nextInt();
			}
			
			final int max    = n + m + 2; 
			final int home   = m;
			final int arrive = n + m + 1;
			
			for(int i = 0; i < max; i++){
				for(int j = 0; j < max; j++){
					adj[i][j] = INF;
				}
			}
			
			for(int i = 0; i < d; i++){
				final int from = node_num(n, m, sc.next());
				final int to   = node_num(n, m, sc.next());
				
				final int dist = sc.nextInt();
				
				///System.out.println(from + " "+ to);
				
				adj[from][to] = adj[to][from] = Math.min(adj[from][to], dist * k);
			}
			
			for(int i = 0; i < max; i++){
				Arrays.fill(cal_array[i], INF);
			}
			cal_array[home][0] = 0;
			
			final int bit_max = 1 << m;
			while(true){
				boolean updated = false;
				
				//System.out.println("test");
				
				for(int node = 0; node < max; node++){
					for(int bit = 0; bit < bit_max; bit++){
						if(cal_array[node][bit] >= INF){
							continue;
						}
						
						//System.out.println(node + " " + Integer.toBinaryString(bit));
						
						for(int next = 0; next < max; next++){
							if(node == next){
								continue;
							}else if(adj[node][next] >= INF){
								continue;
							}
							
							//System.out.println(node + " " + next);
							
							int next_cost = 0;
							int next_bit  = 0;
							
							if(next < m){
								if((bit & (1 << next)) != 0){
									continue;
								}
								
								next_cost = cal_array[node][bit] + adj[node][next] - cal_in[next];
								next_bit  = bit | (1 << next);
							}else{
								next_cost = cal_array[node][bit] + adj[node][next];
								next_bit = bit;
							}
							
							//System.out.println(next + " " + node);
							
							if(cal_array[next][next_bit] > next_cost){
								cal_array[next][next_bit] = next_cost;
								updated = true;
							}
						}
					}
				}
				
				if(!updated){
					break;
				}
			}
			
			int min = Integer.MAX_VALUE;
			for(int i = 0; i < bit_max; i++){
				min = Math.min(min, cal_array[arrive][i]);
			}
			
			System.out.println(min);
		}
		
		sc.close();
	}
}
