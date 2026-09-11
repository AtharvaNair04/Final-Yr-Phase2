import java.util.*;

public class Main {
    public static void main(String[] args) {
        //コード
        Scanner scanner = new Scanner(System.in);
        Integer N = Integer.parseInt(scanner.next());
        Integer K = Integer.parseInt(scanner.next());
        Integer[] H = new Integer[N];

        for (int i = 0; i < N; i++) H[i] = Integer.parseInt(scanner.next());

        Arrays.sort(H,  Collections.reverseOrder());
        Long count = 0L;
        for (Integer i = K; i < N; i++) {
            count += H[i];
        }
        System.out.println(count);
    }
}
