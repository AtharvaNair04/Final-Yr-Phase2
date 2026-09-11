import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String YES = "Yes";
    private static final String NO = "No";

    public static void main(String[] args) {
        TestCase testCase = readFromInput();

        final String result = process(testCase);

        output(result);
    }

    private static TestCase readFromInput() {
        Scanner sc = new Scanner(System.in);

        int N = sc.nextInt();
        List<Integer> L = new ArrayList<>();
        for (int i = 0; i < N; ++i) {
            L.add(sc.nextInt());
        }

        sc.close();
        return new TestCase(N, L);
    }

    public static String process(TestCase testCase) {
        List<Integer> L = testCase.L;

        int largest = 0;
        int sum = 0;

        for (int side : L) {
            largest = Math.max(largest, side);
            sum += side;
        }

        return (largest < (sum - largest)) ? YES : NO;
    }

    private static void output(String result) {
        System.out.println(result);
    }

    public static class TestCase {
        final int N;
        final List<Integer> L;

        public TestCase(int N, List<Integer> L) {
            this.N = N;
            this.L = L;
        }
    }
}
