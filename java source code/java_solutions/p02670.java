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
    // static Node[] map;
    static boolean[] still;
    static int[] glen;
    static int[][] gnext;


    public static void main(String[] args) {
        FastScanner sc = new FastScanner();
        n = sc.nextInt();
        // map = new Node[n * n + 1];
        // int[] p = new int[n*n];
        still = new boolean[n*n + 1];
        glen = new int[n * n + 1];
        gnext = new int[n*n+1][4];
        Arrays.fill(still, true);

        for (int i = 1; i <= n*n; i++) {
            int len = (i-1) / n;
            len = Math.min(len, (i%n) - 1);
            len = Math.min(len, n - (i - 1) / n - 1);
            len = Math.min(len, n - (i % n));
            if(i % n == 0) len = 0;
            glen[i] = len;
            // map[i] = new Node(i);

            // up
            if(i <= n){
                // map[i].addNext(0);
                gnext[i][0] = -1;
            }else{
                // map[i].addNext(i - n);
                gnext[i][0] = i-n;
            }

            // left
            if(i % n == 1){
                // map[i].addNext(0);
                gnext[i][1] = -1;
            }else{
                // map[i].addNext(i - 1);
                gnext[i][1] = i-1;
            }

            // down
            if(i > n*n - n){
                // map[i].addNext(0);
                gnext[i][2] = -1;
            }else{
                // map[i].addNext(i + n);
                gnext[i][2] = i+n;
            }

            // right
            if(i % n == 0){
                // map[i].addNext(0);
                gnext[i][3] = -1;
            }else{
                // map[i].addNext(i + 1);
                gnext[i][3] = i+1;
            }
        }

        long ans = 0;
        for (int i = 0; i < n * n; i++) {
            int p = sc.nextInt();
            int mlen = glen[p];
            still[p] = false;
            ans += mlen;
            updateLen(p, mlen);
        }

        System.out.println(ans);
    }

    static void updateLen(int p, int len){
        for(int i = 0; i < 4; i++){
            int mnext = gnext[p][i];
            if(mnext < 0) continue;
            if(glen[mnext] > len){
                glen[mnext] = len;
            // if(map[mnext].len > len){
            //     map[mnext].len = len;
                if(still[mnext]){
                    updateLen(mnext, len+1);
                }else{
                    updateLen(mnext, len);
                }
            }
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

class Node{
    int id;
    // int len;
    ArrayList<Integer> next;

    public Node(int id){
        this.id = id;
        // this.len = len;
        next = new ArrayList<Integer>();
    }

    public void addNext(int next){
        this.next.add(next);
    }
}
