import java.math.BigDecimal;
import java.util.*;

class Main{
    public static void main(String argv[]) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int K = sc.nextInt();
        sc.close();
        double result = 0.0;
        for(int i=1;i<=N;i++){
            double tmp = 1.0/(double)N;
            int now = i;
            while(now < K){
                tmp *= 0.5;
                now *= 2;
            }
            result += tmp;
        }
        System.out.println(String.format("%.10f", result));
    }
}
