import java.io.*;
import java.util.*;

public class Main {
    void run() {
        while (true) {
            int N = readInt();
            int M = readInt();
            if(N == 0 && M == 0) break;
            int[] T = new int[N];
            int[] S = new int[N];
            for (int i = 0; i < N; i++) {
                T[i] = readInt();
                S[i] = T[i] * (M / T[i]);
            }
            int minArg = 0;
            for (int i = 0; i < N; i++) {
                if (S[i] < S[minArg]) {
                    minArg = i;
                }
                else if (S[i] == S[minArg] && T[i] < T[minArg]) {
                    minArg = i;
                }
            }

            int hope = 0;
            for (int i = 0; i < N; i++) {
                if(S[i] == S[minArg] && T[i] == T[minArg]) {
                    hope++;
                }
            }
            if(hope >= 2) {
                sysout.println(N);
            }
            else {
                sysout.println(minArg + 1);
            }
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }

    // flush automatically iff you call `println` or `printf` or `format`.
    PrintWriter sysout = new PrintWriter(System.out, true);

    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer buffer = new StringTokenizer("");

    String read() {
        if (!buffer.hasMoreTokens()) {
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
        buffer = new StringTokenizer("");
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
