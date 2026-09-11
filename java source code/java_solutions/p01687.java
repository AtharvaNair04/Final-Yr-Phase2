import java.io.*;
import java.util.*;

public class Main {
    void run() {
        String S = read();
        int L = 8;
        char[] ts = "AIDUNYAN".toCharArray();
        Arrays.sort(ts);
        for (int i = 0; i + L <= S.length(); ) {
            char[] cs = S.substring(i, i + L).toCharArray();
            Arrays.sort(cs);
            if (Arrays.equals(cs, ts)) {
                S = S.substring(0, i) + "AIZUNYAN" + S.substring(i + L);
                i += L;
            }
            else {
                i += 1;
            }
        }
        sysout.println(S);
    }

    public static void main(String[] args) {
        new Main().run();
    }

    // flush automatically iff you call `println` or `printf` or `format`.
    PrintWriter sysout = new PrintWriter(System.out, true);

    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer buffer = null;

    String readLine() {
        buffer = null;
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    String read() {
        if (buffer == null || !buffer.hasMoreTokens()) {
            buffer = new StringTokenizer(readLine());
        }
        return buffer.nextToken();
    }

    int readInt() {
        return Integer.parseInt(read());
    }

    long readLong() {
        return Long.parseLong(read());
    }

    double readDouble() {
        return Double.parseDouble(read());
    }
}
