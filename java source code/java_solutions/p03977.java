import java.util.*;

class C {
    public static void main(String... args) {
        final Scanner sc = new Scanner(System.in);
        final int T = sc.nextInt();
        for (int i = 0; i < T; i++) {
            final int N = sc.nextInt();
            final int D = sc.nextInt();
            if(N % 2 == 1)
                System.out.println(D + (N - 1) * 127);
            else
                System.out.println((D ^ 127) + (N - 1) * 127);
        }
    }
}

public class Main {
    public static void main(String... args) {
        C.main();
    }
}
