// 問題URL
// https://atcoder.jp/contests/abc127/tasks/abc127_c

import java.util.*;

public class Main {
    public static void main(String[] args) {
        //コード
        Scanner scanner = new Scanner(System.in);
        Integer N = Integer.parseInt(scanner.next());
        Integer M = Integer.parseInt(scanner.next());
        int LIMIT = 100000;
        int[] S = new int[LIMIT+1];


        for(int i = 0; i < M; i++) {
            Integer start = Integer.parseInt(scanner.next());
            Integer end= Integer.parseInt(scanner.next());
            S[start] += 1;
            if(end != LIMIT) {
                S[end + 1] -= 1;
            }
        }

        int count = 0;
        for(int i = 1; i <= LIMIT; i++) {
            S[i] += S[i-1];
            if(S[i] == M) count++;
        }

        System.out.println(count);
    }
}
