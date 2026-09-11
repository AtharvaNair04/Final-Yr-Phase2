import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;
import java.util.TreeSet;
 
 
public class Main{
     
    public static void main(String[] args) throws IOException {
    	Scanner sc = new Scanner(System.in);
    	
    	boolean[] is_prime = new boolean[1000001];
    	
    	for(int i = 2; i < 1000001; i++){
    		is_prime[i] = true;
    	}
    	
    	for(int i = 2; i <= 1000; i++){
    		if(is_prime[i]){
    			for(int j = 2 * i; j < 1000001; j += i){
    				is_prime[j] = false;
    			}
    		}
    	}
    	
    	while(true){
    		final int a = sc.nextInt();
    		final int b = sc.nextInt();
    		
    		if(a == 0 && b == 0){
    			break;
    		}
    		
    		int a_sum = 0;
    		int a_max = 0;
    		for(int i = 2; i <= a; i++){
    			if(!is_prime[i]){
    				continue;
    			}else if(a % i != 0){
    				continue;
    			}
    			
    			a_sum += i;
    			a_max = i;
    		}
    		int a_key = a_max + a_max - a_sum;
    		
    		int b_sum = 0;
    		int b_max = 0;
    		for(int i = 2; i <= b; i++){
    			if(!is_prime[i]){
    				continue;
    			}else if(b % i != 0){
    				continue;
    			}
    			
    			b_sum += i;
    			b_max = i;
    		}
    		int b_key = b_max + b_max - b_sum;
    		
    		//System.out.println(a_sum + " " + a_max);
    		//System.out.println(b_sum + " " + b_max);
    		
    		if(a_key > b_key){
    			System.out.println("a");
    		}else{
    			System.out.println("b");
    		}
    	}
        
    }
     
}
