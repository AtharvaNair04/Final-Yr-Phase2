import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.List;
    import java.util.Scanner;
     
    public class Main {
        public static void main(String[] args) {
            TestCase testCase = readFromInput();
     
            int number = findNumber(testCase);
     
            output(number);
        }
     
        private static TestCase readFromInput() {
            Scanner sc = new Scanner(System.in);
            final int N = sc.nextInt();
            final int M = sc.nextInt();
     
            List<List<Integer>> A = new ArrayList<>();
            for (int i = 0; i < N; ++i) {
                int K = sc.nextInt();
                List<Integer> AN = new ArrayList<>();
                for (int j = 0; j < K; ++j) {
                    AN.add(sc.nextInt());
                }
                A.add(AN);
            }
     
            sc.close();
     
            return new TestCase(N, M, A);
        }
     
        public static int findNumber(TestCase testCase) {
            int[] counts = new int[testCase.M];
            Arrays.fill(counts, testCase.N);
     
            testCase.A.forEach(AN -> AN.forEach(A -> counts[A - 1]--));
     
            return countNumberAllLiked(counts);
        }
     
        private static int countNumberAllLiked(int[] counts) {
            int number = 0;
            for (int i : counts) {
                if (i == 0) {
                    number++;
                }
            }
            return number;
        }
     
        private static void output(int i) {
            System.out.println(i);
        }
     
        public static class TestCase {
            final int N, M;
            final List<List<Integer>> A;
     
            public TestCase(int N, int M, List<List<Integer>> A) {
                this.N = N;
                this.M = M;
                this.A = A;
            }
        }
     
    }
