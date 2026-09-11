import java.util.*;
import java.io.*;
import static java.lang.Math.*;

class Main {

    static int toInt( int hour, int minute ) {
	return hour * 60 + minute;
    }

    public static void main( final String[] args ) {

	final Scanner stdin = new Scanner( System.in );

	while ( true ) {

	    final int n = stdin.nextInt();
	    final int p = stdin.nextInt();
	    final int q = stdin.nextInt();
	    if ( n == 0 && p == 0 && q == 0 ) {
		break;
	    }
	    final int from = toInt( p / 100, p % 100 );
	    final int to = toInt( q / 100, q % 100 );
	    boolean[][] coms = new boolean[ n ][ to - from ];

	    for ( int i = 0; i < n; i++ ) {
		final int m =  stdin.nextInt();
		for ( int j = 0; j < m; j++ ) {
		    final int mp = stdin.nextInt();
		    final int fm = toInt( mp / 100, mp % 100 );
		    final int mq = stdin.nextInt();
		    final int t = toInt( mq / 100, mq % 100 );
		    
		    Arrays.fill( coms[ i ], fm - from, t - from, true );
		}
	    }
	    //printMap( coms );
	    int count = 0;
	    int maxCount = 0;
	    for ( int i = 0; i < to - from; i++ ){
		boolean flag = false;
		for ( int j = 0; j < n; j++ ){
		    flag |= !coms[ j ][ i ];
		}

		if ( flag ) {
		    count++;
		    maxCount = max( maxCount, count );
		} else {
		    count = 0;
		}
		//System.err.println( count + " " + maxCount );
	    }
	    
	    System.out.println( maxCount );
	}	
    }

    static void printMap( boolean[][] array ) {
	for ( boolean[] ar : array ) {
	    System.err.println( Arrays.toString( ar ) );
	}
    }
}
