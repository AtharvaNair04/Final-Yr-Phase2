import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String S = scanner.next();
        String T = scanner.next();
        if (S.length() - T.length() < 0) {
            System.out.println("UNRESTORABLE");
            return;
        }
        String ans = "";
        for (int i = S.length() - T.length(); i >= 0; i--) {
            boolean flag = true;
            for (int j = i; j < i + T.length(); j++) {
                flag &= S.charAt(j) == '?' || S.charAt(j) == T.charAt(j - i);
            }
            if (flag) {
                for (int k = 0; k < S.length(); k++) {
                    if (k == i) {
                        ans += T;
                        k += T.length() - 1;
                    } else {
                        ans += S.charAt(k) == '?' ? 'a' : S.charAt(k);
                    }
                }
                System.out.println(ans);
                return;
            }
        }
        System.out.println("UNRESTORABLE");
    }
}
