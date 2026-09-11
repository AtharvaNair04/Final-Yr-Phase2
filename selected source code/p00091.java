import java.util.*;
import java.io.*;
import static java.lang.Math.*;

class Drop {

    static final int SMALL = 1;
    static final int MIDDLE = 2;
    static final int LARGE = 3;
    
    final int x;
    final int y;
    final int size;

    Drop ( final int x, final int y, final int size ) {
	this.x = x;
	this.y = y;
	this.size = size;
    }

    public String toString() {
	return String.format( "%d %d %d", x, y, size );
    }
}

class Main {
    
    public static void main( final String[] args ) {

	final Scanner stdin = new Scanner( System.in );

	final int n = stdin.nextInt();
	final int[][] map = new int[10][10];
	for ( int i = 0; i < 10; i++ ) {
	    for ( int j = 0; j < 10; j++ ) {
		map[i][j] = stdin.nextInt();
	    }		
	}

	Stack<Drop> answer = new Stack<Drop>();
	dfs( map, answer, n );

	for ( Drop d : answer ) {
	    System.out.println( d );
	}
    }

    static boolean hasNoDrop( final int[][] map ){
	for ( int[] rows : map ) {
	    for ( int val : rows ) {
		if ( val != 0 ) {
		    return false;
		}
	    }
	}
	return true;
    }

    static boolean finish( int[][] map, Stack<Drop> answer, final int noOfDrop ) {
	return ( noOfDrop == answer.size() && hasNoDrop( map ) );
    }

    static void printMap( int[][] map ) {
	for ( int[] rows : map ) {
	    System.err.println( Arrays.toString( rows ) );
	}
    }

    static void dfs( int[][] map, Stack<Drop> answer, final int noOfDrop ) {

	if ( noOfDrop == answer.size() ) {
	    return;
	}

	for ( int y = 0; y <= 9; y++ ) {
	    for ( int x = 0; x <= 9; x++ ) {
		if ( map[y][x] > 0 ) {
		    boolean putOnce = false;
		    
		    if ( canPutLarge( map, x, y ) ) {
			putOnce = true;
			addLarge( map, x, y, -1 );
			answer.push( new Drop( x, y + 2, Drop.LARGE ) );
			//System.err.printf( "PUT LARGE @ (%d %d)\n", x, y );
			dfs( map, answer, noOfDrop );
			if ( finish( map, answer, noOfDrop ) ) {
			    return;
			}
			addLarge( map, x, y, 1 );
			answer.pop();
		    }
		    if ( canPutMiddle( map, x, y ) ) {
			putOnce = true;
			addMiddle( map, x, y, -1 );
			answer.push( new Drop( x + 1, y + 1, Drop.MIDDLE ) );
			//System.err.printf( "PUT MIDDLE @ (%d %d)\n", x, y );
			dfs( map, answer, noOfDrop );
			if ( finish( map, answer, noOfDrop ) ) {
			    return;
			}
			addMiddle( map, x, y, 1 );
			answer.pop();
		    }
		    if ( canPutSmall( map, x, y ) ) {
			putOnce = true;
			addSmall( map, x, y, -1 );
			answer.push( new Drop( x, y + 1, Drop.SMALL ) );
			//System.err.printf( "PUT SMALL @ (%d %d)\n", x, y );
			dfs( map, answer, noOfDrop );
			if ( finish( map, answer, noOfDrop ) ) {
			    return;
			}
			addSmall( map, x, y, 1 );
			answer.pop();
		    }

		    if ( !putOnce ) {
			return;
		    }
		}
	    }
	}
    }

    static boolean canPutSmall( int[][] map, int cx, int cy ) {
	if ( 1 <= cx && cx <= 8 && cy <= 7 ) {
	    return  map[cy][cx] > 0 && map[cy + 1][cx] > 0 &&
		map[cy + 1][cx + 1] > 0 && map[cy + 1][cx - 1] > 0 && 
		map[cy + 2][cx] > 0;
	} else {
	    return false;
	}
    }
    
    static void addSmall( int[][] map, int cx, int cy, int val ) {
	map[cy][cx] += val;
	map[cy + 1][cx] += val;
	map[cy + 1][cx + 1] += val;
	map[cy + 1][cx - 1] += val;
	map[cy + 2][cx] += val;	
    }

    static boolean canPutMiddle( int[][] map, int cx, int cy ) {
	if ( cx < 8 && cy < 8 ) {
	    for ( int y = cy; y <= cy + 2; y++ ) {
		for ( int x = cx; x <= cx + 2; x++ ) {
		    if ( map[y][x] <= 0 ) {
			return false;
		    }		    
		}
	    }
	    return true;
	} else {
	    return false;
	}	    
    }
        
    static void addMiddle( int[][] map, int cx, int cy, int val ) {
	for ( int y = cy; y <= cy + 2; y++ ) {
	    for ( int x = cx; x <= cx + 2; x++ ) {
		map[y][x] += val;
	    }
	}
    }

    static boolean canPutLarge( int[][] map, int cx, int cy ) {
	if ( 2 <= cx && cx <= 7 && cy <= 5 ) {
	    return ( map[cy][cx] > 0 && map[cy + 2][cx + 2] > 0 && 
		     map[cy + 2][cx - 2] > 0 && map[cy + 4][cx] > 0 &&
		     canPutMiddle( map, cx - 1, cy + 1 ) );
	} else {
	    return false;
	}
    }    
    
    static void addLarge( int[][] map, int cx, int cy, int val ) {
	map[cy][cx] += val;
	map[cy + 2][cx - 2] += val;
	map[cy + 2][cx + 2] += val;
	map[cy + 4][cx] += val;
	addMiddle( map, cx - 1, cy + 1, val );	
    }    
}
