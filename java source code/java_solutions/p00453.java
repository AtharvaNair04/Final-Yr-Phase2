import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {
	
	public static class Walk implements Comparable<Walk>{
		int row, x;
		int pass;
		int denger;
				
		public Walk(int row, int x, int pass, int denger) {
			super();
			this.row = row;
			this.x = x;
			this.pass = pass;
			this.denger = denger;
		}

		@Override
		public int compareTo(Walk arg0) {
			return this.denger - arg0.denger;
		}
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		while (true) {
			final int n = sc.nextInt();
			final int m = sc.nextInt();
			
			ArrayList<Map<Integer, Integer>> list = new ArrayList<Map<Integer, Integer>>();
			
			if(n == 0 && m == 0){
				break;
			}
			
			for(int i = 0; i < n; i++){
				final int k = sc.nextInt();
				
				list.add(new HashMap<Integer, Integer>());
				
				for(int j = 0; j < k; j++){
					final int x = sc.nextInt();
					final int d = sc.nextInt();
					
					list.get(i).put(x, d);
				}
			}
			
			PriorityQueue<Walk> queue = new PriorityQueue<Main.Walk>();
			for(int start : list.get(0).keySet()){
				queue.add(new Walk(0, start, m, 0));
			}
			if(m > 0){
				for(int start : list.get(1).keySet()){
					queue.add(new Walk(1, start, m - 1, 0));
				}
			}
			boolean[][][] visited = new boolean[m+1][n][1001];
			
			while(!queue.isEmpty()){
				Walk walk = queue.poll();
				
				if(visited[walk.pass][walk.row][walk.x]){
					continue;
				}else{
					visited[walk.pass][walk.row][walk.x] = true;
				}
				
				if(walk.row == n - 1){
					System.out.println(walk.denger);
					break;
				}else if(walk.row == n - 2 && walk.pass > 0){
					System.out.println(walk.denger);
					break;
				}
				
				//System.out.println(walk.row + " " + walk.x + " " + walk.pass);
				
				final int cur_d = list.get(walk.row).get(walk.x);
				
				if(walk.row < n - 1){
					for(Map.Entry<Integer, Integer> entry : list.get(walk.row + 1).entrySet()){
						if(visited[walk.pass][walk.row + 1][entry.getKey()]){
							continue;
						}
						
						queue.add(new Walk(walk.row + 1, entry.getKey(), walk.pass, walk.denger + (cur_d + entry.getValue()) * Math.abs(walk.x - entry.getKey())));
					}
				}
				
				if(walk.pass > 0 && walk.row < n - 2){
					for(Map.Entry<Integer, Integer> entry : list.get(walk.row + 2).entrySet()){
						if(visited[walk.pass][walk.row + 2][entry.getKey()]){
							continue;
						}
						
						queue.add(new Walk(walk.row + 2, entry.getKey(), walk.pass - 1, walk.denger + (cur_d + entry.getValue()) * Math.abs(walk.x - entry.getKey())));
					}
				}
			}
			
		}
	}

}
