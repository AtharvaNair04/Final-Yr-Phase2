import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class Main {
    static final int INF = (int)1e9+7;
    int N,K;
    int[] A;
    ArrayList<Integer>[] G,revG;


    @SuppressWarnings("unchecked")
    public void solve() {
        N = nextInt();
        K = nextInt();

        A = new int[N];
        for(int i = 0;i < N;i++) {
            A[i] = nextInt();
        }

        G = new ArrayList[N];
        revG = new ArrayList[N];
        for(int i = 0;i < N;i++) {
            G[i] = new ArrayList<>();
            revG[i] = new ArrayList<>();
        }

        for(int i = 0;i < N;i++) {
            if (A[i] != 0) {
                G[i].add(A[i]-1);
                revG[A[i]-1].add(i);
            }
        }
        int[] d = new int[N];
        Arrays.fill(d,INF);

        for(int i = N-1;i >= 0;i--) {
            if (revG[i].size() == 0) {
                d[i] = 0;
                if (G[i].size() > 0){
                    int pre = -1;
                    int next = i;
                    while(G[next].size() > 0 && (pre == -1 || d[next] > d[pre] + 1)) {
                        if (pre != -1){
                            d[next] = Math.min(d[next],d[pre] + 1);
                        }
                        pre = next;
                        next = G[next].get(0);
                    }
                }
            }
        }
        int ans = 0;
        for(int i = 0;i < N;i++) {
            if (G[i].size() == 0 || d[i] < K) {
                ans++;
            }
        }

        out.println(ans);

    }

    public static void main(String[] args) {
        out.flush();
        new Main().solve();
        out.close();
    }

    /* Input */
    private static final InputStream in = System.in;
    private static final PrintWriter out = new PrintWriter(System.out);
    private final byte[] buffer = new byte[2048];
    private int p = 0;
    private int buflen = 0;

    private boolean hasNextByte() {
        if (p < buflen)
            return true;
        p = 0;
        try {
            buflen = in.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (buflen <= 0)
            return false;
        return true;
    }

    public boolean hasNext() {
        while (hasNextByte() && !isPrint(buffer[p])) {
            p++;
        }
        return hasNextByte();
    }

    private boolean isPrint(int ch) {
        if (ch >= '!' && ch <= '~')
            return true;
        return false;
    }

    private int nextByte() {
        if (!hasNextByte())
            return -1;
        return buffer[p++];
    }

    public String next() {
        if (!hasNext())
            throw new NoSuchElementException();
        StringBuilder sb = new StringBuilder();
        int b = -1;
        while (isPrint((b = nextByte()))) {
            sb.appendCodePoint(b);
        }
        return sb.toString();
    }

    public int nextInt() {
        return Integer.parseInt(next());
    }

    public long nextLong() {
        return Long.parseLong(next());
    }

    public double nextDouble() {
        return Double.parseDouble(next());
    }
}
