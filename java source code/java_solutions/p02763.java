//
import java.math.*;
import java.util.*;
import java.io.*;
  
public class Main { 
  
    static BufferedReader in;
    static PrintWriter out = new PrintWriter(System.out);
    static String file = "../in";
    static int test = 10; // 0 for local testing, 1 for std input
    static int inf = 1_000_000, mod = 1_000_000_007;

    void swap(int[]ary, int i, int j)
    {
        int t = ary[i];
        ary[i] = ary[j];
        ary[j] = t;
    }
    
    String[] split() throws Exception
    {
        return in.readLine().split(" ");
    }

    int readInt() throws Exception
    {
        return Integer.valueOf(in.readLine());
    }

    int[] toIntArray() throws Exception
    {
        String[] sp = split();
        int n = sp.length;
        int[] ary = new int[n];
        for(int i = 0; i < n; i++) ary[i] = Integer.valueOf(sp[i]);
        return ary;
    }

    String reverse(String str)
    {
        return new StringBuilder(str).reverse().toString();
    }


    long pow(long a, int pow)
    {
        long res = 1;
        while(pow > 0)
        {
            if(pow % 2 != 0) {
                res *= a;
                res %= mod;
            }
            a = a * a;
            a %= mod;
            pow /= 2;
        }

        return res;
    }

    public static void main(String[] args) throws Exception
    {
        int _k = Integer.valueOf("1");
        if(test > 0) in = new BufferedReader(new InputStreamReader(System.in));
        else in = new BufferedReader(new FileReader(file));
        if(test < 0) {String[] str = in.readLine().split(" ");}
/***********************************************************************/
/***********************************************************************/
/***********************************************************************/
/***********************************************************************/
        // System.out.println((-100 + 0) / 2);

        new Main().e();
        out.flush();
    }

    void e() throws Exception
    {
        int n = readInt();
        String str = in.readLine();
        char[] chs = new char[n + 1];

        long[][] bit = new long[26][500010];

        chs[0] = '-';
        for(int i = 0; i < n; i++)
        {
            chs[i + 1] = str.charAt(i);
            update(chs[i + 1], 1, i + 1, bit);
        }

        int q = readInt();
        
        while(q-- > 0)
        {
            String[] split = in.readLine().split(" ");
            if(split[0].equals("1"))
            {
                int pos = Integer.valueOf(split[1]);
                char c = split[2].charAt(0);
                char original = chs[pos];
                update(original, -1, pos, bit);
                update(c, 1, pos, bit);
                chs[pos] = c;
                // System.out.println(Arrays.toString(bit[original - 'a']));
                // System.out.println(Arrays.toString(bit[c - 'a']));
            }
            else
            {
                int from = Integer.valueOf(split[1]), 
                    to = Integer.valueOf(split[2]);
                long sum = 0;
                for(int i = 0; i < 26; i++)
                {
                    long a = query0(i, bit, to),
                        b = query0(i, bit, from - 1);
                    
                    // System.out.printf("..%c %d %d\n", (char)('a' + i), a, b);

                    if(a - b > 0) sum ++;
                }
                // System.out.println("_____" + sum);
                out.println(sum);
            }
        }
    }

    void update(char c, int diff, int pos, long[][] bit)
    {
        int size = 500000;
        int idx = c - 'a';
        while(pos <= size)
        {
            bit[idx][pos] += diff;
            pos += pos & -pos;
        }
    }

    long query0(int idx, long[][] bit, int from)
    {
        long sum = 0;
        while(from > 0)
        {
            sum += bit[idx][from];
            from -= from & -from;
        }
        return sum;
    }

    void d() throws Exception{
        int[] ary = toIntArray();
        int n = ary[0], m = ary[1], k = ary[2];
        int[] f = new int[n];
        int[] friends = new int[n];
        int[] cand = new int[n];

        for(int i = 0; i < n; i++) f[i] = i;
        while(m-- > 0)
        {
            ary = toIntArray();
            int a = ary[0] - 1, b = ary[1] - 1;
            friends[a]++;
            friends[b]++;
            a = find(f, a);
            b = find(f, b);
            // if(a != b) 
            f[a] = b;
            // System.out.println(Arrays.toString(f));
        }
        // block
        
        Map<Integer, Integer> map = new HashMap<>();
        
        for(int i = 0; i < n; i++)
        {
            int p = find(f, i);
            map.put(p, map.getOrDefault(p, 0) + 1);
        }

        for(int i = 0; i < n; i++)
        {
            int p = find(f, i);
            cand[i] = map.get(p) - 1;
        }

        // System.out.println(Arrays.toString(cand));

        while(k-- > 0)
        {
            ary = toIntArray();
            int a = ary[0] - 1, b = ary[1] - 1;
            int pa = find(f, a);
            int pb = find(f, b);
            if(pa == pb)
            {
                cand[a]--;
                cand[b]--;
            }
        }

        // System.out.println(Arrays.toString(friends));
        
        // System.out.println(map);       

        for(int i = 0; i < n; i++)
        {
            cand[i] -= friends[i];
        }

        for(int c : cand) out.printf("%d ", c);

    }

    int find(int[] f, int p)
    {
        int fp = f[p];
        while(fp != f[fp]) fp = f[f[fp]];
        return f[p] = fp;
    }

    void c() throws Exception
    {
        int[] ary = toIntArray();
        int n = ary[0], m = ary[1];

        int[] digits = new int[n];
        Arrays.fill(digits, -1);
        int[] vis = new int[n];
        while(m-- > 0)
        {
            ary = toIntArray();
            int s = ary[0] - 1, c = ary[1];
            if(digits[s] > 0 && digits[s] != c)
            {
                out.println(-1);
                return;
            }
            // vis[s]++;
            digits[s] = c;
        }
        if(digits[0] == 0 && n > 1)
        {
            out.println(-1);
            return;
        }
        for(int i = 0; i < n; i++)
        {
            if(digits[i] < 0) digits[i] = 0;
        }
        if(digits[0] == 0 && n > 1) digits[0] = 1;
        for(int k : digits) out.printf("%d", k);

    }
    void b() throws Exception
    {
        int[][] g = new int[3][];
        for(int i = 0; i < 3; i++)
        {
            g[i] = toIntArray();
        }
        int n = readInt();
        boolean[][] v = new boolean[3][3];
        while(n-- > 0)
        {
            int b = readInt();
            for(int i = 0; i < 3; i++)
            {
                for(int j = 0; j < 3; j++)
                {
                    if(g[i][j] == b) v[i][j] = true;
                }
            }
        }

        boolean ans = false;
        for(int i = 0; i < 3; i++)
        {
            boolean col = true;
            for(int j = 0; j < 3; j++)
            {
                col &= v[i][j];
            }
            if(col) ans = true;

            boolean row = true;
            for(int j = 0; j < 3; j++)
            {
                row &= v[j][i];
            }
            if(row) ans = true;
        }

        if(v[0][0] && v[1][1] && v[2][2]) ans = true;
        if(v[0][2] && v[1][1] && v[2][0]) ans = true;
        if(ans) out.println("Yes");
        else out.println("No");
    }
    void a() throws Exception
    {
        int n = readInt();
        
        out.println((n + 1) / 2);
    }
}
