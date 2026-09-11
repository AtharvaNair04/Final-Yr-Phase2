import java.math.BigInteger;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
	
	public static final int MAX = 80000;
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		int[] numbers = new int[MAX];
		
		while(true){
			final String input = sc.next();
			
			if("0".equals(input)){
				break;
			}
			
			final int size = input.length();
			
			for(int i = 0; i < size; i++){
				numbers[i] = input.charAt(i) - '0';
			}
			
			int count = 0;
			int[] DP = new int[11];
			int[] next = new int[11];
			
			for(int i = 0; i < size; i++){
				Arrays.fill(next, 0);
				
				for(int mod = 0; mod < 11; mod++){
					next[(mod * 10 + numbers[i]) % 11] += DP[mod];
				}
				
				if(numbers[i] != 0){
					next[numbers[i]]++;
				}
				
				count += next[0];
				
				int[] tmp = next;
				next = DP;
				DP = tmp;
			}
			
			System.out.println(count);
		}
		
	}

}
