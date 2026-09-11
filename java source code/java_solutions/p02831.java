// 問題URL
// https://atcoder.jp/contests/abc148/tasks/abc148_c

import java.util.Scanner;

public class Main {
    public static Long gcd(Long a, Long b) {
        if(a < b) return gcd(b,a);
        if(b == 0) return a;
        return gcd(b,a%b);
    }

    public static void main(String[] args) {
        //コード
        Scanner scanner = new Scanner(System.in);
        Long a = scanner.nextLong();
        Long b = scanner.nextLong();

        Long sum = gcd(a, b);
        System.out.println((a * b) / sum);
    }
}
