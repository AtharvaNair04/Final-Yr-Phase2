/**
 * @author derrick20
 */
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String args[]) throws Exception {
        FastScanner sc = new FastScanner();
        PrintWriter out = new PrintWriter(System.out);

        int N = sc.nextInt();
        Bag left = new Bag();
        Bag right = new Bag();
        Interval[] intervals = new Interval[N];
        int min = (int) 1e9;
        int max = 0;
        for (int i = 0; i < N; i++) {
            int lo = sc.nextInt();
            int hi = sc.nextInt();
            min = Math.min(min, lo);
            max = Math.max(max, hi);
            intervals[i] = new Interval(i, lo, hi);
            right.loFreq.merge(lo, 1, Integer::sum);
            right.hiFreq.merge(hi, 1, Integer::sum);
        }
        Arrays.sort(intervals);

        Interval[] prefix = new Interval[N];
        Interval[] suffix = new Interval[N];
        prefix[0] = intervals[0];
        for (int i = 1; i < N; i++) {
            prefix[i] = intersect(intervals[i], prefix[i - 1]);
        }
        suffix[N - 1] = intervals[N - 1];
        for (int i = N - 2; i >= 0; i--) {
            suffix[i] = intersect(intervals[i], suffix[i + 1]);
        }

        int ans = 0;
        // Sweep through, transferring from right bag to left
        // There's two possible ways to be optimal. We split it down the middle
        // at some point sweeping through, or we just steal an individual contest
        // and leave the rest for the bad contest
        for (int i = 0; i < N; i++) {
            int lo = intervals[i].lo;
            int hi = intervals[i].hi;

            // Steal one contest, leave rest for the bad contest
            Interval remaining = new Interval(-1, min, max);
            if (i > 0) {
                remaining = intersect(remaining, prefix[i - 1]);
            }
            if (i < N - 1) {
                remaining = intersect(remaining, suffix[i + 1]);
            }
            ans = Math.max(ans, intervals[i].length() + remaining.length());
            left.loFreq.merge(lo, 1, Integer::sum);
            left.hiFreq.merge(hi, 1, Integer::sum);

            right.loFreq.merge(lo, -1, Integer::sum);
            if (right.loFreq.get(lo) == 0) {
                right.loFreq.remove(lo);
            }
            right.hiFreq.merge(hi, -1, Integer::sum);
            if (right.hiFreq.get(hi) == 0) {
                right.hiFreq.remove(hi);
            }

            if (left.loFreq.size() > 0 && right.loFreq.size() > 0) {
                // Now compute answer! But both bags must be size >= 1
                // Choose the most limiting of each boundary, i.e.
                // highest min, lowest max
                int leftAns = Math.max(0, left.hiFreq.firstKey() - left.loFreq.lastKey() + 1);
                int rightAns = Math.max(0, right.hiFreq.firstKey() - right.loFreq.lastKey() + 1);
                ans = Math.max(ans, leftAns + rightAns);
            }
        }
        out.println(ans);

        out.close();
    }

    static class Bag {
        TreeMap<Integer, Integer> loFreq, hiFreq;
        public Bag() {
            loFreq = new TreeMap<>();
            hiFreq = new TreeMap<>();
        }
    }

    static Interval intersect(Interval i1, Interval i2) {
        return new Interval(-1, Math.max(i1.lo, i2.lo), Math.min(i1.hi, i2.hi));
    }

    static class Interval implements Comparable<Interval> {
        int lo, hi;
        int idx;
        public Interval(int i, int l, int h) {
            idx = i; lo = l; hi = h;
        }
        public int compareTo(Interval i2) {
            // We want the intervals to be close together
//            return (int) (hi - i2.hi);
            return lo - i2.lo; // != 0 ? (lo - i2.lo);// : (int) (hi - i2.hi);
        }
        public int length() {
            return Math.max(0, hi - lo + 1);
        }
    }

    static class FastScanner {
        public int BS = 1<<16;
        public char NC = (char)0;
        byte[] buf = new byte[BS];
        int bId = 0, size = 0;
        char c = NC;
        double cnt = 1;
        BufferedInputStream in;

        public FastScanner() {
            in = new BufferedInputStream(System.in, BS);
        }

        public FastScanner(String s) {
            try {
                in = new BufferedInputStream(new FileInputStream(new File(s)), BS);
            }
            catch (Exception e) {
                in = new BufferedInputStream(System.in, BS);
            }
        }

        private char getChar(){
            while(bId==size) {
                try {
                    size = in.read(buf);
                }catch(Exception e) {
                    return NC;
                }
                if(size==-1)return NC;
                bId=0;
            }
            return (char)buf[bId++];
        }

        public int nextInt() {
            return (int)nextLong();
        }

        public long nextLong() {
            cnt=1;
            boolean neg = false;
            if(c==NC)c=getChar();
            for(;(c<'0' || c>'9'); c = getChar()) {
                if(c=='-')neg=true;
            }
            long res = 0;
            for(; c>='0' && c <='9'; c=getChar()) {
                res = (res<<3)+(res<<1)+c-'0';
                cnt*=10;
            }
            return neg?-res:res;
        }

        public double nextDouble() {
            double cur = nextLong();
            return c!='.' ? cur:cur+nextLong()/cnt;
        }

        public String next() {
            StringBuilder res = new StringBuilder();
            while(c<=32)c=getChar();
            while(c>32) {
                res.append(c);
                c=getChar();
            }
            return res.toString();
        }

        public String nextLine() {
            StringBuilder res = new StringBuilder();
            while(c<=32)c=getChar();
            while(c!='\n') {
                res.append(c);
                c=getChar();
            }
            return res.toString();
        }

        public boolean hasNext() {
            if(c>32)return true;
            while(true) {
                c=getChar();
                if(c==NC)return false;
                else if(c>32)return true;
            }
        }
    }
}
