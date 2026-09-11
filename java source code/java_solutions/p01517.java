import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeSet;

import javax.xml.bind.ParseConversionEvent;

public class Main {

	public static int dfs(int cur, final int N, int[] powers, boolean[][] adj, long block, ArrayList<HashMap<Long, Integer>> memo){
		//System.out.println(Long.toBinaryString(block >> cur) + " " + cur);
		if(memo.get(cur).containsKey(block >> cur)){
			return memo.get(cur).get(block >> cur);
		}
		//System.out.println("=> " + Long.toBinaryString(block >> cur) + " " + cur);
		
		
		long next_block = block;
		
		int ret = 0;
		for(int i = 0; i < N; i++){
			if((block & (1L << i)) == 0 && (cur == i || adj[cur][i])){
				next_block |= (1L << i);
			}
		}
		
		for(int pos = cur + 1; pos < N; pos++){
			if((next_block & (1L << pos)) == 0){
				ret = Math.max(ret, dfs(pos, N, powers, adj, next_block, memo));
			}
		}
		
		memo.get(cur).put(block >> cur, ret + powers[cur]);
		return ret + powers[cur];
	}
	
	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);

		while(true){
			final int N = sc.nextInt();
			
			if(N == 0){
				break;
			}
			
			System.gc();
			
			String[] names = new String[N];
			int[] powers = new int[N];
			int[] adjusts = new int[N];
			String[][] ad_names = new String[N][];
			
			Map<String, Integer> map = new HashMap<String, Integer>();
			
			for(int i = 0; i < N; i++){
				names[i] = sc.next();
				map.put(names[i], i);
				powers[i] = sc.nextInt();
				adjusts[i] = sc.nextInt();
				ad_names[i] = new String[adjusts[i]];
				for(int j = 0; j < adjusts[i]; j++){
					ad_names[i][j] = sc.next();
				}
			}
			
			boolean[][] adj = new boolean[N][N];
			for(int i = 0; i < N; i++){
				for(int j = 0; j < adjusts[i]; j++){
					adj[i][map.get(ad_names[i][j])] = true;
				}
			}
			
			ArrayList<HashMap<Long, Integer>> maps = new ArrayList<HashMap<Long,Integer>>();
			for(int i = 0; i < N; i++){
				maps.add(new HashMap<Long, Integer>());
			}
			
			System.out.println(dfs(0, N, powers, adj, 0L, maps));
		}

	}

}
