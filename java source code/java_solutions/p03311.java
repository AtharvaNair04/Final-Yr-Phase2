import java.util.Arrays;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int a[] = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = sc.nextInt() - (i + 1);
        }
        // sum( abs(a[i] - b) )を最小化する
        sc.close();
        // 中央値をとる
        int midNum;
        Arrays.sort(a);
        if (n % 2 == 0) {
            midNum = (a[n / 2] + a[n / 2 - 1]) / 2;
        } else {
            midNum = a[n / 2];
        }
        long ans = 0;
        for (int i = 0; i < n; i++) {
            ans = ans + Math.abs(a[i] - midNum);
        }
        System.out.println(ans);
    }
}
