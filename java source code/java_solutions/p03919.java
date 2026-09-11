import java.util.Scanner;

/**
 * A - Where's Snuke?.
 */
public class Main {

    private static final String[] alpha = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    public static void main( String[] args ) {

        Scanner scn = new Scanner( System.in );
        int height = scn.nextInt();
        int width = scn.nextInt();

        if ( height == 0 || width == 0 ) {
            System.out.println( "Input is invalid!" );
        }

        int count = 0;
        String str = null;

        int h_result = 0;
        int w_result = 0;

        while ( scn.hasNext() ) {
            count = count + 1;
            str = scn.next();
            if ( "snuke".equals( str ) ) {
                break;
            }
        }

        int k = count % width;
        if ( k == 0 ) {
            w_result = width;
            h_result = count / width;
        } else {
            w_result = k;
            h_result = (count / width) + 1;
        }
        System.out.println( alpha[w_result - 1] + Integer.toString( h_result ) );

    }

}
