import java.math.BigInteger;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in).useDelimiter("[^0-9]+");
        BigInteger r = BigInteger.ZERO;
        while (sc.hasNextBigInteger())
            r = r.add(sc.nextBigInteger());
        System.out.println(r.toString());
    }
}
