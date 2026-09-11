import java.util.*;

public class Main {

    private void solve() {
        Scanner sc = new Scanner(System.in);
        int X = sc.nextInt();
        int Y = sc.nextInt();
        int Z = sc.nextInt();
        int K = sc.nextInt();

        long[] A = new long[X];
        long[] B = new long[Y];
        long[] C = new long[Z];
        for (int i = 0; i < X; i++) {
            A[i] = sc.nextLong();
        }
        for (int i = 0; i < Y; i++) {
            B[i] = sc.nextLong();
        }
        for (int i = 0; i < Z; i++) {
            C[i] = sc.nextLong();
        }

        long[] AB = new long[X*Y];
        int k = 0;
        for (int i = 0; i < X; i++) {
            for (int j = 0; j < Y; j++) {
                AB[k] = A[i] + B[j];
                k++;
            }
        }
        Arrays.sort(AB);
        long[] sortedAB = reverseArray(AB);

        int l = 0;
        long[] ABC = new long[(Math.min(K, sortedAB.length)) * C.length];
        for (int i = 0; i < Math.min(K, sortedAB.length); i++) {
            for (int j = 0; j < Z; j++) {
                ABC[l] = sortedAB[i] + C[j];
                l++;
            }
        }

        Arrays.sort(ABC);
        long[] sortedABC = reverseArray(ABC);
        for (int i = 0; i < K; i++) {
            System.out.println(sortedABC[i]);
        }

    }
    private long [] reverseArray (long[] array) {
        long[] ret = new long[array.length];
        int index = array.length -1;
        for (long elem : array) {
            ret[index] = elem;
            index--;
        }
        return ret;
    }

    // Main
    public static void main(String[] args) {
        //long start = System.currentTimeMillis();
        new Main().solve();
        //long end = System.currentTimeMillis();
        //System.out.println((end - start)  + "ms");
    }

}
