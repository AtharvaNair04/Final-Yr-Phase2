import java.util.Scanner;

class Main {
    static final long mod = 1000000007;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();
        int a[] = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = sc.nextInt();
        }
        int b[] = new int[n];// 自分より小さい数の数

        for (int i = 0; i < n; i++) {
            int num = a[i];
            int cnt = 0;
            for (int j = i + 1; j < n; j++) {
                if (num > a[j]) {
                    cnt++;
                }
            }
            b[i] = cnt;
        }
        long c[] = new long[n]; // 自分より少ない
        for (int i = 0; i < n; i++) {
            int num = a[i];
            int cnt = 0;
            for (int j = 0; j < i; j++) {
                if (num > a[j]) {
                    cnt++;
                }
            }
            c[i] = cnt;
        }
        long ans = 0;

        long hh = k;
        long hhh = hh * (hh + 1) / 2;
        hhh %= mod;

        for (int i = 0; i < n; i++) {
            ans += b[i] * hhh % mod;
            ans %= mod;
        }

        long kk = k;
        long kkk = kk * (kk - 1) / 2;
        kkk %= mod;
        for (int i = 0; i < n; i++) {
            ans += kkk * c[i] % mod;
            ans %= mod;
        }

        System.out.println(ans);

        sc.close();
    }
}
