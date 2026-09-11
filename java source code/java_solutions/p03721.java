import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int n = scan.nextInt();
        long k = scan.nextLong();

        BitSet set = new BitSet();
        long[] array = new long[100000 + 1];
        for (int i = 0; i < n; ++i) {
            int a = scan.nextInt();
            int b = scan.nextInt();
            array[a] += b;
            set.set(a);
        }

        for (int i = set.nextSetBit(0);; i = set.nextSetBit(i + 1)) {
            k -= array[i];
            if (k <= 0) {
                System.out.println(i);
                break;
            }
        }
    }

}
