import java.util.Scanner;
import java.util.stream.LongStream;

public class Main {
    public static Long gcd(Long a, Long b) {
        if (a < b) return gcd(b, a);
        if (b == 0) return a;
        return gcd(b, a % b);
    }

    public static Long lcm(Long a, Long b) {
        if (a < b) return lcm(b, a);
        return a * b / gcd(a, b);
    }

    public static Long cal(Long a, Long b) {
        Long min_f;
        if(a%b == 0) {
            min_f = a / b;
        } else {
            min_f= (a / b) + 1;
        }
        return min_f;

    }
    public static void main(String[] args) {
        //コード
        Scanner scanner = new Scanner(System.in);
        Long a = scanner.nextLong();
        Long b = scanner.nextLong();
        Long c = scanner.nextLong();
        Long d = scanner.nextLong();

        Long a_b_min = cal(a,c);
        Long a_b_max = b / c;
        Long total1 = (a_b_max - a_b_min) + 1;

        Long a_d_min = cal(a,d);
        Long b_d_max = b / d;
        Long total2 = (b_d_max - a_d_min) + 1;

        Long lcm = lcm(c,d);
        Long lcm_min = cal(a,lcm);
        Long lcm_max = b / lcm;
        Long total3 = (lcm_max - lcm_min) + 1;

        System.out.println((b-a+1) - (total1+total2-total3));


    }
}
