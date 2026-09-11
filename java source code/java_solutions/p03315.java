import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
	
 	    Scanner scanner = new Scanner(System.in);
 	    String S = scanner.next();
 	    int ans = 0;
 	    
 	    for(int i=0; i<4; i++) {
 	    	if(S.substring(i,i+1).equals("+")) {
 	    		ans++;
 	    	}else {
 	    		ans--;
 	    	}
 	    }
 	    System.out.println(ans);
 	    
 	    
	}

}
