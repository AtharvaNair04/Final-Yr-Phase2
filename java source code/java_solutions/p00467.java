import java.util.*;
import java.io.*;
import static java.lang.Math.*;

class Main {

    public static void main( final String[] args ) {

	final Scanner stdin = new Scanner( System.in );

	while ( true ) {
	    final int N = stdin.nextInt();
	    final int M = stdin.nextInt();
	    if ( N == 0 && M == 0 ){
		break;
	    }
	    
	    int[] map = new int[N + 1];
	    for ( int i = 1; i <= N; i++ ) {
		map[i] = stdin.nextInt();
	    }
	    
	    int[] dices = new int[M + 1];
	    for ( int i = 1; i <= M; i++ ) {
		dices[i] = stdin.nextInt();
	    }
	    
	    int p = 1;
	    for ( int i = 1; i <= M; i++ ) {
		final int dice = dices[i];
		p += dice;
		if ( p >= N ) {
		    System.out.println( i );
		    break;
		}
		p += map[p];
		if ( p >= N ) {
		    System.out.println( i );
		    break;
		}
	    }
	}
    }    
}
