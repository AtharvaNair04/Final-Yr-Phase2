import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {
	
	public static String connect(String pre, String post){
		char[] pre_array = pre.toCharArray();
		char[] post_array = post.toCharArray();
		
		int min = Math.min(pre_array.length, post_array.length);
		
		int over = 0;
		for(int range = 0; range <= min; range++){
			boolean flag = true;
			for(int pos = 0; pos < range; pos++){
				//System.out.println(pre_array[pre_array.length - (range - pos)] + " " + post_array[pos]);
				
				if(pre_array[pre_array.length - (range - pos)]
					!= post_array[pos]){
						flag = false;
						break;
					}
			}
			
			//System.out.println(range);
			
			if(flag){
				over = Math.max(over, range);
			}
		}
		
		//System.out.println(pre+ " " + post.substring(over));
		
		return pre.concat(post.substring(over));
	}
	
	public static final int INF = -1;
	public static final int MAX = 14;
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		int[][] dp = new int[1 << MAX][MAX];
		int[][] adj = new int[MAX][MAX];
		
		while (true) {
			final int N = sc.nextInt();

			if (N == 0) {
				break;
			}
			
			ArrayList<String> array_list = new ArrayList<String>();
			
			for (int i = 0; i < N; i++) {
				array_list.add(sc.next());
			}
			
			ArrayList<String> no_dup_list = new ArrayList<String>();
			for(int i = 0; i < N; i++){
				String left = array_list.get(i);
				boolean flag = false;
				
				for(int j = 0; j < N; j++){	
					if(i == j){
						continue;
					}
					
					String right = array_list.get(j);
					
					if(right.indexOf(left) > 0){
						flag = true;
						break;
					}else if(right.indexOf(left) == 0 && no_dup_list.contains(left)){
						flag = true;
						break;
					}
				}
				
				if(!flag){
					no_dup_list.add(left);
				}
			}
			
			//System.out.println(no_dup_list);
			
			String[] array = new String[no_dup_list.size()];
			for(int i = 0; i < no_dup_list.size(); i++){
				array[i] = no_dup_list.get(i);
			}
			
			Arrays.sort(array);
			//System.out.println(Arrays.toString(array));
			
			final int n = array.length;
			int sum_length = 0;
			for(String str : array){
				sum_length += str.length();
			}
			
			for(int i = 0; i < n; i++){
				for(int j = 0; j < n; j++){
					if(i == j){
						adj[i][j] = INF;
						continue;
					}
					
					adj[i][j] = 
							array[i].length() + 
							array[j].length() - 
							connect(array[i], array[j]).length();
					
				}
			}			
			
			//for(int i = 0; i < n; i++){
			//	System.out.println(Arrays.toString(adj[i]));
			//}
			
			final int limit = 1 << n;
			
			for(int S = 0; S < limit; S++){
				for(int i = 0; i < n; i++){
					dp[S][i] = INF;
				}
			}
			
			for(int i = 0; i < n; i++){
				dp[0][i] = 0;
				dp[1 << i][i] = 0;
			}
			
			for(int S = 0; S < limit; S++){
				for(int i = 0; i < n; i++){
					if((S & (1 << i)) == 0){
						continue;
					}
					
					//System.out.println(Integer.toBinaryString(S) + " " + i + " " + dp[S][i]);		
					
					for(int j = 0; j < n; j++){
						if((S & (1 << j)) != 0){
							continue;
						}
						
						dp[S | (1 << j)][j] = Math.max(
								dp[S | (1 << j)][j],
								dp[S][i] + adj[i][j]);
					}
				}
			}
			
			int max = Integer.MIN_VALUE;
			for(int i = 0; i < n; i++){
				max = Math.max(max, dp[limit-1][i]);
			}
			
			System.out.println(sum_length - max);
		}

		sc.close();
	}

}
