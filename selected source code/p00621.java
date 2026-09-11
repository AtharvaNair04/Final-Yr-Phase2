import java.io.*;
import java.util.*;

public class Main {
    void run() {
        while (true) {
            int W = readInt();
            int Q = readInt();
            if (W == 0 && Q == 0) break;
            int[] used = new int[W];
            Arrays.fill(used, -1);
            for (int q = 0; q < Q; q++) {
                char c = read().charAt(0);
                if (c == 's') {
                    int id = readInt();
                    int w = readInt();
                    int p = -1;
                    for (int i = 0; i + w <= W; i++) {
                        boolean ok = true;
                        for (int j = 0; j < w; j++) {
                            if (used[i + j] != -1) ok = false;
                        }
                        if (ok) {
                            p = i;
                            break;
                        }
                    }
                    if (p != -1) {
                        for (int i = 0; i < w; i++) {
                            used[p + i] = id;
                        }
                        sysout.println(p);
                    } else {
                        sysout.println("impossible");
                    }
                } else {
                    int id = readInt();
                    for (int i = 0; i < W; i++) {
                        if (used[i] == id) used[i] = -1;
                    }
                }
            }
            sysout.println("END");
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }

    // flush automatically iff you call `println` or `printf` or `format`.
    PrintWriter sysout = new PrintWriter(System.out, true);

    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer buffer = null;

    String read() {
        if (buffer == null || !buffer.hasMoreTokens()) {
            try {
                buffer = new StringTokenizer(in.readLine());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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

    String readLine() {
        buffer = null;
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
