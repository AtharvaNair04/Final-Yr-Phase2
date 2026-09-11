import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        int n = Integer.parseInt(in.nextLine());
        String[] params = in.nextLine().split(" ");

        double parent = 0;
        for( int i = 0 ; i < n ; i++ ){
            parent += 1 / Double.parseDouble(params[i]);
        }

        System.out.println( 1/parent );

    }

}
