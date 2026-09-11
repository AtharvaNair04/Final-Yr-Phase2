import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
    	try(Scanner sc = new Scanner(System.in)) {
    		
    		while(true) {
    			int r0 = sc.nextInt();
    			int w0 = sc.nextInt();
    			int c  = sc.nextInt();
    			int r  = sc.nextInt();
    			if(r0 + w0 + c + r == 0)	break;
    			int ans = 0;
    			for(int i = 0; i <= Integer.MAX_VALUE; i++) {
        			if((r0 + r*i)/ c >= w0) {
        				ans = i;
        				break;
        			}
        		}
    			System.out.println(ans);
    		}
    		
    	}
    }
   
}
