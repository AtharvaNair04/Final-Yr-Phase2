import java.util.Scanner;

class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        String[] s = new String[N];
        for (int i = 0; i < N; i++) {
            s[i] = scanner.next();
        }
        int ans = 0;
        int cntB = 0;
        int cntA = 0;
        boolean flag = false;
        for (int i = 0; i < N; i++) {
            if (s[i].charAt(0) == 'B') {
                cntB++;

                if (s[i].charAt(s[i].length() - 1) != 'A') {
                    flag = true;
                }
            }
            if (s[i].charAt(s[i].length() - 1) == 'A') {
                cntA++;

                if (s[i].charAt(0) != 'B') {
                    flag = true;
                }
            }
            for (int j = 0; j < s[i].length() - 1; j++) {
                if (s[i].substring(j, j + 2).equals("AB")) {
                    ans++;
                }
            }
        }
        if (!flag && cntA == cntB) {
            if (cntA != 0) {
                ans += cntA - 1;
            }
        } else {
            ans += Math.min(cntA, cntB);
        }
        System.out.println(ans);
    }

}
