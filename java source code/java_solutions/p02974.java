import java.util.*;
import java.io.*;

public class Main {
    static final long MOD = 1000000007L;
    public static void main(String[] args) {
        FastScanner scanner = new FastScanner();
        PrintWriter out = new PrintWriter(System.out);
        int N = scanner.nextInt();
        int K = scanner.nextInt();
        long[][][] dp = new long[N+1][N+1][2605];
        dp[0][0][0] = 1;
        for(int i= 0; i <N; i++) {
            for(int j = 0; j <= i; j++) {
                for(int k = 0; k <= K; k++) {
                    if (dp[i][j][k] == 0) continue;
                    //close previously opened segment
                    if (j > 0 && k + j * 2 <= K) {
                        dp[i+1][j][k + j * 2] = (dp[i+1][j][k+j*2] + dp[i][j][k] * 2 * j) % MOD;
                    }
                    //base of putting self
                    dp[i+1][j][k+j*2] = (dp[i+1][j][k+j*2] + dp[i][j][k]) %MOD;
                    //close with cur already set
                    if (j> 0 && k + (j-1)*2 <= K) {
                        dp[i+1][j-1][k+(j-1)*2] = (dp[i+1][j-1][k+(j-1)*2] + dp[i][j][k] * j * j) % MOD;
                    }
                    //open new
                    if (k + (j+1)*2 <= K) {
                        dp[i+1][j+1][k+(j+1)*2] = (dp[i+1][j+1][k+(j+1)*2] + dp[i][j][k]) % MOD;
                    }
                }
            }
        }
        out.println(dp[N][0][K]);
        out.flush();
    }
    
    public static class FastScanner {
        BufferedReader br;
        StringTokenizer st;
        
        public FastScanner(Reader in) {
            br = new BufferedReader(in);
        }
        
        public FastScanner() {
            this(new InputStreamReader(System.in));
        }
        
        String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }
        
        int nextInt() {
            return Integer.parseInt(next());
        }
        
        long nextLong() {
            return Long.parseLong(next());
        }
        
        double nextDouble() {
            return Double.parseDouble(next());
        }
        
        String readNextLine() {
            String str = "";
            try {
                str = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return str;
        }
    }
}
