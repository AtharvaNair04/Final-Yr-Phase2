import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by mixnector on 2017/03/04.
 */
public class Main {
    static Scanner in;
    static PrintWriter out;
    static String INPUT = "";

    public static void main(String[] args) throws Exception {
        in = INPUT.isEmpty() ? new Scanner(System.in) : new Scanner(INPUT);
        out = new PrintWriter(System.out);
        int N = ni();
        long[] sizes = new long[N];
        int i;
        for (i = 0; i < N; i++) {
            sizes[i] = nl();
        }
        Arrays.sort(sizes);
        int result = N;
        int now = 0;
        long totalSize = 0;
        for (i = 0; i < N - 1; i++)
        {
            if (totalSize < 500000000)
                totalSize += sizes[i];
            //サイズ不足
            if (totalSize * 2 < sizes[i + 1])
            {
                result -= i - now + 1;
                now = i + 1;
            }
            //サイズ神化
            if (totalSize >= 500000000)
            {
                break;
            }
        }
        out.println(result);
        out.flush();
    }

    static int ni() {
        return Integer.parseInt(in.next());
    }

    static long nl() {
        return Long.parseLong(in.next());
    }
}
