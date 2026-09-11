import java.util.*;
import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        double n = sc.nextInt();
        double m = sc.nextInt();
        double d = sc.nextInt();
        double ans = 0;
        if (d == 0) {
            ans = ((m - 1) / n);
        } else {
            ans = (2 * (n - d) * (m - 1)) / (n * n);
        }
        System.out.println(BigDecimal.valueOf(ans).toPlainString());
    }    
}
