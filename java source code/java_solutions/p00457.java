import java.util.*;
import java.io.*;
import static java.lang.Math.*;

class Main {

    public static void main( final String[] args ) {

	final Scanner stdin = new Scanner( System.in );

	while( true ) {

	    final int n = stdin.nextInt();
	    if ( n == 0 ){
		break;
	    }

	    int[] color = new int[ n ];
	    for ( int i = 0; i < n; i++ ) {
		color[ i ] = stdin.nextInt();
	    }
	    int minSize = n;
	    for ( int i = 0; i < n; i++ ) {
		int preColor = color[ i ];
		if ( preColor != 1 ) {
		    color[ i ] = 1;
		    minSize = Math.min( minSize, n - simulate( color, i ) );
		}
		if ( preColor != 2 ) {
		    color[ i ] = 2;
		    minSize = Math.min( minSize, n - simulate( color, i ) );
		}
		if ( preColor != 3 ) {
		    color[ i ] = 3;
		    minSize = Math.min( minSize, n - simulate( color, i ) );
		}
		color[ i ] = preColor;
	    }
	    System.out.println( minSize );
	}	
    }

    static int simulate( final int[] color, final int index ) {
	
	int left = index;
	int right = index;
	int number = 0;
	//System.err.printf( "%d, %s\n", index, Arrays.toString( color ) );

	while ( 0 <= left && right < color.length && color[ left ] == color[ right ] ) {
	    final int pColor = color[ left ];
	    int localCount = 0;
	    while ( 0 <= left && color[ left ] == pColor ) {
		if ( left != right ) {
		    localCount++;
		}
		left--;
	    }
	    while( right < color.length && color[ right ] == pColor ) {
		right++;
		localCount++;
	    }
	    if ( localCount >= 4 ) {
		//System.err.printf( "remove: (left, right, localCount) = (%d, %d, %d)\n", left, right, localCount );
		number += localCount;
	    }
	}
	return number;
    }    
}
