import java.util.Arrays;
import java.util.Scanner;


public class Main {

    public static String Y = "Yes";
    public static String N = "No";
    public static long MOD = (long) (Math.pow(10, 9) + 7);
    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        int n = ni();
        int ans = 0;
        for (int i = 0; i < n; i++) {
            int l = ni();
            int r = ni();

            ans += r - l + 1;
        }
        out(ans);

    }

    /*
     * 以下メソッド集
     */
    static double[] sort(double[] r) {

        Arrays.sort(r);
        return r;
    }

    static int[] sort(int[] r) {

        Arrays.sort(r);
        return r;
    }

    static void debug() {

        out("---");
    }

    static void debug(long a) {

        out("-------");
        out(a);
        out("-------");
    }

    static void debug(String a) {

        out("-------");
        out(a);
        out("-------");
    }

    static long[] sort(long[] n) {

        Arrays.sort(n);
        return n;
    }

    static int ketasuu(int n) {

        String str = "" + n;
        return str.length();
    }

    static int account(String str) {

        String target = "AC";
        int count = 0;
        int len = str.length();
        for (int i = 0; i < len - 1; i++) {
            if (target.equals(str.substring(i, i + target.length()))) {
                count++;
            }
        }
        return count;
    }

    static int ni() {

        return sc.nextInt();
    }

    static long nl() {

        return sc.nextLong();
    }

    static double nd() {

        return sc.nextDouble();
    }

    static String n() {

        return sc.next();
    }

    static char[] nc() {

        return sc.next().toCharArray();
    }

    static int kaijo(int n) {

        if (n == 0 || n == 1) {
            return 1;
        } else {
            return n * kaijo(n - 1);
        }
    }

    static int fib(int n) {

        return (n == 1 || n == 0) ? n : fib(n - 2) + fib(n - 1);
    }

    static long lcm(long m, long n) {

        return m * n / gcd(m, n);
    }

    static int lcm(int m, int n) {

        return m * n / gcd(m, n);
    }

    static long gcd(long a, long b) {

        return (b == 0) ? a : gcd(b, a % b);
    }

    static int gcd(int a, int b) {

        return (b == 0) ? a : gcd(b, a % b);
    }

    static void out(String info) {

        System.out.println(info);
    }

    static void out(int info) {

        System.out.println(info);
    }

    static void out(double info) {

        System.out.println(info);
    }

    static void out(long info) {

        System.out.println(info);
    }

    static void out(char info) {

        System.out.println(info);
    }

    static void out(Object obj) {

        System.out.println(obj.toString());
    }

    static void outn(String info) {

        System.out.print(info);
    }

    static void outn(int info) {

        System.out.print(info);
    }

    static void outn(double info) {

        System.out.print(info);
    }

    static void outn(long info) {

        System.out.print(info);
    }

    static void outn(char info) {

        System.out.print(info);
    }

    static double max(double d, double e) {

        return Math.max(d, e);
    }

    static long max(long a, long b) {

        return Math.max(a, b);
    }

    static long min(long d, long e) {

        return Math.min(d, e);
    }

    static int min(int a, int b) {

        return Math.min(a, b);
    }
}


class XY {

    int h;
    int c;

    XY(int h, int c) {
        this.h = h;
        this.c = c;
    }
}
