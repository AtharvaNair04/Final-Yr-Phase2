// 問題URL
//

import java.util.Scanner;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        //コード
        Scanner scanner = new Scanner(System.in);
        Integer a = Integer.parseInt(scanner.next());
        Integer b = Integer.parseInt(scanner.next());
        int result =  -1;
        if (a < 10 && b < 10) {
            result = a * b;
        }

        System.out.println(result);
    }
}
