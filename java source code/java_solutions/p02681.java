import java.util.*;
import java.io.*;
public class Main {
    static PrintWriter pw  = new PrintWriter(new OutputStreamWriter(System.out));
     
    public static void main(String[] args) {
        MyScanner ms = new MyScanner();
        String T = ms.nextLine();
        String S = ms.nextLine();
        boolean game  = true;
        for(int i=0;i<T.length();i++){
            if(S.charAt(i)!=T.charAt(i))
            game = false;
        }
        pw.println(game?"Yes":"No");
        pw.flush();
    }
    public static class MyScanner {
        BufferedReader br;
        StringTokenizer st;
   
        public MyScanner() {
           br = new BufferedReader(new InputStreamReader(System.in));
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
   
        String nextLine(){
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
