import java.io.*;
import java.util.*;

class Main {

    public static void main(String[] args) {
        MyScanner sc = new MyScanner();
        int A = sc.nextInt();
        int B = sc.nextInt();

        int ans = 0;
        if (B == 1){
            System.out.println(0);
            return;
        }
        if (B <= A){
            System.out.println(1);
            return;
        }
        B -= A;
        ans = 1;
        while (B>0) {
            B -= A-1;
            ans++;
        }
        System.out.println(ans);
    }

    static class MyScanner {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in), 1 << 15);
        StringTokenizer tokenizer;
    
        String next() {
            try {
                while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                    tokenizer = new StringTokenizer(reader.readLine());
                }
            } catch (IOException ignored) {
            }
            return tokenizer.nextToken();
        }
    
        int nextInt() {
            return Integer.parseInt(next());
        }
    
        long nextLong() {
            return Long.parseLong(next());
        }
    }
}
