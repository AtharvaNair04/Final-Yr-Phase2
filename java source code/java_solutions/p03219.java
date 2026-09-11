import java.io.PrintWriter;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (
                Scanner in = new Scanner(System.in);
                PrintWriter out = new PrintWriter(System.out))
        {
            final int X = in.nextInt();
            final int Y = in.nextInt();

            out.println(X + (Y >> 1));
        }
    }
}
