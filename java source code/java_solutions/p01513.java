import java.util.Scanner;
 
 
public class Main{
     
    public static void main(String[] args){
    	
    	final Scanner sc = new Scanner(System.in);
    	
    	while(true){
    		final int N = sc.nextInt();
    		
    		if(N == 0){
    			break;
    		}
    		
    		final int[][] knows = new int[N][];
    		
    		for(int i = 0; i < N; i++){
    			final int M = sc.nextInt();
    			
    			knows[i] = new int[M];
    			
    			for(int j = 0; j < M; j++){
    				knows[i][j] = sc.nextInt();
    			}
    			
    		}
    		
    		final int K = sc.nextInt();
    		final int[] data = new int[K];
    		
    		for(int i = 0; i < K; i++){
    			data[i] = sc.nextInt();
    		}
    		
    		int conf = -1;
    		for(int i = 0; i < N; i++){
    			boolean flag = true;
    			
    			for(int d : data){
    				boolean in_flag = false;
    				
    				for(int m : knows[i]){
    					if(d == m){
    						in_flag = true;
    						break;
    					}
    				}
    				
    				if(!in_flag){
    					flag = false;
    					break;
    				}
    			}
    			
    			if(flag){
    				if(conf == -1){
    					conf = i + 1;
    				}else{
    					conf = -1;
    					break;
    				}
    			}
    		}
    		
    		System.out.println(conf);
    		
    	}
    	
    }
     
}
