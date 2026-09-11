import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 *
 * @author hsy
 */
public class Main {
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        Scanner in = new Scanner(inputStream);
        PrintWriter out = new PrintWriter(outputStream);
        Task solver = new Task();
        solver.solve(1, in, out);
        out.close();
    }

    static class Task {
        public void solve(int testNumber, Scanner in, PrintWriter out) {
            String s = in.next();
            int l = -1, r = -1;
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == 'K') {
                    l = i;
                    break;
                }
            }
            for (int i = s.length() - 1; i >= 0; i--) {
                if (s.charAt(i) == 'R') {
                    r = i;
                    break;
                }
            }
            if (l == -1 || r == -1) {
                out.println("NO");
                return;
            }
            if (l > 1 || (s.length() - 1 - r) > 1) {
                out.println("NO");
                return;
            }
            for (int i = 0; i < l; i++) {
                if (s.charAt(i) != 'A') {
                    out.println("NO");
                    return;
                }
            }
            for (int i = r + 1; i < s.length(); i++) {
                if (s.charAt(i) != 'A') {
                    out.println("NO");
                    return;
                }
            }
            String str = s.substring(l, r + 1);
            if (str.equals("KIHABAR") || str.equals("KIHBAR") || str.equals("KIHABR") || str.equals("KIHBR")) {
                out.println("YES");
            } else {
                out.println("NO");
            }
        }

    }
}
