import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.*;
import java.util.jar.JarOutputStream;

public class Main {

    public static void recurse(ArrayList<ArrayList<Integer>> adj, int[] dp,int[] visited, int v){

        visited[v]=1;

        for(int i=0;i<adj.get(v).size();i++){
            if(visited[adj.get(v).get(i)]!=1){
                recurse(adj,dp,visited,adj.get(v).get(i));
            }
            dp[v]=Math.max(dp[v],dp[adj.get(v).get(i)]+1);

        }
    }

    public static void main(String[] args) throws IOException {

        Reader.init(System.in);
        int n = Reader.nextInt();
        double[] prob=new double[n];
        for(int i=0;i<n;i++){
            prob[i]=Reader.nextDouble();
        }
        double[][] dp=new double[n+1][n+1];
        dp[0][0]=1;

        for(int i=1;i<=n;i++){
            for(int j=0;j<=i;j++){
                if(j==0){
                    dp[i][j] = dp[i-1][j]*(1-prob[i-1]);
                }
                else{
                    dp[i][j] = dp[i-1][j]*(1-prob[i-1])+dp[i-1][j-1]*prob[i-1];
                }
            }
        }
        double ans=0;
        for (int i = (n + 1) / 2; i <= n; i += 1){
            ans += dp[n][i];
        }
        System.out.println(ans);
    }

}






class Reader {
    static BufferedReader reader;
    static StringTokenizer tokenizer;

    /** call this method to initialize reader for InputStream */
    static void init(InputStream input) {
        reader = new BufferedReader(
                new InputStreamReader(input) );
        tokenizer = new StringTokenizer("");
    }

    /** get next word */
    static String nextLine() throws IOException {
        while ( ! tokenizer.hasMoreTokens() ) {
            //TODO add check for eof if necessary
            tokenizer = new StringTokenizer(
                    reader.readLine() );
        }
        return tokenizer.nextToken();
    }

    static int nextInt() throws IOException {
        return Integer.parseInt( nextLine() );
    }
    static long nextLong() throws IOException {
        return Long.parseLong(nextLine());
    }

    static double nextDouble() throws IOException {
        return Double.parseDouble( nextLine() );
    }

    static float nextFloat() throws IOException {
        return Float.parseFloat( nextLine() );
    }
    static int[] nextIntArray(int length) throws IOException {
        int[] arr = new int[length];
        //System.out.println(length);
        for (int i=0; i<length; i++) {
            arr[i] = Integer.parseInt( nextLine() );
            //System.out.println(arr[i]);
        }
        //System.out.println(arr);
        return arr;
    }
    static long[] nextLongArray(int length) throws IOException {
        long[] arr = new long[length];
        for (int i=0; i<length; i++) {
            arr[i] = Long.parseLong( nextLine() );
        }
        return arr;
    }

}
