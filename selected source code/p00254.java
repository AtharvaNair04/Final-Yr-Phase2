import java.util.*;
import java.io.*;
import static java.lang.Math.*;

class Main {

    public static void main( final String[] args ) {

	final Scanner stdin = new Scanner( System.in );

	while ( stdin.hasNextLine() ) {

	    final String input = stdin.nextLine();
	    if ( "0000".equals( input ) ) {
		break;		
	    }
	    int[] nums = new int[4];
	    for ( int i = 0; i < 4; i++ ) {
		nums[i] = input.charAt( i ) - '0';
	    }
	    if ( sameAllNumber( nums ) ) {
		System.out.println( "NA" );
	    } else {
		int count = 0;
		while ( !is6174( nums ) ) {
		    Arrays.sort( nums );
		    final int L = nums[3] * 1000 + nums[2] * 100 + nums[1] * 10 + nums[0];
		    final int S = nums[0] * 1000 + nums[1] * 100 + nums[2] * 10 + nums[3];
		    final int diff = L - S;
		    nums[3] = diff % 10;
		    nums[2] = ( diff / 10 ) % 10;
		    nums[1] = ( diff / 100 ) % 10;
		    nums[0] = ( diff / 1000 );
		    count++;
		}
		System.out.println( count );
	    }
	}	
    }

    static boolean sameAllNumber( int[] nums ) {
	return ( nums[0] == nums[1] && nums[1] == nums[2] && nums[2] == nums[3] );
    }

    static boolean is6174( int[] nums ) {
	return ( nums[0] == 6 && nums[1] == 1 && nums[2] == 7 && nums[3] == 4 );
    }    
}
