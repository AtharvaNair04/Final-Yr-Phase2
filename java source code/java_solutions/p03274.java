import java.util.Scanner;

class Main {

    public static int min(int a, int b) {
        if (a < b) {
            return a;
        } else {
            return b;
        }

    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int k = scanner.nextInt();
        int[] candles = new int[n];
        for (int i = 0; i < n; i++) {
            candles[i] = scanner.nextInt();
        }

        int ans = Integer.MAX_VALUE;

        for (int i = 0; i < n - k + 1; i++) {
            int a = Math.abs(candles[i] - candles[i + k - 1]) + Math.abs(candles[i]);
            int b = Math.abs(candles[i] - candles[i + k - 1]) + Math.abs(candles[i + k - 1]);
            int min = min(a, b);
            if (min < ans)
                ans = min;
        }
        System.out.println(ans);
    }

}
