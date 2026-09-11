import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
 
 
public class Main{
	public static final long B = 1000000007L;
	
	//library
	/*
	public static long modmul(long a, long b, long m){
		long ans =  (((a * (b >> 20) % m) << 20) + a * (b & ((1 << 20) - 1))) % m;
		if(ans < 0){
			return ans + m;
		}else{
			return ans;
		}
	}
	*/
	public static long inv(long a, long p){
		return (a == 1 ? 1 : (1 - p*inv(p%a, a)) / a + p);
	}
	
    public static void main(String[] args) throws IOException {
    	Scanner sc = new Scanner(System.in);
    	
    	final int n = sc.nextInt();
    	final int m = sc.nextInt();
    	
    	String str = sc.next();
    	char[] input = str.toCharArray();
    	
    	HashSet<Long> set = new HashSet<Long>();
    	
    	long[] pow = new long[n + 1];
    	pow[0] = 1L;
    	for(int i = 1; i <= n; i++){
    		pow[i] = (pow[i - 1] * B);
    	}
    	
    	long[] hash_p = new long[n + 1];
    	hash_p[0] = 0;
    	for(int i = 0; i < n; i++){
    		hash_p[i+1] = (hash_p[i] * B + (input[i] - 'a' + 1));
    	}
    	
    	//System.out.println((inv * B) % H);
    	
    	int l = 0, r = 0;
    	long hash = input[l];
    	
    	for(int i = 0; i < m; i++){
    		String in = sc.next();
    		
    		if("L++".equals(in)){
    			l++;
    		}else if("L--".equals(in)){
    			l--;
    		}else if("R++".equals(in)){
    			r++;
    		}else if("R--".equals(in)){
    			r--;
    		}
    		
    		hash = hash_p[r + 1] - hash_p[l] * pow[r + 1 - l];
    		
    		if(!set.contains(hash)){
    			set.add(hash);
    			//System.out.println(str.substring(l, r + 1) + " : " + hash);
    		}else{
    			//System.out.println("dup :: " + str.substring(l, r + 1) + " : " + hash);
    		}
    	}
    	
    	System.out.println(set.size());
    	
    }
     
}
