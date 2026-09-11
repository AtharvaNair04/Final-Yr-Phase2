import java.util.*;

public class Main {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        int n = Integer.parseInt( in.nextLine() );
        String[] params = in.nextLine().split(" ");
        int[] ps = new int[n];
        for( int i = 0 ; i < n ; i++ ){
            ps[i] = Integer.parseInt( params[i] );
        }

        int ans = 0;
        int min = ps[0];
        for( int i = 0 ; i < n ; i++ ){
            if( ps[i] <= min ){
                ans++;
                min = ps[i];
            }
        }

        System.out.println( ans );

    }

}
