import java.util.*;
import java.io.*;
import static java.lang.Math.*;

class Main {

    public static void main( final String[] args ) {

	final char[] inFromLst = { ' ', '\'', ',', '-', '.', '?', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

	final String[] inToLst = { "101" ,"000000" ,"000011" ,"10010001" ,"010001" ,"000001" ,"100101" ,"10011010" ,"0101" ,"0001" ,"110" ,"01001" ,"10011011" ,"010000" ,"0111" ,"10011000" ,"0110" ,"00100" ,"10011001" ,"10011110" ,"00101" ,"111" ,"10011111" ,"1000" ,"00110" ,"00111" ,"10011100" ,"10011101" ,"000010" ,"10010010" ,"10010011" ,"10010000" };

	final HashMap<Character, String> inMap = new HashMap<Character, String>();
	for ( int i = 0; i < inFromLst.length; i++ ) {
	    inMap.put( inFromLst[ i ], inToLst[ i ] );
	}

	final String[] outFromLst = { "00000", "00001", "00010", "00011", "00100", "00101", "00110", "00111", "01000", "01001", "01010", "01011", "01100", "01101", "01110", "01111", "10000", "10001", "10010", "10011", "10100", "10101", "10110", "10111", "11000", "11001", "11010", "11011", "11100", "11101", "11110", "11111" };

	final char[] outToLst = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', ' ', '.', ',', '-', '\'', '?' };

	final Map<String, Character> outMap = new HashMap<String, Character>();
	for ( int i = 0; i < outFromLst.length; i++ ) {
	    outMap.put( outFromLst[ i ], outToLst[ i ] );
	}
	final Scanner stdin = new Scanner( System.in );
	
	while ( stdin.hasNextLine() ) {

	    final String input = stdin.nextLine();
	    
	    final StringBuilder in = new StringBuilder();
	    for ( final char ch : input.toCharArray() ) {
		in.append( inMap.get( ch ) );
	    }
	    
	    for ( int i = 0; i < in.length() % 5; i++ ) {
		in.append( '0' );
	    }
	    final StringBuilder out = new StringBuilder();
	    final int no = in.length() / 5;
	    for ( int i = 0; i < no; i++ ) {
		final String token = in.substring( i * 5, ( i + 1 ) * 5 );
		out.append( outMap.get( token ) );
	    }
	    System.out.println( out.toString() );
	}
    }    
}
