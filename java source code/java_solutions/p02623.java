import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
            final FastReader reader = new FastReader();
            final Solver solver = new Solver();
            final StringBuilder result = new StringBuilder();
            int t = 1;
            while(t-- > 0) {
                int n = reader.nextInt();
                int m = reader.nextInt();
                long k = reader.nextLong();
                long[] d1 = new long[n];
                long[] d2 = new long[m];
                int d1max = 0;
                int d2max = 0;
                for(int i = 0 ; i < n ; i++) {
                    d1[i] = reader.nextInt();
                }
                for (int i = 0 ; i < m ; i++) {
                    d2[i] = reader.nextInt();
                }
                for(int i = 0 ; i < n ; i++) {
                    if(i>0)
                        d1[i] = d1[i-1] + d1[i];
                    if(d1[i] <= k)
                        d1max = i+1;
                }
                for (int i = 0 ; i < m ; i++) {
                    if(i > 0)
                        d2[i] = d2[i-1] + d2[i];
                    if(d2[i] <= k)
                        d2max = i+1;
                }
                result.append(solver.solve(n , m , k , d1 , d2 , d1max , d2max)).append("\n");
            }
            System.out.println(result);
        }
    
    
        private static class Solver {
            public String solve(int n , int m , long k , long[] d1 , long[] d2 , int d1max , int d2max){
                long max = Math.max(d1max , d2max);
                int i = d1max - 1, j = 0;
                while (i >= 0 && j < d2max){
                    if(d2[j] + d1[i] > k){
                        i--;
                    } else {
                        max = Math.max(max , (i+j+2));
                        j++;
                    }
                }
                i = d2max - 1;
                j = 0;
                while (i >= 0 && j < d1max){
                    if(d1[j] + d2[i] > k){
                        i--;
                    } else {
                        max = Math.max(max , (i+j+2));
                        j++;
                    }
                }
                String result = "" + max;
                return result;
            }
        }
    
    
        /*
        * Helper Classes
         */
        //Fast Reader
        private static class FastReader {
            BufferedReader br;
            StringTokenizer st;
    
            public FastReader() {
                br = new BufferedReader(new InputStreamReader(System.in));
            }
    
            public String next() {
                while(st == null || !st.hasMoreElements()) {
                    try {
                        st = new StringTokenizer(br.readLine());
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
                return st.nextToken();
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
    
            public float nextFloat() {
                return Float.parseFloat(next());
            }
    
            public String nextLine() {
                String str = "";
                try {
                    str = br.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                st = null;
                return str;
            }
    
            public void resetST(){
                st = null;
            }
        }
}
