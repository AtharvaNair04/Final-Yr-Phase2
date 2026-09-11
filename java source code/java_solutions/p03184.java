/**
 * author: derrick20
 * created: 8/27/20 1:06 PM
 */
import java.io.*;
import java.util.*;

public class Main implements Runnable {
    public static void main(String[] args) throws Exception {
        new Thread(null, new Main(), ": )", 1 << 26).start();
    }

    public void run() {
        FastScanner sc = new FastScanner();
        PrintWriter out = new PrintWriter(System.out);

        int MAX = (int) 2e5; // todo change this!
        fact = new long[MAX + 1];
        invFact = new long[MAX + 1];
        fact[0] = 1;
        invFact[0] = 1;
        for (int i = 1; i <= MAX; i++) {
            fact[i] = ((long) i * fact[i - 1]) % mod;
            invFact[i] = inverse(fact[i]);
        }

        H = sc.nextInt();
        W = sc.nextInt();
        int N = sc.nextInt();
        nodes = new Node[N + 2];
        for (int i = 1; i <= N; i++) {
            nodes[i] = new Node(sc.nextInt(), sc.nextInt());
        }
        Node start = new Node(1, 1);
        Node end = new Node(H, W);
        nodes[0] = start;
        nodes[N + 1] = end;
        adjList = new ArrayList[N + 2];
        Arrays.setAll(adjList, i -> new ArrayList<>());
        for (int u = 0; u <= N + 1; u++) {
            Node a = nodes[u];
            for (int v = 0; v <= N + 1; v++) {
                if (u == v) continue;
                Node b = nodes[v];
                if (a.r <= b.r && a.c <= b.c) {
                    adjList[u].add(v); // let's make it directed
                }
            }
//            System.out.println(u + " -> " + adjList[u]);
        }
        dp = new long[N + 2];
        Arrays.fill(dp, -1);
        long ans = (mod - solve(0)) % mod;
        out.println(ans);
        out.close();
    }

    // todo SET THE MOD
    static long mod = (long) 1e9 + 7;
    static long[] fact, invFact;
    static ArrayList<Integer>[] adjList;
    static Node[] nodes;
    static int H, W;
    static long[] dp;

    static long solve(int u) {
        if (adjList[u].size() == 0) {
            return 1;
        } else if (dp[u] != -1) {
            return dp[u];
        } else {
            long ans = 0;
            Node node = nodes[u];
            for (int v : adjList[u]) {
                Node adj = nodes[v];
                int dr = adj.r - node.r;
                int dc = adj.c - node.c;
                long amt = (choose(dr + dc, dr) * solve(v)) % mod;
//                System.out.println("Branch from " +  node + " to " + adj + " = " + amt);
                // apply a negative sign now!
                ans += mod - amt;
                if (ans >= mod) {
                    ans -= mod;
                }
            }
//            System.out.println("Ans for " + nodes[u] + " = " + ans);
            return dp[u] = ans;
        }
    }

    static long choose(int n, int k) {
//        ASSERT(k <= n, "Invalid choose");
        long ans = (fact[n] * invFact[n - k]) % mod;
        return (ans * invFact[k]) % mod;
    }

    static long inverse(long x) {
        return fastExpo(x, mod - 2);
    }

    static long fastExpo(long x, long k) {
        long ans = 1;
        while (k > 0) {
            if ((k & 1) > 0) {
                ans = (x * ans) % mod;
            }
            x = (x * x) % mod;
            k >>= 1;
        }
        return ans;
    }
    static class Node {
        int r, c;
        public Node(int rr, int cc) {
            r = rr; c = cc;
        }
        public String toString() {
            return "(" + r + ", " + c + ")";
        }
    }

    static class FastScanner {
        private int BS = 1 << 16;
        private char NC = (char) 0;
        private byte[] buf = new byte[BS];
        private int bId = 0, size = 0;
        private char c = NC;
        private double cnt = 1;
        private BufferedInputStream in;

        public FastScanner() {
            in = new BufferedInputStream(System.in, BS);
        }

        public FastScanner(String s) {
            try {
                in = new BufferedInputStream(new FileInputStream(new File(s)), BS);
            } catch (Exception e) {
                in = new BufferedInputStream(System.in, BS);
            }
        }

        private char getChar() {
            while (bId == size) {
                try {
                    size = in.read(buf);
                } catch (Exception e) {
                    return NC;
                }
                if (size == -1) return NC;
                bId = 0;
            }
            return (char) buf[bId++];
        }

        public int nextInt() {
            return (int) nextLong();
        }

        public int[] nextInts(int N) {
            int[] res = new int[N];
            for (int i = 0; i < N; i++) {
                res[i] = (int) nextLong();
            }
            return res;
        }

        public long[] nextLongs(int N) {
            long[] res = new long[N];
            for (int i = 0; i < N; i++) {
                res[i] = nextLong();
            }
            return res;
        }

        public long nextLong() {
            cnt = 1;
            boolean neg = false;
            if (c == NC) c = getChar();
            for (; (c < '0' || c > '9'); c = getChar()) {
                if (c == '-') neg = true;
            }
            long res = 0;
            for (; c >= '0' && c <= '9'; c = getChar()) {
                res = (res << 3) + (res << 1) + c - '0';
                cnt *= 10;
            }
            return neg ? -res : res;
        }

        public double nextDouble() {
            double cur = nextLong();
            return c != '.' ? cur : cur + nextLong() / cnt;
        }

        public double[] nextDoubles(int N) {
            double[] res = new double[N];
            for (int i = 0; i < N; i++) {
                res[i] = nextDouble();
            }
            return res;
        }

        public String next() {
            StringBuilder res = new StringBuilder();
            while (c <= 32) c = getChar();
            while (c > 32) {
                res.append(c);
                c = getChar();
            }
            return res.toString();
        }

        public String nextLine() {
            StringBuilder res = new StringBuilder();
            while (c <= 32) c = getChar();
            while (c != '\n') {
                res.append(c);
                c = getChar();
            }
            return res.toString();
        }

        public boolean hasNext() {
            if (c > 32) return true;
            while (true) {
                c = getChar();
                if (c == NC) return false;
                else if (c > 32) return true;
            }
        }
    }
    static void ASSERT(boolean assertion, String message) {
        if (!assertion) throw new AssertionError(message);
    }
    static void ASSERT(boolean assertion) {
        if (!assertion) throw new AssertionError();
    }
}
