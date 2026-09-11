import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Scanner;
import java.util.HashMap;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        Scanner in = new Scanner(inputStream);
        PrintWriter out = new PrintWriter(outputStream);
        FoxObservation solver = new FoxObservation();
        solver.solve(1, in, out);
        out.close();
    }

    static class FoxObservation {
        public void solve(int testNumber, Scanner in, PrintWriter out) {
            int N = in.nextInt();
            Map<Integer, Map<Integer, Long>> p = new HashMap<>();
            for (int i = 0; i < N; i++) {
                int x = in.nextInt();
                int y = in.nextInt();
                long w = in.nextInt();
                if (!p.containsKey(x)) {
                    p.put(x, new HashMap<>());
                }
                p.get(x).put(y, w);
            }
            int[] dx = new int[]{0, 1, 0, 1};
            int[] dy = new int[]{0, 0, 1, 1};
            int[] sign = new int[]{1, -1};
            long ans = 0;
            for (int x : p.keySet()) {
                for (int y : p.get(x).keySet()) {
                    for (int sx : sign) {
                        for (int sy : sign) {
                            long nowW = 0;
                            for (int i = 0; i < 4; i++) {
                                int nx = x + dx[i] * sx;
                                int ny = y + dy[i] * sy;
                                if (p.containsKey(nx) && p.get(nx).containsKey(ny)) {
                                    nowW += p.get(nx).get(ny);
                                }
                            }
                            ans = Math.max(ans, nowW);
                        }

                    }

                }
            }
            out.println(ans + " / 1");
        }

    }
}
