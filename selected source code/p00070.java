import java.util.*;
import java.io.*;
import static java.lang.Math.*;

class Main {

    public static void main( final String[] args ) {

	final Scanner stdin = new Scanner( System.in );

	while ( stdin.hasNextInt() ) {

	    final int n = stdin.nextInt();
	    final int s = stdin.nextInt();
	    final boolean[] visited = new boolean[ 10 ];
	    
	    final int ans = dfs( n, s, visited, 0, 0 );
	    System.out.println( ans );
	}
    }

    static int dfs( final int n, final int s, final boolean[] visited, final int sum, final int depth ) {

	if ( sum == s && depth == n ) {
	    return 1;
	} else if ( sum > s || depth > n ) {
	    return 0;
	}
	int limSum = sum;
	int limNum = 9;
	for ( int i = 0; i < n - depth; i++ ) {
	    while ( limNum > 0 && visited[ limNum ] ) {
		limNum--;
	    }
	    limSum += limNum * ( n - i );
	}
	if ( limSum < s ) {
	    return 0;
	}
	
	int comb = 0;
	for ( int i = 0; i <= 9; i++ ) {
	    if ( !visited[i] ) {
		visited[i] = true;
		comb += dfs( n, s, visited, sum + ( depth + 1 ) * i, depth + 1 );
		visited[i] = false;
	    }
	}
	return comb;
    }    
}
