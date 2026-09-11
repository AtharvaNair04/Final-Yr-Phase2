// 問題URL
// https://atcoder.jp/contests/abc145/tasks/abc145_a

import java.util.Scanner;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        //コード
        Scanner scanner = new Scanner(System.in);
        // 半径1の円の面積=1*1*3.14
        double area = 3.14;
        int r = Integer.parseInt(scanner.next());
        int result = (int)(Math.ceil(r*r*3.14/area));
        System.out.println(result);
    }
}
