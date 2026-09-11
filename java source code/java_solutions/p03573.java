import java.util.*;

// CODEFES_B 2017-C
// http://code-festival-2017-qualb.contest.atcoder.jp/tasks/code_festival_2017_qualb_c	

public class Main {
	
	public static void main (String[] args) throws InterruptedException {
		Scanner in = new Scanner(System.in);
		
		
	    int A = in.nextInt();
	    int B = in.nextInt();
	    int C = in.nextInt();
	    
	    if (A == B) {
	      System.out.println(C);
	    } else if (A == C) {
	      System.out.println(B);
	    } else {
	      System.out.println(A);
	    }
	}
}
