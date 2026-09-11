import java.util.*;
import java.io.*;
import static java.lang.Math.*;

class Point {

    double x;
    double y;

    Point( double x, double y ) {
	this.x = x;
	this.y = y;
    }
}

class Main {

    public static void main( final String[] args ) {

	final Scanner stdin = new Scanner( System.in );

	while ( stdin.hasNextDouble() ) {
	    Point[] in = new Point[4];
	    for ( int i = 0; i < 4; i++ ) {
		in[i] = new Point( stdin.nextDouble(), stdin.nextDouble() );
	    }
	    final double deg1 = degree( in[3], in[0], in[1] );
	    final double deg2 = degree( in[3], in[1], in[2] );
	    final double deg3 = degree( in[3], in[2], in[0] );
	    if ( abs( deg1 + deg2 + deg3 - 2 * PI ) < 1.0e-8 ) {
		System.out.println( "YES" );
	    } else {
		System.out.println( "NO" );
	    }
	}	
    }

    static double degree( Point p1, Point p2, Point p3 ) {
	double d12 = distance( p1, p2 );
	double d13 = distance( p1, p3 );
	double d23 = distance( p2, p3 );

	return acos( ( d12 * d12 + d13 * d13 - d23 *d23 ) / ( 2 * d12 * d13 ) );
    }
    
    static double distance( Point p1, Point p2 ) {
	return sqrt( pow( p1.x - p2.x, 2.0 ) + pow( p1.y - p2.y, 2.0 ) );
    }
}
