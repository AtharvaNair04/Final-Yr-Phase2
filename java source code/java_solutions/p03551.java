import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(in);
        PrintWriter w = new PrintWriter(out);

        int n = sc.nextInt();
        int m = sc.nextInt();

        int t1 = (n-m) * 100;
        int t2 = m * 1900;

        int chance = (int) Math.pow(2, m);
        
        w.println((t1+t2)*chance);

        w.close();
    }

}
