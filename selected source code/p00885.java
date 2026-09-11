import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {
	
	public static int dfs(final int n, int pur_bal, int cur_bal, int cur_place, int cur_time, int[] places, int[] times){
		if(pur_bal == n){
			return places[pur_bal - 1];
		}
		
		if(times[pur_bal] > cur_time){
			return -pur_bal;
		}
		
		int ret_time = dfs(n, pur_bal, 0, 0, cur_time + (cur_place * (cur_bal + 1)), places, times);
		int combo_time = dfs(n, pur_bal + 1, cur_bal + 1, places[pur_bal + 1], (Math.abs(cur_place - places[pur_bal + 1]) * (cur_bal + 2)), places, times);
		
		if(ret_time < 0 && combo_time < 0){
			return Math.min(ret_time, combo_time);
		}else if(combo_time < 0){
			return ret_time + places[pur_bal];
		}else if(ret_time < 0){
			return combo_time + Math.abs(places[pur_bal] - cur_place);
		}else{
			return Math.min(ret_time + places[pur_bal], combo_time + Math.abs(places[pur_bal] - cur_place));
		}
	}
	
	public static class Vehicle implements Comparable<Vehicle>{
		int pur_bal;
		int cur_bal;
		int cur_place;
		int remain_time;
		int sum_dist;
		
		public Vehicle(int pur_bal, int cur_bal, int cur_place, int remain_time, int sum_dist) {
			super();
			this.pur_bal = pur_bal;
			this.cur_bal = cur_bal;
			this.cur_place = cur_place;
			this.remain_time = remain_time;
			this.sum_dist = sum_dist;
		}

		@Override
		public int compareTo(Vehicle arg0) {
			return this.sum_dist - arg0.sum_dist;
		}
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		while(true){
			final int n = sc.nextInt();
			
			if(n == 0){
				break;
			}
			System.gc();
			
			int[] places = new int[n+1];
			int[] in_times = new int[n+1];
			//int[] times = new int[n+1];
			
			for(int i = 0; i < n; i++){
				places[i] = sc.nextInt();
				in_times[i] = sc.nextInt();
			}
			
			int[] times = new int[n + 1];
			times[0] = in_times[0];
			
			for(int i = 1; i < n; i++){
				times[i] = in_times[i] - in_times[i - 1];
			}
			
			times[n] = Integer.MAX_VALUE;
			places[n] = 0;
			
			//System.out.println(Arrays.toString(times));
			
			boolean[][][] is_visited = new boolean[n+1][4][50001];
			PriorityQueue<Vehicle> queue = new PriorityQueue<Main.Vehicle>();
			queue.add(new Vehicle(0, 0, 0, 0, 0));
			
			boolean flag = false;
			int progress = 0;
			while(!queue.isEmpty()){
				Vehicle vehicle = queue.poll();
				
				if(is_visited[vehicle.pur_bal][vehicle.cur_bal][vehicle.remain_time]){
					continue;
				}else{
					is_visited[vehicle.pur_bal][vehicle.cur_bal][vehicle.remain_time] = true;
				}
				
				//System.out.println(vehicle.pur_bal + " " + vehicle.cur_bal + " " + vehicle.cur_place + " " + vehicle.remain_time + " " + vehicle.sum_dist);
				
				progress = Math.max(progress, vehicle.pur_bal + 1);
				
				if(vehicle.pur_bal == n && vehicle.cur_place == 0){
					System.out.println("OK " + vehicle.sum_dist);
					flag = true;
					break;
				}
				
				if(!is_visited[vehicle.pur_bal][0][0]){
					final int next_time = vehicle.remain_time + vehicle.cur_place * (vehicle.cur_bal + 1);
					
					//System.out.println(next_time);
					
					if(times[vehicle.pur_bal] >= next_time){
						queue.add(new Vehicle(vehicle.pur_bal, 0, 0, next_time, vehicle.sum_dist + vehicle.cur_place));
					}
				}
				
				if(vehicle.pur_bal != n && vehicle.cur_bal < 3 && !is_visited[vehicle.pur_bal + 1][vehicle.cur_bal + 1][places[vehicle.pur_bal]]){
					final int next_time = vehicle.remain_time + Math.abs(vehicle.cur_place - places[vehicle.pur_bal]) * (vehicle.cur_bal + 1);
					
					//System.out.println(times[vehicle.pur_bal] + " " + next_time);
					
					if(times[vehicle.pur_bal] >= next_time){
						queue.add(new Vehicle(vehicle.pur_bal + 1, vehicle.cur_bal + 1, places[vehicle.pur_bal], 0, vehicle.sum_dist + Math.abs(vehicle.cur_place - places[vehicle.pur_bal])));
					}
				}
			}
			
			if(!flag){
				System.out.println("NG " + progress);
			}
			
			//System.out.println(dfs(n, 0, 0, 0, 0, places, times));
		}
		
	}

}
