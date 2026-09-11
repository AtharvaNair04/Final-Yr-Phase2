import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        main.solve();
    }
    public void solve() {
        Scanner scan = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int X = scan.nextInt();
        if (360 % X == 0) {
            System.out.println(360/X);
            return;
        }
        int lcm = lcm(360, X);
        System.out.println(lcm/X);
    }
    private int gcd(int x, int y) {
        if (y == 0) {
            return x;
        }
        return gcd(y, x % y);
    }
    private int lcm(int x, int y) {
        int gcd = gcd(x, y);
        return x / gcd * y;
    }
}
