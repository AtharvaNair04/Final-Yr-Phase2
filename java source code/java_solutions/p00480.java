import java.util.*;
import java.io.*;
import static java.lang.Math.*;

class Main {

    public static void main( final String[] args ) {

		final Scanner stdin = new Scanner( System.in );
		int n = stdin.nextInt();
		int[] ary = new int[ n ];
		for ( int i = 0; i < n; i++ ) {
			ary[ i ] = stdin.nextInt();
		}
		long[] ans = new long[ 21 ];
		long[] before = new long[ 21 ];
		before[ ary[ 0 ] ] = 1L;
		
		for ( int i = 1; i < n - 1; i++ ) {
			Arrays.fill( ans, 0L );
			for ( int j = 0; j <= 20; j++ ) {
				final int pval = j + ary[ i ];
				if ( 0 <= pval && pval <= 20 ) {
					ans[ pval ] += before[ j ];
				}
				final int sval = j - ary[ i ];
				if ( 0 <= sval && sval <= 20 ) {
					ans[ sval ] += before[ j ];
				}
			}
			System.arraycopy( ans, 0, before, 0, ans.length );
		}
		System.out.println( ans[ ary[ n - 1 ] ] );
    }    
}
