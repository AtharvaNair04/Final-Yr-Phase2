import java.util.Scanner;

//0571
class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        char[] s = sc.next().toCharArray(), t = "JOI".toCharArray();
        int n = s.length, p = 0, max = 0;
        while (p < n) {
            if (s[p] == t[0]) {
                int[] c = new int[3];
                for (int i = 0; i < 3; i++) {
                    while (p < n && s[p] == t[i]) {
                        c[i]++;
                        p++;
                    }
                }
                max = Math.max(max, c[0] >= c[1] && c[2] >= c[1] ? c[1] : 0);
            } else {
                p++;
            }
        }
        System.out.println(max);
    }
}
