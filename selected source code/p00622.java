import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		while(true){
			String in = sc.next();
			
			if("-".equals(in)){
				break;
			}
			
			LinkedList<Character> red = new LinkedList<Character>();
			LinkedList<Character> green = new LinkedList<Character>();
			for(char c : in.toCharArray()){
				red.add(c);
			}
			for(char c : sc.next().toCharArray()){
				green.add(c);
			}
			LinkedList<Character> right = new LinkedList<Character>();
			char[] down = sc.next().toCharArray();
			
			char center = green.poll();
			final int size = green.size() + red.size();
			int cur_d = 0;
			for(int count = 0; count < size; count++){
				//System.out.print(center);
				
				if(cur_d < down.length && down[cur_d] == center){
					center = red.poll();
					cur_d++;
				}else{
					right.add(center);
					center = green.poll();
				}
			}
			right.add(center);
			
			for(char r : right){
				System.out.print(r);
			}
			System.out.println();
		}
		
	}

}
