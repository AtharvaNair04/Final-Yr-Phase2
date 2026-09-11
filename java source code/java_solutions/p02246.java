import java.util.HashMap;
    import java.util.Scanner;
    public class Main {
        public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);
            n = 16;
            char[] s = new char[n];
            for (int i = 0; i < n; i++) {
                s[i] = (char)('0' + sc.nextInt());
            }
            ans = "123456789:;<=>?0";
            for (int i = 0; i < 46; i++) {
                hm = new HashMap<String, Integer>();
                limit = i;
                dfs(new Panel(toString(s), 0));
            }
            sc.close();
        }
        public static void dfs(Panel u) {
            if (u.s.equals(ans)) {
                System.out.println(u.count);
                System.exit(0);
            }
            if (u.count + u.min > limit) {
                return;
            }
            hm.put(u.s, u.count);
            StringBuffer str = new StringBuffer(u.s);
            int zero = str.indexOf("0");
            assert (zero != -1);
            for (int i = 0; i < d.length; i++) {
                if (zero + d[i] >= 0 && zero + d[i] < n && !(d[i] == -1 && (zero + d[i]) % 4 == 3) && !(d[i] == 1 && (zero + d[i]) % 4 == 0)) {
                    str.setCharAt(zero, str.charAt(zero + d[i]));
                    str.setCharAt(zero + d[i], '0');
                    String tmps = str.toString();
                    if (!hm.containsKey(tmps) || hm.get(tmps) > u.count + 1) {
                        dfs(new Panel(tmps, u.count + 1));
                    }
                    str.setCharAt(zero + d[i], str.charAt(zero));
                    str.setCharAt(zero, '0');
                }
            }
        }
        public static int n;
        public static int limit;
        public static String ans;
        public static final int[] d = {4, -4, -1, 1};
        public static HashMap<String, Integer> hm;
        public static class Panel {
            String s;
            int count;
            int min;
            Panel (String s, int count) {
                this.s = s;
                this.count = count;
                this.min = getMin(s);
            }
            public int getMin(String s) {
                char[] c = s.toCharArray();
                int ret = 0;
                for (int i = 0; i < c.length; i++) {
                    int tmp = c[i] - '0';
                    if (tmp != 0) {
                        int x = (tmp - 1) % 4;
                        int y = (tmp - 1) / 4;
                        ret += Math.abs(x - i % 4) + Math.abs(y - i / 4);
                    }
                }
                return ret;
            }
        }
        public static String toString(char[] s) {
            String ret = "";
            for (int i = 0; i < s.length; i++) {
                ret += s[i];
            }
            return ret;
        }
    }
