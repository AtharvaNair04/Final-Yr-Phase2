import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.*;


 
public class Main {
 
    void solve() throws IOException {
        BigInteger md = BigInteger.valueOf(mod);
        int K = ni();
        String S = ns();
        int N = S.length();
        final int MAX = (int)2e6+10;
        fact = new int[MAX];
        fact[0] = 1;
        for (int j = 1; j < fact.length; j++) {
            fact[j] = mul(fact[j-1],j);
        }
        factInv = new int[MAX];
        factInv[MAX-1] = BigInteger.valueOf(fact[MAX-1]).modInverse(md).intValue(); 
        for (int j = MAX-1; j >=1; j--) {
            factInv[j-1] = mul(factInv[j],j);
        }
        int[] d25 = new int[K+1];
        d25[0] = 1;
        for (int i = 1; i < d25.length; i++) {
            d25[i] = mul(d25[i-1],25);
        }
        int[] d26 = new int[K+1];
        d26[0] = 1;
        for (int i = 1; i < d26.length; i++) {
            d26[i] = mul(d26[i-1],26);
        }
        int ans = 0;
        for (int i = N-1; i <N+K; i++) {
            int addv = c(i, N-1);
            addv = mul(addv,d25[i+1-N]);
            addv = mul(addv,d26[K-(i+1-N)]);
            ans = add(ans,addv);
        }
        out.println(ans);
    }

    int c(int n, int k) {
        if (k < 0 || k > n) {
            return 0;
        }
        return mul(fact[n], mul(factInv[k], factInv[n - k]));
    }
    int mod = 1000000007;
    int[] fact;
    int[] factInv;

    int mul(int x, int y) {
        return (int) ((x * 1L * y) % mod);
    }
    int add(int x, int y) {
        x += y;
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
