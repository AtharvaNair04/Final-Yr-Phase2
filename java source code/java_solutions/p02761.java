import java.util.Scanner;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        //コード
        Scanner scanner = new Scanner(System.in);
        Integer N = Integer.parseInt(scanner.next());
        Integer M = Integer.parseInt(scanner.next());
        Integer[] digit = new Integer[N];

        Arrays.fill(digit, -1);
        for (int i = 0; i < M; i++) {
            Integer S = Integer.parseInt(scanner.next());
            Integer C = Integer.parseInt(scanner.next());
            if(digit[S-1] == -1 || digit[S-1] == C) {
                digit[S-1] = C;
            } else {
                System.out.println(-1);
                System.exit(0);
            }
        }

        boolean first = true;
        for (int i = 0; i < digit.length; i++) {
            if(digit[0] == 0 &&  digit.length > 1) {
                System.out.println(-1);
                System.exit(0);
            } else if(digit[i] == -1 && first && digit.length > 1) {
                digit[i] = 1;
            } else {
                if(digit[i] == -1)  digit[i] = 0;
            }
            first = false;
        }

        String kekka = new String();
        for (Integer n: digit) kekka += n;
        System.out.println(kekka);

    }
}
