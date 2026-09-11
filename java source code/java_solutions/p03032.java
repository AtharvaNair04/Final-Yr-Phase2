import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int N = sc.nextInt();
        int K = sc.nextInt();
        long[] V = new long[N];
        for (int i=0; i<N; i++){
            V[i] = sc.nextInt();
        }
        sc.close();

        // l + r - d

        long ans = Long.MIN_VALUE;

        for(int l=0; l<=K; l++){
            for (int r=0; r<=K-l; r++){
                if (l+r > N) break;

                ArrayList<Long> s = new ArrayList<>();
                int d = K - l - r;
                long now =0;

                for (int i=0; i<=l-1; i++){
                    now += V[i];
                    s.add(V[i]);
                }
                for (int i=N-r; i<N; i++){
                    now += V[i];
                    s.add(V[i]);
                }

                Collections.sort(s);

                for (int i=0; i<d; i++){
                    if (i >= s.size()) break;
                    long tmp = s.get(i);
                    if (tmp > 0) break;
                    now -= tmp;
                }
                ans = Math.max(ans, now);
            }
        }
        System.out.println(ans);

    }
}
