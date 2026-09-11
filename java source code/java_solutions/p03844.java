import java.util.*;
public class Main{
	public static void main(String args[]) {
		Scanner sc=new Scanner(System.in);
	    int A=sc.nextInt();
	    String OP=sc.next();
	    int B=sc.nextInt();
	    int K=0;
	    switch(OP) {
	    case "+":
	        K=A+B;
	    	System.out.println(K);
	    	break;
	    case "-":
	    	K=A-B;
	    	System.out.println(K);
	    }
	    
	}
}
