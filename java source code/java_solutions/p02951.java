// 問題URL
// https://atcoder.jp/contests/abc136/tasks/abc136_a

import java.util.Scanner;
import java.util.Arrays;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) {
        //コード
        Scanner scanner = new Scanner(System.in);
        Integer x = Integer.parseInt(scanner.next());
        Integer y = Integer.parseInt(scanner.next());
        Integer z = Integer.parseInt(scanner.next());
        Integer abs = Math.abs(x-y);
        if (abs >= z) {
            System.out.println(0);
        } else {
            System.out.println(Math.abs((x - y) - z));
        }

    }
}
