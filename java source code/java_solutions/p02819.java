import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //コード
        Scanner scanner = new Scanner(System.in);
        Long x = scanner.nextLong();

        for(Long i = x; i < x*x; i++) {
            if(x == 2L) {
                System.out.println(i);
                System.exit(0);
            }
            if ( i % 2 == 0) continue;
            for(Long j = 2L; j < x; j++) {
                if(i % j == 0L) {
                    break;
                }
                if(x-1L == j) {
                    System.out.println(i);
                    System.exit(0);
                }
            }
        }

    }
}
