import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 *
 * @author masterbios
 */
public class Main {
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        InputReader in = new InputReader(inputStream);
        PrintWriter out = new PrintWriter(outputStream);
        FLISOnTree solver = new FLISOnTree();
        solver.solve(1, in, out);
        out.close();
    }

    static class FLISOnTree {
        final int MAXN = (int) (2e5) + 10;
        final int INF = (int) (1e9) + 700;
        List<Integer>[] graph;
        int[] dis;
        int[] dp;
        int[] ans;
        int n;

        public void solve(int testNumber, InputReader in, PrintWriter out) {
            n = in.nextInt();
            graph = new ArrayList[n + 1];
            dis = new int[n + 1];
            ans = new int[n + 1];
            dp = new int[MAXN];
            Arrays.fill(dp, INF);
            for (int i = 1; i <= n; i++) dis[i] = in.nextInt();
            for (int i = 0; i <= n; i++) graph[i] = new ArrayList<>();
            for (int i = 1; i < n; i++) {
                int u = in.nextInt(), v = in.nextInt();
                graph[u].add(v);
                graph[v].add(u);
            }
            dfs(1, 1, 1);
            for (int i = 1; i <= n; i++) out.println(ans[i]);
        }

        public void dfs(int node, int par, int depth) {
            int idx = lowerbound(1, depth, dis[node]);
            int before = dp[idx];
            dp[idx] = dis[node];
            if (idx >= ans[par]) {
                ans[node] = idx;
            } else {
                ans[node] = ans[par];
            }
            for (int child : graph[node]) {
                if (par == child) continue;
                dfs(child, node, depth + 1);
            }
            dp[idx] = before;
        }

        public int lowerbound(int low, int high, int find) {
            while (low <= high) {
                int mid = low + (high - low) / 2;
                if (dp[mid] == find) return mid;
                else if (dp[mid] < find) low = mid + 1;
                else high = mid - 1;
            }
            return (int) low;
        }

    }

    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreElements()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

    }
}
