import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.next();

        int len = s.length();
        long ans = 0;
        for (int bit = 0; bit < (1 << (len - 1)); bit++) {
            long wa = 0;
            long now = s.charAt(0) - '0';

            for (int i = 1; i < len; i++) {
                int tmp = s.charAt(i) - '0';
                if ((bit & (1 << len - 1 - i)) != 0) {
                    // +の場合
                    wa += now;
                    now = tmp;
                } else {
                    now *= 10;
                    now += tmp;
                }
            }
            wa += now;
            ans += wa;
        }
        System.out.println(ans);
        sc.close();
    }
}
