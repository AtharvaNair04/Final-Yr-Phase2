import java.util.*;
import java.io.*;
import static java.lang.Math.*;

class Main {

    public static void main( final String[] args ) {

	final Scanner stdin = new Scanner( System.in );

	while ( true ) {
	    final int n = stdin.nextInt();
	    final int k = stdin.nextInt();

	    if ( n == 0 && k == 0 ) {
		break;
	    }
	    
	    final Queue<Integer> q = new LinkedList<Integer>();
	    int maxVal = Integer.MIN_VALUE;
	    int sum = 0;
	    for ( int i = 0; i < k; i++ ) {
		final int elem = stdin.nextInt();
		q.offer( elem );
		sum += elem;
	    }
	    maxVal = max( maxVal, sum );
	    for ( int i = k; i < n; i++ ) {
		final int elem = stdin.nextInt();
		sum -= q.poll();
		sum += elem;
		q.offer( elem );
		maxVal = max( maxVal, sum );
	    }
	    System.out.println( maxVal );
	}	
    }    
}
