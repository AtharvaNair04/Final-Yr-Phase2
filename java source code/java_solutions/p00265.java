import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		final int N = sc.nextInt();
		final int Q = sc.nextInt();
		
		int[] array = new int[N];
		for(int i = 0; i < N; i++){
			array[i] = sc.nextInt();
		}
		Arrays.sort(array);
		
		for(int i = 0; i < Q; i++){
			final int mod = sc.nextInt();
			
			int max = 0;
			for(int d = mod - 1; d <= 300000; d += mod){
				int pos = Arrays.binarySearch(array, d);
				
				if(pos < 0){
					pos = - (pos + 2);
					
					if(pos != -1){
						max = Math.max(max, array[pos] % mod);
					}else{
						continue;
					}
					
				}else{
					max = mod - 1;
					break;
				}
			}
			
			System.out.println(max);			
		}
		
		sc.close();
	}
}
