import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int M = sc.nextInt();
        ArrayList<ArrayList<Integer>> passages = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < N - 1; i++) {
            passages.add(new ArrayList<Integer>());
        }
        for (int i = 0; i < M; i++) {
            int s = sc.nextInt();
            int t = sc.nextInt();
            passages.get(s-1).add(t-1);
        }

        // no blocking
        double[] E = new double[N];
        for (int i = N - 2; i >= 0; i--) {
            double sum = 0;
            for (int dest: passages.get(i)) {
                sum += E[dest];
            }
            E[i] = sum / passages.get(i).size() + 1;
        }

        double minE = E[0];

        // with blocking
        for (int i = 0; i < N - 1; i++) {
            int len = passages.get(i).size();
            if (len == 1) {
                continue;
            }
            double maxE = 0;
            for (int dest: passages.get(i)) {
                maxE = Math.max(maxE, E[dest]);
            }
            E[i] = ((E[i] - 1) * len - maxE) / (len - 1) + 1;
            if (i > 0) {
                for (int j = i - 1; j >= 0; j--) {
                    double sum = 0;
                    for (int dest: passages.get(j)) {
                        sum += E[dest];
                    }
                    E[j] = sum / passages.get(j).size() + 1;
                }
            }
            minE = Math.min(minE, E[0]);
        }

        System.out.println(minE);
    }
}
