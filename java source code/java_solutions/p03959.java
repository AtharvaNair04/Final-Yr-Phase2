import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.lang.Math;

public class Main {
    public static void main(String[] args) {
        FS reader = new FS();
        // write reader
        int N = reader.nextInt();
        int[] T = new int[N];
        int[] A = new int[N];
        for (int i=0; i<N; i++) {
            T[i] = reader.nextInt();
        }
        for (int i=0; i<N; i++) {
            A[i] = reader.nextInt();
        }
        
        solver(N, T, A);
    }
    
    public static void solver(int N, int[] T, int[] A) {
        // write logic
        int[] pos = new int[N];
        for (int i=0; i<N; i++) {
            if (i == 0) pos[i] = T[i];
            else {
                if (T[i-1] < T[i]) {
                    pos[i] = T[i];
                }
            }
        }
        
        for (int i=N-1; i>=0; i--) {
            if (i == N-1 || A[i+1] < A[i]) {
                // try to update
                if ((pos[i] == 0 && A[i] > T[i]) || (pos[i] != 0 && pos[i] != A[i])) {
                    System.out.println(0);
                    return;
                }
                pos[i] = A[i];
            } else {
                // no update
                if (pos[i] != 0) {
                    if (pos[i] > T[i]) {
                        System.out.println(0);
                    }
                }
            }
        }
        
        long ans = 1;
        final long MOD = 1000000007;
        for (int i=0; i<N; i++) {
            if (pos[i] == 0) {
                ans = (ans * Math.min(T[i], A[i])) % MOD;
            }
        }
        System.out.println(ans);
    }
    
    // Read Class
    static class FS {
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
    
        private int readByte() { return hasNextByte() ? buffer[ptr++] : -1;}
        private boolean isPrintableChar(int c) {return 33 <= c && c <= 126;}
        private void skipUnprintable() {while(hasNextByte() && !isPrintableChar(buffer[ptr])) ptr++;}
    
        public boolean hasNext() { skipUnprintable(); return hasNextByte();}
        public String next() {
            if (!hasNext()) throw new NoSuchElementException();
            StringBuilder sb = new StringBuilder();
            int b = readByte();
            while(isPrintableChar(b)) {
                sb.appendCodePoint(b);
                b = readByte();
            }
            return sb.toString();
        }
        public int nextInt() {
            return (int)nextLong();
        }
        public long nextLong() {
            if (!hasNext()) throw new NoSuchElementException();
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
            while(true) {
                if ('0' <= b && b <= '9') {
                    n *= 10;
                    n += b - '0';
                } else if(b == -1 || !isPrintableChar(b)) {
                    return minus ? -n : n;
                } else {
                    throw new NumberFormatException();
                }
                b = readByte();
            }
        }
    }
}
