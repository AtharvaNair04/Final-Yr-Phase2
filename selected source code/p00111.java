import java.util.*;
import java.io.*;

class Main {

    public static void main(String[] args) {

	final Scanner stdin = new Scanner(System.in);
	final HashMap<String, String> encoder = new HashMap<String, String>();
	initializeEncode( encoder );
	final HashMap<String, String> decoder = new HashMap<String, String>();
	initializeDecode( decoder );

	
	while ( stdin.hasNext() ) {
	    String input = stdin.nextLine();
	    
	    // encode
	    for ( String str : encoder.keySet() ){
		input = input.replace( str, encoder.get( str ) );
	    }

	    // decode
	    final StringBuilder answer = new StringBuilder();
	    final StringBuilder cache = new StringBuilder(input);
	    boolean flag;
	    do {
		flag = false;
		for ( String key : decoder.keySet() ) {
		    if ( startWith( cache, key ) ) {
			answer.append( decoder.get( key ) );
			cache.delete( 0, key.length() );
			flag = true;
			break;
		    }
		}		
	    } while( flag );
	    System.out.println( answer.toString() );		    
	}
	
    }

    static boolean startWith( StringBuilder sb, String val ) {
	if ( sb.length() < val.length() ) {
	    return false;
	}
	for ( int i = 0; i < val.length(); i++ ) {
	    if ( sb.charAt( i ) != val.charAt( i ) ) {
		return false;
	    } 
	}
	return true;
    }

    static void initializeEncode( HashMap<String, String> map ) {
	map.put("A", "00000");
	map.put("B", "00001");
	map.put("C", "00010");
	map.put("D", "00011");
	map.put("E", "00100");
	map.put("F", "00101");
	map.put("G", "00110");
	map.put("H", "00111");
	map.put("I", "01000");
	map.put("J", "01001");
	map.put("K", "01010");
	map.put("L", "01011");
	map.put("M", "01100");
	map.put("N", "01101");
	map.put("O", "01110");
	map.put("P", "01111");
	map.put("Q", "10000");
	map.put("R", "10001");
	map.put("S", "10010");
	map.put("T", "10011");
	map.put("U", "10100");
	map.put("V", "10101");
	map.put("W", "10110");
	map.put("X", "10111");
	map.put("Y", "11000");
	map.put("Z", "11001");
	map.put(" ", "11010");
	map.put(".", "11011");
	map.put(",", "11100");
	map.put("-", "11101");
	map.put("'", "11110");
	map.put("?", "11111");
    }

    static void initializeDecode( HashMap<String, String> map ) {
	map.put("101", " ");
	map.put("000000", "'");
	map.put("000011", ",");
	map.put("10010001", "-");
	map.put("010001", ".");
	map.put("000001", "?");
	map.put("100101", "A");
	map.put("10011010", "B");
	map.put("0101", "C");
	map.put("0001", "D");
	map.put("110", "E");
	map.put("01001", "F");
	map.put("10011011", "G");
	map.put("010000", "H");
	map.put("0111", "I");
	map.put("10011000", "J");
	map.put("0110", "K");
	map.put("00100", "L");
	map.put("10011001", "M");
	map.put("10011110", "N");
	map.put("00101", "O");
	map.put("111", "P");
	map.put("10011111", "Q");
	map.put("1000", "R");
	map.put("00110", "S");
	map.put("00111", "T");
	map.put("10011100", "U");
	map.put("10011101", "V");
	map.put("000010", "W");
	map.put("10010010", "X");
	map.put("10010011", "Y");
	map.put("10010000", "Z");
    }
    
}
