import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();
        sc.close();

        // 作れる最大値
        int max = n - 2;
        if (n >= 3) {
            max += (n - 3) * (n - 2) / 2;
        }
        if (max < k) {
            // 可能より多く作らなくてはならない
            System.out.println(-1);
            return;
        }
        int del = k - max;
        int path = n - 1 - del;
        System.out.println(path);
        p(1, 2);
        for (int i = 3; i <= n; i++) {
            // 先にpathを構成して消していく
            p(2, i);
        }
        int count = 0;
        if (count == -del) {
            return;
        }
        for (int i = 3; i <= n; i++) {
            for (int j = i + 1; j <= n; j++) {
                p(i, j);
                count++;
                if (count == -del) {
                    return;
                }
            }
        }
        for (int i = 3; i <= n; i++) {
            p(1, i);
            count++;
            if (count == -del) {
                return;
            }
        }
    }

    static void p(int a, int b) {
        System.out.println(a + " " + b);
    }
}
