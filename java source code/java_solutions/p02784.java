// https://atcoder.jp/contests/abc153/tasks/abc153_b

import java.util.*;
import java.lang.*;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) {
        //コード
        Scanner scanner = new Scanner(System.in);
        Integer x = Integer.parseInt(scanner.next());
        Integer y = Integer.parseInt(scanner.next());
        Integer[] a = new Integer[y];
        for(int i=0; i < y; i++) {
            a[i] = Integer.parseInt(scanner.next());
        }

        int sum = Stream.of(a).mapToInt(Integer::intValue).sum();
        if (sum >= x) {
            System.out.println("Yes");
        } else {
            System.out.println("No");
        }

    }
}
