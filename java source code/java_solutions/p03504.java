import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;
 
public class Main {
 
    void solve() throws IOException {
        int N = ni();
        int C = ni();
        int[] s = new int[N];
        int[] t = new int[N];
        int[] c = new int[N];
        int[] tt = new int[(int)200002];
        int[] sm = new int[(int)200002];
        for (int i = 0; i < N; i++) {
            s[i] = ni();
            t[i] = ni();
            c[i] = ni()-1;
        }
        for (int i = 0; i < C; i++) {
            for (int j = 0; j < tt.length; j++) {
                tt[j] = 0;
            }
            for (int j = 0; j < N; j++) {
                if(c[j] == i){
                    tt[s[j]*2-1]++;
                    tt[t[j]*2]--;
                }
            }
            for (int j = 1; j < tt.length; j++) {
                tt[j] += tt[j-1];
            }
            for (int j = 0; j < tt.length; j++) {
                if(tt[j]>0) sm[j]++;
            }
            // for (int j = 0; j < 40; j++) {
            //     System.out.print(tt[j]);
            // }
            // System.out.println();
        }
        int ans = 0;
        for (int i = 0; i < sm.length; i++) {
            ans = Math.max(ans, sm[i]);
        }
        out.println(ans);
    }

    final int mod = 1000000007;
    int mul(int x, int y){
        return (int)((x * 1L * y) % mod);
    }
    int add(int x, int y) {
        x += y;
        return x >= mod ? (x - mod) : x;
    }
    int sub(int x, int y){
        x = add(x,mod-y);
        return x >= mod ? (x - mod) : x;
    }
    String ns() throws IOException {
        while (!tok.hasMoreTokens()) {
            tok = new StringTokenizer(in.readLine(), " ");
        }
        return tok.nextToken();
    }
 
    int ni() throws IOException {
        return Integer.parseInt(ns());
    }
 
    long nl() throws IOException {
        return Long.parseLong(ns());
    }
 
    double nd() throws IOException {
        return Double.parseDouble(ns());
    }
 
    String[] nsa(int n) throws IOException {
        String[] res = new String[n];
        for (int i = 0; i < n; i++) {
            res[i] = ns();
        }
        return res;
    }
 
    int[] nia(int n) throws IOException {
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            res[i] = ni();
        }
        return res;
    }
 
    long[] nla(int n) throws IOException {
        long[] res = new long[n];
        for (int i = 0; i < n; i++) {
            res[i] = nl();
        }
        return res;
    }
 
    static BufferedReader in;
    static PrintWriter out;
    static StringTokenizer tok;
 
    public static void main(String[] args) throws IOException {
        in = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(System.out);
        tok = new StringTokenizer("");
        Main main = new Main();
        main.solve();
        out.close();
    }
}
