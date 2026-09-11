import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {
	
	public static int gcd(int x, int y){
		if(y == 0){
			return x;
		}else{
			return gcd(y, x % y);
		}
	}


	public static void main(String[] args) {
		final Scanner sc = new Scanner(System.in);

		while (true) {
			final int p = sc.nextInt();
			final int n = sc.nextInt();
			
			if(p == 0 && n == 0){
				break;
			}
			
			final double ans = Math.sqrt(p);
			double less = Double.MIN_VALUE;
			double more = Double.MAX_VALUE;
			int less_u = 1, less_v = 1, more_x = 1, more_y = 1;
			
			//bottom
			for(int b = 1; b <= n; b++){
				//calc
				final double b_ = b;
				
				double top = ans * b;
				
				int high_a = (int)(Math.ceil(top));
				int low_a  = (int)(top);
				
				//System.out.println(b + " " + high_a + " " + low_a);
				
				if(low_a >= 1 && low_a <= n && less < low_a / b_){
					final int gcd = gcd(low_a, b);
					
					less = low_a / b_;
					less_u = low_a / gcd;
					less_v = b / gcd;
				}
				
				if(high_a >= 1 && high_a <= n && more > high_a / b_){
					final int gcd = gcd(high_a, b);
					
					more = high_a / b_;
					more_x = high_a / gcd;
					more_y = b / gcd;
				}
			}
			
			System.out.println(more_x + "/" + more_y + " " + less_u + "/" + less_v);
			
		}
	}

}
