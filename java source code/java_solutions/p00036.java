import java.util.*;
import java.io.*;

class Point {

    final int x;
    final int y;

    Point( int x, int y ) {
	this.x = x;
	this.y = y;
    }    
}

class Main {

    public static void main(String[] args) {

	final Scanner stdin = new Scanner(System.in);
	boolean first = true;
	
	while ( stdin.hasNextLine() ) {
	    if ( !first ) {
		stdin.nextLine();
	    }
	    
	    char[][] map = new char[8][];
	    for ( int i = 0; i < 8; i++ ) {
		map[i] = stdin.nextLine().toCharArray();
	    }

	    final Point p = getUpLeftPoint( map );

	    if ( isA( map, p ) ) {
		System.out.println( "A" );
	    } else if ( isB( map, p ) ) {
		System.out.println( "B" );
	    } else if ( isC( map, p ) ) {
		System.out.println( "C" );
	    } else if ( isD( map, p ) ) {
		System.out.println( "D" );
	    } else if ( isE( map, p ) ) {
		System.out.println( "E" );
	    } else if ( isF( map, p ) ) {
		System.out.println( "F" );
	    } else {
		System.out.println( "G" );
	    }
	    first = false;
	}
    }

    static boolean isA( char[][] map, Point p ) {
	if ( p.x < 7 && p.y < 7 ) {
	    return map[p.y][p.x] == '1' && map[p.y + 1][p.x] == '1' &&
		map[p.y][p.x + 1] == '1' && map[p.y + 1][p.x + 1] == '1';			 
	} else {
	    return false;
	}
    }
    
    static boolean isB( char[][] map, Point p ) {
	if ( p.y < 5 ) {
	    return map[p.y][p.x] == '1' && map[p.y + 1][p.x] == '1' &&
		map[p.y + 2][p.x] == '1' && map[p.y + 3][p.x] == '1';
	} else {
	    return false;
	}
    }
    
    static boolean isC( char[][] map, Point p ) {
	if ( p.x < 5 ) {
	    return map[p.y][p.x] == '1' && map[p.y][p.x + 2] == '1' &&
		map[p.y][p.x + 2] == '1' && map[p.y][p.x + 3] == '1';
	} else {
	    return false;
	}
    }
    
    static boolean isD( char[][] map, Point p ) {
	if ( p.x > 0 && p.y < 6 ) {
	    return map[p.y][p.x] == '1' && map[p.y + 1][p.x] == '1' &&
		map[p.y + 1][p.x - 1] == '1' && map[p.y + 2][p.x - 1] == '1';							     
	} else {
	    return false;
	}
    }
    
    static boolean isE( char[][] map, Point p ) {
	if ( p.x < 6 && p.y < 7 ) {
	    return map[p.y][p.x] == '1' && map[p.y][p.x + 1] == '1' &&
		map[p.y + 1][p.x + 1] == '1' && map[p.y + 1][p.x + 2] == '1';			     
	} else {
	    return false;
	}
    }
    
    static boolean isF( char[][] map, Point p ) {
	if ( p.x < 7 && p.y < 6 ) {
	    return map[p.y][p.x] == '1' && map[p.y + 1][p.x] == '1' &&
		map[p.y + 1][p.x + 1] == '1' && map[p.y + 2][p.x + 1] == '1';  
	} else {
	    return false;
	}
    }
    
    static Point getUpLeftPoint( char[][] map ) {

	for ( int c = 0; c < map.length; c++ ) {
	    for ( int r = 0; r < map[c].length; r++ ) {
		if ( map[c][r] == '1' ) {
		    return new Point( r, c );
		}
	    }
	}
	return new Point( -1, -1 );
    }    
}
