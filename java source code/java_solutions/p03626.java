import java.util.Scanner;

class Main {
    static final long mod = 1000000007;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        String s = sc.next();
        String t = sc.next();
        long ans = 3;
        boolean tate = true;
        int start = 0;
        if (s.charAt(0) == t.charAt(0)) {
            start = 1;
        } else {
            tate = false;
            start = 2;
            ans = 6;
        }

        for (int i = start; i < n; i++) {
            char c1 = s.charAt(i);
            char c2 = t.charAt(i);
            if (tate) {
                if (c1 == c2) {
                    ans *= 2;
                    ans %= mod;
                    tate = true;
                } else {
                    ans *= 2;
                    ans %= mod;
                    i++;
                    tate = false;
                }
            } else {
                if (c1 == c2) {
                    ans *= 1;
                    tate = true;
                } else {
                    ans *= 3;
                    ans %= mod;
                    i++;
                    tate = false;
                }
            }

        }
        System.out.println(ans);
        sc.close();
    }
}
