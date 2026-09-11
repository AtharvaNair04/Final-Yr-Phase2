import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		final Scanner sc = new Scanner(System.in);
		
		while(true){
			final int n = sc.nextInt();
			
			if(n == 0){
				break;
			}
			
			int[] arrived = new int[3];
			int[] accepted = new int[3];
			
			for(int i = 0; i < n; i++){
				String[] time_str = sc.next().split(":");
				
				int hour = Integer.parseInt(time_str[0]);
				int min = Integer.parseInt(time_str[1]);
				int min2 = sc.nextInt();
				
				int type = -1;
				if(hour >= 11 && hour < 15){
					type = 0;
				}else if(hour >= 18 && hour < 21){
					type = 1;
				}else if((hour >= 21 && hour <= 24) || (hour >= 0 && hour < 2)){
					type = 2;
				}
				
				if(type == -1){
					continue;
				}
				
				arrived[type]++;
				
				if(min > min2){
					min2 += 60;
				}
				
				if(min2 - min <= 8){
					accepted[type]++;
				}
			}
			
			String[] str = {"lunch", "dinner", "midnight"};
			
			for(int i = 0; i < 3; i++){
				System.out.println(str[i] + " " + (arrived[i] == 0 ? "no guest" : ((accepted[i] * 100) / arrived[i]) + ""));
			}
		
		}
	}

}
