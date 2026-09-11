import java.util.*;

class Main {
    static int W;
    static int H;
    static int s[][];
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        H = sc.nextInt();
        W = sc.nextInt();
        s = new int[H][W];
        for (int i = 0; i < H; i++) {
            String line = sc.next();
            for (int j = 0; j < W; j++) {
                char c = line.charAt(j);
                if (c == '.') {
                    s[i][j] = 0;
                } else {
                    s[i][j] = c - '0';
                }
            }
        }
        int count = 0;
        Set<Integer> set = rush0();
        while (set.size() > 0) {
            for(int x: set) {
                int i = x >> 16;
                int j = x & 0xffff;
                s[i][j] = 0;
            }
            count++;
            set = rush1(set);
        }
        System.out.printf("%d\n", count);
    }

    static Set<Integer> rush1(Set<Integer> set) {
        Set<Integer> set2 = new HashSet<Integer>();
        for(int x: set) {
            int p = x >> 16;
            int q = x & 0xffff;
            for (int i = p-1; i <= p+1; i++) {
                for (int j = q-1; j <= q+1; j++) {
                    if (i == p && j == q) continue;
                    if (s[i][j] > 0 && count(i, j) >= s[i][j]) {
                        set2.add(i << 16 | j);
                    }
                }
            }
        }
        return set2;
    }

    static int count(int p, int q) {
        int c = 0;
        for (int i = p-1; i <= p+1; i++) {
            for (int j = q-1; j <= q+1; j++) {
                if (s[i][j] == 0) c++;
            }
        }
        return c;
    }

    static Set<Integer> rush0() {
        Set<Integer> set = new HashSet<Integer>();
        for (int i = 1; i < H-1; i++) {
            for (int j = 1; j < W-1; j++) {
                if (s[i][j] > 0 && count(i, j) >= s[i][j]) {
                    set.add(i << 16 | j);
                }
            }
        }
        return set;
    }
}
