import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        String[] params = in.nextLine().split(" ");
        long n = Long.parseLong( params[0]);
        long a = Long.parseLong( params[1]);
        long b = Long.parseLong( params[2]);

        if( ( b - a ) % 2 != 0 ){
            long wina = a;
            long winb = b;
            long countWin = wina - 1;
            winb -= countWin;
            countWin++;
            winb--;
            countWin += ( winb - 1 ) / 2;

            long losea = a;
            long loseb = b;
            long countLose = n - loseb;
            losea += countLose;
            countLose++;
            losea++;
            countLose += ( n - losea ) / 2;

            System.out.println( countWin < countLose ? countWin : countLose );
        }
        else {
            System.out.println( ( b - a ) / 2 );
        }

    }

}
