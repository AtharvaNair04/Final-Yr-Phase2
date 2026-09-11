import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;
import java.util.TreeSet;
 
 
public class Main{
	
    public static void main(String[] args) throws IOException {
    	Scanner sc = new Scanner(System.in);
    	
    	while(sc.hasNext()){
    		boolean inited = false;
    		boolean error = false;
    		int cur = 0;
    		int pos = 0;
    		
    		char[] input = sc.next().toCharArray();
    		
    		while(true){
    			//System.out.println(cur);
    			if(error){
    				if(input[pos] == '='){
    					break;
    				}else{
    					pos++;
    					continue;
    				}
    			}
    			
    			
    			if(input[pos] == '+'){
    				pos++;
    				
    				int sum = 0;
    				while('0' <= input[pos] && input[pos] <= '9'){
    					sum *= 10;
    					sum += input[pos] - '0';
    					pos++;
    				}
    				
    				if(sum < 0 || sum >= 10000){
    					error = true;
    				}else{
    					cur += sum;
    				}
    			}else if(input[pos] == '-'){	
    				pos++;
    				
    				int sum = 0;
    				while('0' <= input[pos] && input[pos] <= '9'){
  	  					sum *= 10;
    					sum += input[pos] - '0';
    					pos++;
        			}
    				
    				if(sum < 0 || sum >= 10000){
    					error = true;
    				}else{
    					cur -= sum;
    				}
    			}else if(input[pos] == '*'){
    				pos++;
    					
    				int sum = 0;
    				while('0' <= input[pos] && input[pos] <= '9'){
    					sum *= 10;
    					sum += input[pos] - '0';
    					pos++;
    				}
        			
    				if(sum < 0 || sum >= 10000){
    					error = true;
    				}else{
    					cur *= sum;
    				}
    			}else if(input[pos] == '='){
    				break;
    			}else{
    				int sum = 0;
    				while('0' <= input[pos] && input[pos] <= '9'){
    					sum *= 10;
    					sum += input[pos] - '0';
    					pos++;
    				}
        			
    				if(sum < 0 || sum >= 10000){
    					error = true;
    				}else{
    					cur = sum;
    				}
    			}
    			
    			if(cur < 0 || cur >= 10000){
					error = true;
				}
    		}
    		
    		if(error){
    			System.out.println("E");
    		}else{
    			System.out.println(cur);
    		}
    	}
    }
     
}
