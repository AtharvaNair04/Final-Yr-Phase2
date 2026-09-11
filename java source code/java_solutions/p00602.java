import java.util.*;
import java.io.*;

class Main {

    public static void main(String[] args) {

	Scanner stdin = new Scanner(System.in);

	while ( stdin.hasNextInt() ) {

	    final int V = stdin.nextInt();
	    final int d = stdin.nextInt();
	    solve( V, d );
	}	
    }

    static void solve( final int V, final int d ) {

	final int[] vs = fibs( V );
	final int[] parents = new int[V];
	final int[] rank = new int[V];
	for ( int i = 0; i < V; i++ ) {
	    parents[i] = i;
	}

	for ( int i = 0; i < V; i++ ) {
	    for ( int j = i + 1; j < V; j++ ) {
		if ( Math.abs( vs[i] - vs[j] ) < d ) {
		    unite( i, j, parents, rank );
		}
	    }
	}
	
	Set<Integer> set = new HashSet<Integer>();
	for ( int i = 0; i < V; i++ ) {
	    final int parent = find( i, parents, rank );
	    if ( !set.contains( parent ) ) {
		set.add( parent );
	    }
	}
	System.out.println( set.size() );	
    }

    static int find( final int x, final int[] parents, final int[] rank ) {
	if ( parents[x] == x ) {
	    return x;
	} else {
	    parents[x] = find( parents[x], parents, rank );
	    return parents[x];
	}
    }
    
    static void unite( int x, int y, final int[] parents, final int[] rank ) {
	final int xp = find( x, parents, rank );
	final int yp = find( y, parents, rank );
	
	if ( xp == yp ) {
	    return;
	}
	
	if ( rank[xp] < rank[yp] ) {
	    parents[xp] = yp;
	} else {
	    parents[yp] = xp;
	    if ( rank[xp] == rank[yp] ) {
		rank[xp]++;
	    }			
	}
    }

    static boolean same( final int x, final int y, final int[] parents, final int[] rank ) {
	return find( x, parents, rank ) == find( y, parents, rank );
    }

    static int[] fibs( final int V ) {

	if ( V == 1 ) {
	    final int[] ar = {2};
	    return ar;
	} else {
	    final int[] vs = new int[V];
	    vs[0] = 2;
	    vs[1] = 3;
	    for ( int i = 2; i < V; i++ ) {
		vs[i] = ( vs[i - 1] + vs[i - 2] ) % 1001;
	    }
	    return vs;
	}
    }
}
