import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {
	
	public static final int SIZE = 15;
	public static final int ROOM_BITS = 5;
	
	public static int create_bit(int room, int bit){
		return (room << SIZE) | bit;
	}
	
	public static int get_bit(int bit){
		return bit & ((1 << SIZE) - 1);
	}
	
	public static int get_room(int bit){
		return bit >> SIZE;
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		boolean[][] adj = new boolean[SIZE][SIZE];
		int[] room_switch_bit = new int[SIZE];
		int[] prev_bit = new int[1 << (SIZE + ROOM_BITS)];

		while (true) {
			final int n = sc.nextInt();
			final int m = sc.nextInt();

			if (n == 0 && m == 0) {
				break;
			}
			//System.gc();

			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					adj[i][j] = false;
				}
			}

			for (int i = 0; i < m; i++) {
				final int s = sc.nextInt() - 1;
				final int t = sc.nextInt() - 1;

				adj[s][t] = adj[t][s] = true;
			}

			int init_bit = 0;
			for (int i = 0; i < n; i++) {
				final int bit = sc.nextInt();

				if (bit == 1) {
					init_bit |= 1 << i;
				}
			}
			// System.out.println(Integer.toBinaryString(init_bit));

			Arrays.fill(room_switch_bit, 0);
			for (int i = 0; i < n; i++) {
				final int k = sc.nextInt();

				for (int j = 0; j < k; j++) {
					final int bit = sc.nextInt() - 1;

					room_switch_bit[i] |= 1 << bit;
				}
				// System.out.println(Arrays.toString(room_switch_bit));
			}

			Arrays.fill(prev_bit, -2);
			
			LinkedList<Integer> queue = new LinkedList<Integer>();
			queue.add(create_bit(0, init_bit));

			boolean ok = false;
			boolean can_goal = false;

			while (!queue.isEmpty()) {
				final int doctor = queue.poll();
				
				final int room = get_room(doctor);
				final int bit  = get_bit(doctor);
				
				//System.out.println(room + " " + Integer.toBinaryString(bit));
				
				if (room == n - 1) {
					can_goal = true;

					if (bit == 1 << (n - 1)) {
						ok = true;
						/*
						System.out.println("You can go home in " + doctor.time
								+ " steps.");
						*/
						LinkedList<String> list = new LinkedList<String>();

						int cur_doctor = doctor;

						while (true) {
							int p_doctor = prev_bit[cur_doctor];
							
							int cur_pos = get_room(cur_doctor);
							int cur_bit = get_bit(cur_doctor);
							
							if(cur_pos == 0 && cur_bit == init_bit){
								break;
							}
							
							int p_pos = get_room(p_doctor);
							int p_bit = get_bit(p_doctor);
							
							if(p_pos == cur_pos){
								int diff_bit = cur_bit ^ p_bit;
								int pos = Integer.numberOfTrailingZeros(diff_bit);
								
								if((cur_bit & (1 << pos)) == 0){
									list.addFirst("Switch off room " + (pos + 1) + ".");
								}else{
									list.addFirst("Switch on room " + (pos + 1) + ".");
								}
							}else{
								list.addFirst("Move to room " + (cur_pos + 1) + ".");
							}
							
							
							
							cur_doctor = p_doctor;
						}

						System.out.println("You can go home in " + list.size() + " steps.");
						for (String str : list) {
							System.out.println(str);
						}

						break;
					}
				}

				for(int next = 0; next < n; next++){
					if(!adj[room][next]){
						continue;
					}else if((bit & (1 << next)) == 0){
						continue;
					}else if((bit & (1 << room)) == 0){
						continue;
					}
					//System.out.println(Integer.toBinaryString(bit));
					
					final int next_bit = create_bit(next, bit);
					
					if(prev_bit[next_bit] >= 0){
						continue;
					}else{
						prev_bit[next_bit] = doctor;
						queue.add(next_bit);
					}
				}
				
				for(int next_keta = 0; next_keta < n; next_keta++){
					if((room_switch_bit[room] & (1 << next_keta)) == 0){
						continue;
					}
					
					final int next_bit = bit ^ (1 << next_keta);
					final int next_doctor = create_bit(room, next_bit);
					
					//System.out.println(room + " " + Integer.toBinaryString(next_bit));
					
					//System.out.println(prev_bit[next_doctor]);
					if(prev_bit[next_doctor] < 0){
						prev_bit[next_doctor] = doctor;
						queue.add(next_doctor);
					}
				}
			}

			if (!ok) {
				if (can_goal) {
					System.out.println("You can not switch off all lights.");
				} else {
					System.out.println("Help me!");
				}
			}

		}
	}
}
