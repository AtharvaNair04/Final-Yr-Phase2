import java.util.*;

public class Main {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        String[] params = in.nextLine().split( " " );
        int n = Integer.parseInt( params[0] );
        int k = Integer.parseInt( params[1] );
        long s = Long.parseLong( params[2] );

        if( k == 0 ){
            for( int i = 0 ; i < n ; i++ ){
                if( i != 0 ){
                    System.out.print(" ");
                }
                if( s == 1000000000 ){
                    System.out.print( s - 1 );
                }
                else {
                    System.out.print( s + 1 );
                }
            }
            System.out.println();
            return;
        }

        for( int i = 0 ; i < k ; i++ ){
            if( i != 0 ){
                System.out.print(" ");
            }
            System.out.print( s );
        }
        for( int i = 0 ; i < n - k ; i++ ){
            if( s == 1000000000 ){
                System.out.print( " " + ( s - 1 ) );
            }
            else {
                System.out.print( " " + ( s + 1 ) );
            }
        }
        System.out.println();

    }

}
