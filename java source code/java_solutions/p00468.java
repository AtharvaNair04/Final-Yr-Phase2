import java.util.*;
import java.io.*;
import static java.lang.Math.*;

class Main {

    public static void main( final String[] args ) {

	final Scanner stdin = new Scanner( System.in );

	while ( true ) {

	    final int n = stdin.nextInt();
	    final int m = stdin.nextInt();

	    if ( n == 0 && m == 0 ) {
		break;
	    }

	    ArrayList<ArrayList<Integer> > list = new ArrayList<ArrayList<Integer>>();
	    for ( int i = 0; i < n; i++ ) {
		list.add( new ArrayList<Integer>() );
	    }

	    for ( int i = 0; i < m; i++ ) {
		final int p1 = stdin.nextInt() - 1;
		final int p2 = stdin.nextInt() - 1;

		list.get( p1 ).add( p2 );
		list.get( p2 ).add( p1 );
	    }
	    boolean[] visited = new boolean[ n ];
	    dfs( list, 0, 0, visited );
	    int count = 0;
	    for ( int i = 1; i < visited.length; i++ ) {
		if ( visited[ i ] ) {
		    count++;
		}
	    }
	    System.out.println( count );
	}	
    }

    static void dfs( final ArrayList<ArrayList<Integer>> list, final int p, final int depth, final boolean[] visited ) {

	visited[ p ] = true;
	if ( depth == 2 ) {
	    return;
	}
	for( final int to : list.get( p ) ){
	    dfs( list, to, depth + 1, visited );
	}	
    }    
}
