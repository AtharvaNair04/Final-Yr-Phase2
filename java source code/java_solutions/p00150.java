import java.util.*;
import java.io.*;

class Main {

    public static int[] sieve( int max ) {
	boolean[] prime = new boolean[max + 1];
	Arrays.fill( prime, true );
	final int lim = ( int )Math.ceil( Math.sqrt( max ) );
	prime[0] = prime[1] = false;
	int index = 2;
	while ( index <= lim ) {
	    for ( int i = 2 * index; i <= max; i += index ){
		prime[i] = false;
	    }
	    while ( !prime[++index] && index <= lim );
	}
	final int count = count( prime, true );
	final int[] answer = new int[count];
	index = 0;
	for (int i = 0; i < prime.length; i++ ) {
	    if ( prime[i] ) {
		answer[index] = i;
		index++;
	    }
	}
	return answer;
    }

    private static int count( boolean[] array, boolean val ) {
	int count = 0;
	for ( boolean b : array ) {
	    if ( val == b ) {
		count++;
	    }
	}
	return count;
    }
    
    public static void main( String[] args ) {

	final int[] primes = sieve( 10000 );
	final Scanner stdin = new Scanner( System.in );

	while ( stdin.hasNext() ) {
	    final int n = stdin.nextInt();
	    if ( n == 0 ) {
		break;
	    }

	    final int insertPoint = Arrays.binarySearch( primes, n );
	    int index = insertPoint >= 0 ? insertPoint : (- insertPoint - 1);
	    index = Math.max( 0, Math.min( primes.length - 1, index ) );
	    while ( index >= 0 && primes[index] > n ) {
		index--;
	    }
	    while (index > 0 && primes[index] - primes[index - 1] != 2) {
		index--;
	    }
	    System.out.println(primes[index - 1] + " " + primes[index]);
	}
	
    }
    
}
