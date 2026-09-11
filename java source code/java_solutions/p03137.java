import java.util.*;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        //コード
        Scanner scanner = new Scanner(System.in);
        Integer N = Integer.parseInt(scanner.next());
        Integer M = Integer.parseInt(scanner.next());
        Long[] x = new Long[M];
        Long[] d = new Long[M];
        Arrays.fill(d, 0l);

        for (int i = 0; i < M; i++) {
            x[i] = scanner.nextLong();
        }

        Arrays.sort(x);

        for (int i = 0; i < M-1; i++) {
            d[i] = x[i+1] - x[i];
        }
        Arrays.sort(d, Comparator.reverseOrder());

        for (int i = 0; i < N-1; i++) {
            if (M == i) break;
            d[i] = 0l;
        }

        Long sum = Stream.of(d).mapToLong(Long::longValue).sum();
        System.out.println(sum);
    }
}
