import java.util.*;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int n = scan.nextInt();
        double sum = IntStream.range(0, n).mapToDouble(i -> {
            String x = scan.next();
            String u = scan.next();
            double total = 0;
            if ("BTC".equals(u)) {
                total += Double.parseDouble(x) * 380000;
            } else {
                total += Integer.parseInt(x);
            }
            return total;
        }).sum();
        System.out.println(sum);
    }
}
