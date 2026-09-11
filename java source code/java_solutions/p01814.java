import java.io.*;

/**
 * AIZU ONLINE JUDGE
 * 2711 Nearly Cyclic String
 *    2018/02/16
 */
public class Main {

    long[] hash;
    long[] base;
    int K = 29;

    // lからrのハッシュを求める
    long getHash(int l, int r) {
        return hash[r + 1] - hash[l] * base[r - l + 1];
    }

    // lからとl+tからsize文字比較して最初の不一致文字を返す
    int comp(int l, int t, int size) {
        if (getHash(l, l + size - 1) == getHash(l+t, l+t + size - 1)) {
            return -1;
        }

        int s1 = l;
        int s2 = l + size - 1;
        while(s1 <= s2) {
            int m = (s1 + s2) / 2;
            if (getHash(l, m) == getHash(l+t, m+t)) {
                s1 = m + 1;
            }
            else {
                s2 = m-1;
            }
        }
        return s1;
    }

    boolean main() throws IOException {

        String S = systemin.readLine();
        int[] ir = readIntArray();

        hash = new long[S.length() + 1];
        base = new long[S.length() + 1];

        hash[0] = 0;
        base[0] = 1;
        for(int i = 0; i < S.length(); i++) {
            hash[i + 1] = hash[i] * K + S.charAt(i);
            base[i + 1] = base[i] * K;
        }

        int Q = ir[0];
        for(int q = 0; q < Q; q++) {
            ir = readIntArray();
            int l = ir[0] - 1;
            int r = ir[1] - 1;
            int t = ir[2];

            //log.printf("-----\nS.len = %d l, r, t = %d, %d, %d\n", S.length(), l, r, t);
            int size = r - l + 1 - t;
            int c1 = comp(l, t, size);
            int res = 1;
            if (c1 >= 0) {
                //log.printf("c1 %d '%s' '%s'\n", c1, S.charAt(c1), S.charAt(c1 + t));
                l = c1 + 1;
                size = r - l + 1 - t;
                int c2 = comp(l, t, size);
                if (c2 >= 0) {
                    //log.printf("c2 %d '%s' '%s'\n", c2, S.charAt(c2), S.charAt(c2 + t));
                    if (c2 != c1 + t) {
                        res = 0; // だめ
                    }
                    else if (c2 + t <= r && S.charAt(c1) != S.charAt(c2 + t)) {
                        //log.printf("dame\n");
                        res = 0; // だめ
                    }
                    else {
                        l = c2 + 1;
                        size = r - l + 1 - t;
                        int c3 = comp(l, t, size);
                        if (c3 >= 0) {
                            res = 0; // NG
                        }
                    }
                }
            }

            if (res > 0)
                result.printf("Yes\n");
            else
                result.printf("No\n");
        }

        //return true; // 正常終了 次へ
        return false;
    }

	static long time0;

    PrintStream log;
    PrintStream result;
    BufferedReader systemin;

    static Main instance = new Main();

    Main() {
        systemin = new BufferedReader(new InputStreamReader(System.in));
        result = System.out;
        log = new PrintStream(new OutputStream() { public void write(int b) {} } );
    }

	public static void main(String[] args) throws IOException {

//	    instance.main0();
//	    if (true)
//	        System.exit(0);

		int N = Integer.MAX_VALUE;
		//int N = readIntArray()[0];

		for(int i = 0; i < N; i++) {
			boolean b = instance.main();
			if (!b)
				break;
		}

        instance.systemin.close();
	}

	// 標準入力より1行分の区切り文字区切りでの整数値を読む
	// EOFの場合はnullを返す
	private int[] readIntArray() throws IOException {

		String s = null;
		for(;;) {
			s = systemin.readLine();
			if (s == null)
				return null;
			s = s.trim();
			if (s.length() != 0) // 突然空行を読むことがある。読み飛ばすとうまくいくらしい
				break;
		}

		String[] sp = s.split("[ ,]"); // 区切り文字はスペースかカンマ
		int[] a = new int[sp.length];
		for(int i = 0; i < sp.length; i++) {
			a[i] = Integer.parseInt(sp[i]);
		}
		return a;
	}

}
