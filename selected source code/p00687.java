import java.util.Scanner;

class Main {
    private static void solve() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            int n = scanner.nextInt();
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            if (n == 0 && a == 0 && b == 0) {
                break;
            }
            int total = 0;
            for (int i = 0; a * i <= n; i++) {
                if (i > 0 && a * i % b == 0) {
                    break;
                } else {
                    total += (n - a * i) / b + 1;
                }
            }
            System.out.println(n - total + 1);
        }
    }

    public static void main(String... args) {
        solve();
    }
}
