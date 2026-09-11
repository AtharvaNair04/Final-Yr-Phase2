import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.*;
 
public class Main {
 
    void solve() throws IOException {
        int N = ni();
        int K = ni();
        int[] a = new int[N];
        for (int i = 0; i < N; i++) {
            a[i] = ni();
        }
        int[] fac = new int[N+1];
        fac[0] = 1;
        for (int i = 1; i <= N; i++) {
            fac[i] = mul(fac[i-1],i);
        }
        int invKm1 = BigInteger.valueOf(fac[K-1]).modInverse(MOD).intValue();
        int invN = BigInteger.valueOf(fac[N]).modInverse(MOD).intValue();
        int[] facinv = new int[N+1];
        facinv[N] = invN;
        for (int i = N-1; i >=0; i--) {
            facinv[i] = mul(facinv[i+1],i+1);
        }
        // System.out.println(Arrays.toString(fac));
        // System.out.println(Arrays.toString(facinv));
        Arrays.sort(a);
        BigInteger ans = BigInteger.ZERO;
        for (int i = N-1; i>=K-1 ; i--) {
            BigInteger v = BigInteger.valueOf(fac[i]);
            v = v.multiply(BigInteger.valueOf(invKm1)).mod(MOD);
            v = v.multiply(BigInteger.valueOf(facinv[i-K+1])).mod(MOD);
            v = v.multiply(BigInteger.valueOf(a[i])).mod(MOD);
            ans = ans.add(v).mod(MOD);
            // int count = mul(mul(fac[i],invKm1),facinv[i-K+1]);
            // ans = add(ans, mul(count,a[i]));
        }
        for (int i = 0; i<=N-K; i++) {
            BigInteger v = BigInteger.valueOf(fac[N-i-1]);
            v = v.multiply(BigInteger.valueOf(invKm1)).mod(MOD);
            v = v.multiply(BigInteger.valueOf(facinv[N-K-i])).mod(MOD);
            v = v.multiply(BigInteger.valueOf(a[i])).mod(MOD);
            ans = ans.subtract(v).mod(MOD);
            // int count = mul(mul(fac[N-i-1],invKm1),facinv[N-K-i]);
            // ans = sub(ans, mul(count,a[i]));
        }


        out.println(ans.intValue());
    }

    final int mod = 1000000007;
    final BigInteger MOD = BigInteger.valueOf(mod);
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
