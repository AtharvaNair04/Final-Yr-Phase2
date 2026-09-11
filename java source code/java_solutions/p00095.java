import java.util.*;
import java.io.*;

class Main {

    public static void main(String[] args) {

	final Scanner stdin = new Scanner(System.in);
	final int n = stdin.nextInt();

	int maxNum = 0;
	int maxIndex = Integer.MAX_VALUE;
	for ( int i = 0; i < n; i++ ){
	    final int index = stdin.nextInt();
	    final int num = stdin.nextInt();
	    if ( num > maxNum || ( num == maxNum && index < maxIndex ) ) {
		maxNum = num;
		maxIndex = index;
	    }
	}

	System.out.printf( "%d %d\n", maxIndex, maxNum );
    }    
}
