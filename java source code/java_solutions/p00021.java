import java.util.*;
import java.io.*;

class Main {

    static final int X = 0;
    static final int Y = 1;
    
    public static void main(String[] args) {

	final Scanner stdin = new Scanner(System.in);
	final int T = stdin.nextInt();
	for ( int t = 0; t < T; t++ ) {
	    double[][] co = new double[4][2];
	    
	    for ( int i = 0; i < 4; i++ ) {
		for ( int j = 0; j < 2; j++ ) {
		    co[i][j] = stdin.nextDouble();
		}
	    }
	    
	    final double dx1 = co[1][X] - co[0][X];
	    final double dy1 = co[1][Y] - co[0][Y];
	    final double dx2 = co[3][X] - co[2][X];
	    final double dy2 = co[3][Y] - co[2][Y];

	    if ( solve( dx1, dy1, dx2, dy2 ) ) {
		System.out.println( "YES" );
	    } else {
		System.out.println( "NO" );
	    }
	}
    }
    static boolean solve ( double dx1, double dy1, double dx2, double dy2 ) {
	return dy1 * dx2 == dx1 * dy2;
    }    
}
