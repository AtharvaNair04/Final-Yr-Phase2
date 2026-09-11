import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;

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
        UGrouping solver = new UGrouping();
        solver.solve(1, in, out);
        out.close();
    }

    static class UGrouping {
        int n;
        int[][] arr;
        long[] preProcess;
        long[] memo;

        public void solve(int testNumber, Scanner sc, PrintWriter pw) {
            n = sc.nextInt();
            arr = new int[n][n];
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    arr[i][j] = sc.nextInt();
            preProcess = new long[1 << n];
            for (int i = 0; i < 1 << n; i++) {
                long temp = 0;
                for (int j = 0; j < n; j++) {
                    if ((i >> j & 1) == 1)
                        for (int z = j + 1; z < n; z++)
                            if ((i >> z & 1) == 1)
                                temp += arr[j][z];
                }
                preProcess[i] = temp;
            }
            memo = new long[1 << n];
            Arrays.fill(memo, -1);
            pw.println(dp(0, 0));
        }

        private long dp(int idx, int msk) {
            if (msk == (1 << n) - 1)
                return 0;
            if (idx == n)
                return (long) -1e18;
            if (memo[msk] != -1)
                return memo[msk];
            if ((msk >> idx & 1) == 1)
                return memo[msk] = dp(idx + 1, msk);
            int[] temp = new int[n];
            int size = 0;
            for (int i = 0; i < n; i++) {
                if ((msk & 1 << i) == 0)
                    temp[size++] = i;
            }
            long max = Long.MIN_VALUE;
            for (int j = 0; j < 1 << size; j++) {
                int i = 0;
                for (int z = 0; z < size; z++)
                    if ((j & 1 << z) != 0)
                        i |= 1 << temp[z];
                if ((i & msk) != 0 || (i >> idx & 1) == 0)
                    continue;
                max = Math.max(max, preProcess[i] + dp(idx + 1, msk | i));
            }
            return memo[msk] = max;
        }

    }

    static class Scanner {
        StringTokenizer st;
        BufferedReader br;

        public Scanner(FileReader r) {
            br = new BufferedReader(r);
        }

        public Scanner(InputStream s) {
            br = new BufferedReader(new InputStreamReader(s));
        }

        public String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return st.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

    }
}
