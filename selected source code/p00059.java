import java.util.*;
import java.io.*;
import static java.lang.Math.*;

class Main {

    static final double eps = 1.0e-8;
    static final int X = 0;
    static final int Y = 1;
    
    public static void main( String[] args ) {

	final Scanner stdin = new Scanner( System.in );

	while ( stdin.hasNextDouble() ) {
	    final double[][] ps = new double[4][2];
	    for ( int i = 0; i < 4; i++ ){
		ps[i][X] = stdin.nextDouble();
		ps[i][Y] = stdin.nextDouble();
	    }

	    if ( intersect( ps[0][X], ps[1][X], ps[2][X], ps[3][X] ) &&
		 intersect( ps[0][Y], ps[1][Y], ps[2][Y], ps[3][Y] ) ) {
		
		System.out.println( "YES" );
	    } else {
		System.out.println( "NO" );
	    }
	}
    }

    static boolean intersect( double a1, double a2, double b1, double b2 ) {
	return min( a1, a2 ) <= max( b1, b2 ) &&
	    min( b1, b2 ) <= max( a1, a2 );
    }
}
