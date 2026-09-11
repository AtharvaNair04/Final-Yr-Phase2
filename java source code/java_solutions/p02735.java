import java.io.*;
import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import java.util.Queue;

public class Main {

    public static void main(String[] args) {
        FastScanner fs = new FastScanner();
        int h = fs.nextInt();
        int w = fs.nextInt();
        String[][] ss = new String[h][w];
        // 特定の場所に入って来た時のカウント最小値
        int[][] dp = new int[h][w];
        for( int i = 0 ; i < h ; i++ ){
            ss[i] = fs.next().split("" );
            for( int j = 0 ; j < w ; j++ ){
                dp[i][j] = 10000;
            }
        }

        Queue<Integer> posQueue = new ArrayDeque();

        posQueue.add(0); // 縦座標 * 100 + 横座標
        dp[0][0] = "#".equals(ss[0][0]) ? 1 : 0;

        while( !posQueue.isEmpty() ) {

            // 今いる場所
            int item = posQueue.poll();
            int hPos = item / 100;
            int wPos = item % 100;
            int isWhite = "#".equals(ss[hPos][wPos]) ? 0 : 1;

            // 下を見る
            if (hPos != h - 1) {
                // 白い場所から黒い場所へ移る場合のみカウントを増加
                int addCount = (isWhite == 1 && "#".equals(ss[hPos + 1][wPos])) ? 1 : 0;
                // 次の場所へ移動する際に保持しているカウントよりも大きい場合は別ルートを通った方が良いのでループを省略
                if( dp[hPos][wPos] + addCount < dp[hPos+1][wPos] ) {
                    dp[hPos+1][wPos] = dp[hPos][wPos] + addCount;
                    posQueue.add((hPos + 1) * 100 + wPos);
                }
            }
            // 右を見る
            if (wPos != w - 1) {
                // 白い場所から黒い場所へ移る場合のみカウントを増加
                int addCount = (isWhite == 1 && "#".equals(ss[hPos][wPos + 1])) ? 1 : 0;
                // 次の場所へ移動する際に保持しているカウントよりも大きい場合は別ルートを通った方が良いのでループを省略
                if( dp[hPos][wPos] + addCount < dp[hPos][wPos+1] ) {
                    dp[hPos][wPos + 1] = dp[hPos][wPos] + addCount;
                    posQueue.add(hPos * 100 + wPos + 1);
                }
            }
        }

        System.out.println( dp[h-1][w-1] );

    }

    static class FastScanner {
        private final InputStream in = System.in;
        private final byte[] buffer = new byte[1024];
        private int ptr = 0;
        private int bufferLength = 0;

        private boolean hasNextByte() {
            if (ptr < bufferLength) {
                return true;
            } else {
                ptr = 0;
                try {
                    bufferLength = in.read(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bufferLength > 0;
            }
        }

        private int readByte() {
            if (hasNextByte()) {
                return buffer[ptr++];
            } else {
                return -1;
            }
        }

        private static boolean isPrintableChar(int c) {
            return 33 <= c && c <= 126;
        }

        boolean hasNext() {
            while (hasNextByte() && !isPrintableChar(buffer[ptr])) {
                ptr++;
            }
            return hasNextByte();
        }

        String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            StringBuilder sb = new StringBuilder();
            int b = readByte();
            while (isPrintableChar(b)) {
                sb.appendCodePoint(b);
                b = readByte();
            }
            return sb.toString();
        }

        long nextLong() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
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
            if (nl < Integer.MIN_VALUE || nl > Integer.MAX_VALUE) {
                throw new NumberFormatException();
            }
            return (int) nl;
        }

        public double nextDouble() {
            return Double.parseDouble(next());
        }
    }
}
