import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

public class Main {
    static int n;
    static int[] move;
    public static void main(String[] args) {
        FastScanner sc = new FastScanner();
        n = sc.nextInt();
        long a = sc.nextLong();
        long b = sc.nextLong();
        long c = sc.nextLong();
        long[] t = new long[]{a, b, c};
        move = new int[n];
        String[] abc = new String[]{"A", "B", "C"};
        String[] abc2 = new String[]{"BC", "AC", "AB"};
        for (int i = 0; i < n; i++) {
            String s = sc.next();
            for (int j = 0; j < abc2.length; j++) {
                if(s.equals(abc2[j])) move[i] = j;
            }
        }

        int[] ans = new int[n];

        for (int i = 0; i < n; i++) {
            int m1 = (move[i] + 1) % 3;
            int m2 = (move[i] + 2) % 3;
            if(t[m1] == 0 && t[m2] == 0){
                System.out.println("No");
                return;
            }else if(t[m1] == 1 && t[m2] == 1 && i < n-1){
                if(move[i] == move[i+1]){
                    ans[i] = m1;
                    t[m1]++;
                    t[m2]--;
                }else if(move[i] == (move[i+1] + 1) % 3){
                    ans[i] = m1;
                    t[m1]++;
                    t[m2]--;
                }else{
                    ans[i] = m2;
                    t[m2]++;
                    t[m1]--;
                }
            }else{
                if(t[m1] >= t[m2]){
                    ans[i] = m2;
                    t[m1]--;
                    t[m2]++;
                }else{
                    ans[i] = m1;
                    t[m2]--;
                    t[m1]++;
                }
            }
        }
        
        System.out.println("Yes");
        for (int i = 0; i < ans.length; i++) {
            System.out.println(abc[ans[i]]);
        }
    }


    private static class FastScanner {
        private final InputStream in = System.in;
        private final byte[] buffer = new byte[1024];
        private int ptr = 0;
        private int buflen = 0;

        private boolean hasNextByte() {
            if (ptr < buflen) {
                return true;
            } else {
                ptr = 0;
                try {
                    buflen = in.read(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (buflen <= 0) {
                    return false;
                }
            }
            return true;
        }

        private int readByte() {
            if (hasNextByte())
                return buffer[ptr++];
            else
                return -1;
        }

        private static boolean isPrintableChar(int c) {
            return 33 <= c && c <= 126;
        }

        public boolean hasNext() {
            while (hasNextByte() && !isPrintableChar(buffer[ptr]))
                ptr++;
            return hasNextByte();
        }

        public String next() {
            if (!hasNext())
                throw new NoSuchElementException();
            StringBuilder sb = new StringBuilder();
            int b = readByte();
            while (isPrintableChar(b)) {
                sb.appendCodePoint(b);
                b = readByte();
            }
            return sb.toString();
        }

        public long nextLong() {
            if (!hasNext())
                throw new NoSuchElementException();
            long n = 0;
            boolean minus = false;
            int b = readByte();
            if (b == '-') {
                minus = true;
                b = readByte();
            }
            if (b < '0' || '9' < b) {
                throw new NumberFormatException();
            }
            while (true) {
                if ('0' <= b && b <= '9') {
                    n *= 10;
                    n += b - '0';
                } else if (b == -1 || !isPrintableChar(b)) {
                    return minus ? -n : n;
                } else {
                    throw new NumberFormatException();
                }
                b = readByte();
            }
        }

        public int nextInt() {
            long nl = nextLong();
            if (nl < Integer.MIN_VALUE || nl > Integer.MAX_VALUE)
                throw new NumberFormatException();
            return (int) nl;
        }

        public double nextDouble() {
            return Double.parseDouble(next());
        }
    }

}
