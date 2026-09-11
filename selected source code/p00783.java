import java.util.Arrays; 
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

public class Main {
	
	public static void subsump(String in, Set<String> set){
		if(set.contains(in)){
			return;
		}
		
		//setの中に含まれている
		for(Iterator<String> iter = set.iterator(); iter.hasNext(); ){
			String set_str = iter.next();
			
			final int diff_len = set_str.length() - in.length();
			if(diff_len % 2 != 0){
				System.err.println("violate");
				return;
			}else if(diff_len >= 0){
				final int offset = diff_len / 2;
				if(set_str.startsWith(in, offset)){
					return;
				}
			}
		}
		
		//setの中に含まれている
		for(Iterator<String> iter = set.iterator(); iter.hasNext(); ){
			String set_str = iter.next();
			
			final int diff_len = in.length() - set_str.length();
			if(diff_len % 2 != 0){
				System.err.println("violate");
				return;
			}else if(diff_len >= 0){
				final int offset = diff_len / 2;
				if(in.startsWith(set_str, offset)){
					iter.remove();
				}
			}
		}
		
		set.add(in);
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		Set<String> odd  = new HashSet<String>();
		Set<String> even = new HashSet<String>();
		
		while(sc.hasNextLine()){
			String input = sc.nextLine().replaceAll("[^a-zA-Z]", "").toUpperCase();
			odd.clear();
			even.clear();
			
			final int length = input.length();
			
			for(int center = 1; center < length - 1; center++){
				{
					int low = center, high = center;
					
					while(true){
						if(low == 0 || high == length - 1){
							break;
						}else if(input.charAt(low - 1) != input.charAt(high + 1)){
							break;
						}
						
						low--;
						high++;						
					}
					
					if(low != high){
						subsump(input.substring(low, high + 1), odd);
					}
				}
				
				{
					int low = center, high = center - 1;
					
					while(true){
						if(low == 0 || high == length - 1){
							break;
						}else if(input.charAt(low - 1) != input.charAt(high + 1)){
							break;
						}
						
						low--;
						high++;						
					}
					
					if(high - low > 2){
						subsump(input.substring(low, high + 1), even);
					}
				}
			}
			
			boolean first = true;
			for(String str : odd){
				if(first){
					first = false;
					System.out.print(str);
				}else{
					System.out.print(" " + str);
				}
			}
			for(String str : even){
				if(first){
					first = false;
					System.out.print(str);
				}else{
					System.out.print(" " + str);
				}
			}
			System.out.println();
		}
		
		sc.close();
	}
}
