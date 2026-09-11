import java.util.*;
import java.lang.*;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) {
        //コード
        Scanner scanner = new Scanner(System.in);
        Integer n = Integer.parseInt(scanner.next());
        Integer l = Integer.parseInt(scanner.next());


        Integer[] taste = new Integer[n];
        for(int i = 1; i <= n; i++) {
            taste[i-1] = l + i - 1;
        }


        int max = Math.abs(taste[0]);
        int min = Math.abs(taste[1]);
        for(int i = 0; i < taste.length; i++) {
            int v = Math.abs(taste[i]);
            if (v > max) {
                max = v;
            }
            if (v < min) {
                min = v;
            }
        }

        int sum = Stream.of(taste).mapToInt(Integer::intValue).sum();

        if( sum < 0) {
            System.out.println(sum + min);
        } else {
            System.out.println(sum - min);
        }

    }
}
