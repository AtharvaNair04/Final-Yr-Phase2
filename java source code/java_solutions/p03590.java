import java.io.IOException;
import java.util.Scanner;

/**
 * Created on 2017/09/30 21:46.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        // BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLong()) {
            int N = sc.nextInt();
            int K = sc.nextInt();
            int[] A = new int[N];
            int[] B = new int[N];

            for (int i = 0; i < N; i++) {
                A[i] = sc.nextInt();
                B[i] = sc.nextInt();
            }

            long ans = 0;

            while (true) {
                long sum = 0;
                for (int i = 0; i < N; i++) {
                    if ((K & (A[i])) == A[i]) {
                        sum += B[i];
                    }
                }
                ans = Math.max(sum, ans);

                int pos0 = -1;
                for (int i = 0; (1 << i) < K; i++) {
                    if (((1 << i) & K) == 0) {
                        pos0 = i;
                        break;
                    }
                }
                if (K == 0 || pos0 == -1) {
                    break;
                }
                K -= (1 << pos0);
            }
            System.out.println(ans);
        }
    }
}
