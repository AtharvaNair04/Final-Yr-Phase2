import java.util.*;
import java.io.*;
import java.lang.*;

public final class Main {

    private final static boolean USE_FILE_IO = false;
    private final static long M = 1000000007;

    Reader reader;
    Writer writer;

    Main(Reader reader, Writer writer) {
        this.reader = reader;
        this.writer = writer;
    }

    void solve() throws java.lang.Exception {
        int n = reader.nextInt();
        int[] a = new int[n];
        for (int i=0; i<n; i++) a[i] = reader.nextInt();
        int[] s = new int[n];
        s[0] = a[0];
        for (int i=1; i<n; i++) s[i] = s[i-1] ^ a[i];
        int[] z = new int[n+1];
        z[0] = s[0] == 0 ? 1 : 0;
        for (int i=1; i<n; i++) z[i] = z[i-1] + (s[i] == 0 ? 1 : 0);
        z[n] = z[n-1];
        int[] next = new int[n];
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i=n-1; i>=0; i--) {
            if (map.containsKey(s[i])) {
                next[i] = map.get(s[i]);
            } else {
                next[i] = n;
            }
            map.put(s[i], i);
        }
        long ans = 0;
        if (s[n-1] != 0) ans = count(s, next, z, n, map.get(s[n-1]), s[n-1])[0];
        else {
            for (int x : map.keySet()) {
                ans = (ans + count(s, next, z, n, map.get(x), x)[1]) % M;
            }
        }
        writer.writeLine(ans);
    }

    long[] count(int[] s, int[] next, int[] z, int n, int start, int x) {
        long ans = 0;
        if (x==0) {
            ans = 1;
            for (int i=0; i<z[n-1]-1; i++) {
                ans = (ans << 1) % M;
            }
            return new long[] { ans, ans };
        }
        long ans0 = 1;
        int i = start;
        while (i<n) {
            ans = (ans0 + ans) % M;
            int j = next[i];
            long d = z[j] - z[i];
            ans0 = (ans0 + ans * d) % M;
            i = j;
        }
        return new long[] { ans0, ans };
    }

    public static void main(String[] args) throws java.lang.Exception {
        Reader reader = null;
        Writer writer = null;
        try {
            if (USE_FILE_IO) {
                reader = new Reader(new FileInputStream("input.txt"));
                writer = new Writer(new FileOutputStream("output.txt"));
            } else {
                reader = new Reader(System.in);
                writer = new Writer(System.out);
            }
            new Main(reader, writer).solve();
        } finally {
            if (reader != null)
                reader.close();
            if (writer != null)
                writer.close();
        }
    }

    static class Writer {
        BufferedWriter bw;

        public Writer(OutputStream out) {
            bw = new BufferedWriter(new OutputStreamWriter(out));
        }

        void write(String s) throws java.lang.Exception {
            bw.write(s);
        }

        void write(int x) throws java.lang.Exception {
            write(String.valueOf(x));
        }

        void write(long x) throws java.lang.Exception {
            write(String.valueOf(x));
        }

        void write(char c) throws java.lang.Exception {
            bw.write(c);
        }

        void newLine() throws java.lang.Exception {
            bw.newLine();
        }

        void writeLine(String s) throws java.lang.Exception {
            write(s);
            newLine();
        }

        void writeLine(int n) throws java.lang.Exception {
            writeLine(String.valueOf(n));
        }

        void writeLine(long n) throws java.lang.Exception {
            writeLine(String.valueOf(n));
        }
        
        void writeLine() throws java.lang.Exception {
            writeLine("");
        }

        void close() throws java.lang.Exception {
            bw.flush();
            bw.close();
        }
    }

    static class Reader {
        BufferedReader br;
        StringTokenizer st;

        public Reader(InputStream in) {
            br = new BufferedReader(new InputStreamReader(in));
        }

        String nextToken() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        String[] nextTokens(int n) {
            String[] a = new String[n];
            for (int i=0; i<n; i++) {
                a[i] = nextToken();
            }
            return a;
        }

        int nextInt() {
            return Integer.parseInt(nextToken());
        }

        int[] nextInts(int n) {
            int[] a = new int[n];
            for (int i=0; i<n; i++) {
                a[i] = nextInt();
            }
            return a;
        }

        long nextLong() {
            return Long.parseLong(nextToken());
        }

        long[] nextLongs(int n) {
            long[] a = new long[n];
            for (int i=0; i<n; i++) {
                a[i] = nextInt();
            }
            return a;
        }

        double nextDouble() {
            return Double.parseDouble(nextToken());
        }

        String nextLine() {
            String str = "";
            try {
                str = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return str;
        }

        void close() throws java.lang.Exception {
            br.close();
        }
    }
}
