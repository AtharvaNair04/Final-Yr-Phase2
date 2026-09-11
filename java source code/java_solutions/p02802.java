import java.util.*;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        //コード
        Scanner scanner = new Scanner(System.in);
        int N = Integer.valueOf(scanner.next());
        int M = Integer.valueOf(scanner.next());
        Integer WA_COUNT = 0;
        Integer AC_COUNT = 0;
        boolean[] AC = new boolean[N + 1];
        Integer[] WA = new Integer[N + 1];
        Arrays.fill(WA, 0);

        for (int i = 0; i < M; i++) {
            Integer P = Integer.parseInt(scanner.next());
            String S = scanner.next();

            if (AC[P]) continue;
            // まだACしていない問題でWAの場合は数える
            if ("WA".equals(S)) {
                WA[P]++;
            } else {
                // ACしたとき、この問題でのWAをペナルティとして数える
                AC[P] = true;
                AC_COUNT++;
                WA_COUNT += WA[P];
            }

        }
        System.out.println(AC_COUNT + " " + WA_COUNT);
    }
}
