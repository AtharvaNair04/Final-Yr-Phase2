import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

public class Main {
    static final int INF = (int)1e9 + 7;
    int N,M,s,t;
    int[] d;
    ArrayList<Integer>[] G;

    private class D implements Comparable<D>{
        int now;
        int dime;
        int cost;
        public D(int now,int dime,int cost) {
            this.now = now;
            this.dime = dime;
            this.cost = cost;
        }

        public int compareTo(D d) {
            return this.cost - d.cost;
        }
    }

    @SuppressWarnings("unchecked")
    public void solve() {
        N = nextInt();
        M = nextInt();
        s = nextInt()-1;
        t = nextInt()-1;

        d = new int[N];

        for(int i = 0;i < N;i++) {
            d[i] = nextInt();
        }

        G = new ArrayList[N];
        for(int i = 0;i < N;i++) {
            G[i] = new ArrayList<>();
        }

        for(int i = 0;i < M;i++) {
            int a = nextInt()-1;
            int b = nextInt()-1;
            G[a].add(b);
        }

        int[] minCost = new int[N];
        Arrays.fill(minCost,INF);
        PriorityQueue<D> pq = new PriorityQueue<>();
        pq.add(new D(s, s,0));

        while(pq.size() > 0) {

            D data = pq.poll();
            if(minCost[data.now] <= data.cost)continue;
            minCost[data.now] = data.cost;

            for(int next : G[data.now]) {
                if (minCost[next] > data.cost + d[next]) {
                    pq.add(new D(next, Math.max(next,data.dime), data.cost + d[next]));
                }
            }
            if (data.now -1 >= 0 && minCost[data.now-1] > data.cost){
                pq.add(new D(data.now-1,data.dime,data.cost));
            }
        }

        out.println(minCost[t]);
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
