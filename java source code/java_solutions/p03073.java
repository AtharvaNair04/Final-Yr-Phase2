import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //コード
        Scanner scanner = new Scanner(System.in);
        String s = scanner.next();
        String sp = "01";

        Long count_s1 = 0L;
        Long count_s2 = 0L;
        for (int i = 0; i < s.length(); i++) {
            if(i%2 ==0) {
                if (s.charAt(i) == sp.charAt(0)) {
                    count_s1++;
                } else {
                    count_s2++;
                }
            }else {
                if (s.charAt(i) == sp.charAt(1)) {
                    count_s1++;
                } else {
                    count_s2++;
                }

            }
        }

        Long min = Math.min(count_s1, count_s2);
        System.out.println(min);
    }
}
