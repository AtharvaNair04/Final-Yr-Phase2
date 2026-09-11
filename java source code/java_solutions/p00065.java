import java.util.*;
import java.io.*;

class Main {

    public static void main( String[] args ) {

	final Scanner stdin = new Scanner(System.in);
	final Map<Integer, Integer> cur = new HashMap<Integer, Integer>();
	final Map<Integer, Integer> next = new HashMap<Integer, Integer>();
	
	while ( true ) {

	    final String line = stdin.nextLine();
	    if ( line.trim().equals( "" ) ) {
		break;
	    }
	    final String[] input = line.split( "," );
	    final int val = Integer.parseInt( input[0] );
	    if ( !cur.containsKey( val ) ) {
		cur.put( val, 1 );
	    } else {
		cur.put( val, cur.get( val ) + 1 );
	    }	    
	}

	while ( stdin.hasNextLine() ) {
	    final String[] input = stdin.nextLine().split( "," );
	    final int val = Integer.parseInt( input[0] );
	    if ( !next.containsKey( val ) ) {
		next.put( val, 1 );
	    } else {
		next.put( val, next.get( val ) + 1 );
	    }	    
	}

	final ArrayList<Integer> keyArray = new ArrayList<Integer>( next.keySet() );
	Collections.sort( keyArray );
	for ( Integer key : keyArray ) {
	    if ( cur.containsKey( key ) ) {
 		System.out.printf( "%d %d\n", key, ( cur.get( key ) + next.get( key ) ) );
	    }
	}
	
    }
    
}
