import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main implements Runnable {

    private static int MOD = 1_000_000_007;

    public static void main(String[] args) {
        // Run with 32MB stack
        Thread thread = new Thread(null, new Main(), "", 32 * 1024 * 1024);
        thread.start();
    }

    @Override
    public void run() {
        final Scanner scanner = new Scanner(System.in);
        solve(scanner);
    }

    static void solve(Scanner scanner) {
        int N = Integer.parseInt(scanner.next());
        Map<String, Integer> m = new HashMap<>();
        int total = 0;
        for (int i = 0; i < N; i++) {
            String s = scanner.next();
            int n = Integer.parseInt(scanner.next());
            total += n;
            m.put(s, total);
        }
        String x = scanner.next();
        System.out.println(total - m.get(x));
    }

}
