import java.util.*;
import java.io.*;
import static java.lang.Math.*;

class Main {

    static int argmax( final int[] array ){
	    
	int index = 0;
	int maxVal = Integer.MIN_VALUE;
	for ( int i = 0; i < array.length; i++ ) {
	    if ( maxVal < array[ i ] ) {
		index = i;
		maxVal = array[ i ];
	    }
	}
	return index;
    }
    
    static int argmin( final int[] array ){
	
	int index = 0;
	int minVal = Integer.MAX_VALUE;
	for ( int i = 0; i < array.length; i++ ) {
	    if ( minVal > array[ i ] ) {
		index = i;
		minVal = array[ i ];
	    }
	}
	return index;
    }
    
    public static void main( final String[] args ) {

	final Scanner stdin = new Scanner( System.in );

	while ( true ) {

	    final int n = stdin.nextInt();
	    if ( n == 0 ) {
		break;
	    }

	    int[][] map = new int[ n ][ n ];
	    for ( int i = 0; i < n; i++ ){
		for ( int j = 0; j < n; j++ ){
		    map[ i ][ j ] = stdin.nextInt();
		}		    
	    }

	    ArrayList<Integer> mins = new ArrayList<Integer>();
	    for ( int i = 0; i < n; i++ ){
		mins.add( map[ i ][ argmin( map[ i ] ) ] );
	    }

	    ArrayList<Integer> maxs = new ArrayList<Integer>();
	    int[] cols = new int[ n ];
	    for ( int i = 0; i < n; i++ ) {
		for ( int j = 0; j < n; j++ ) {
		    cols[ j ] = map[ j ][ i ];
		}
		maxs.add( map[ argmax( cols ) ][ i ] );
	    }

	    Collections.sort( maxs );
	    boolean success = false;
	    for ( int i = maxs.size() - 1; i >= 0; i-- ) {
		final int val = maxs.get( i );
		if ( mins.contains( val ) ) {
		    System.out.println( val );
		    success = true;
		    break;
		}
	    }
	    if ( !success ) {
		System.out.println( 0 );
	    }
	}	
    }    
}
