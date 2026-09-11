import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = Integer.parseInt(sc.next());

        List<Integer> x = new ArrayList<>();
        List<Integer> y = new ArrayList<>();

        for (int i = 0; i < n; i++) x.add(Integer.parseInt(sc.next()));
        for (int i = 0; i < n; i++) y.add(Integer.parseInt(sc.next()));

        List<Double> M = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            double tmp = 0.0;
            for (int j = 0; j < n; j++) {
                tmp += Math.pow(Math.abs((double)x.get(j) - (double)y.get(j)), (double)i);
            }
            M.add(Math.pow(tmp, 1.0 / i));
        }

        double max = Double.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            double tmp = Math.abs(x.get(i) - y.get(i));
            if (tmp > max) max = tmp;
        }
        if (max == Double.MIN_VALUE) max = 0.0;
        M.add(max);

        M.forEach(System.out::println);
    }
}
