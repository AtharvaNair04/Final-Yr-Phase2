import java.util.*;

public class Main {
    public static void main(String[] args) {
        //コード
        Scanner scanner = new Scanner(System.in);
        Integer A = Integer.parseInt(scanner.next());
        Integer B = Integer.parseInt(scanner.next());

        Integer a = 0;
        Integer start = 0;
        for(;;) {
            double tax1 = Math.floor(a * 0.08);
            if((int)tax1 <= A-1) start = a + 1;
            if(A < tax1) break;
            a++;
        }

        for (int i = start; i < a; i++) {
            double tax2 = Math.floor(i * 0.1);
            if((int)tax2 == B) {
                System.out.println(i);
                System.exit(0);
            }
        }
        System.out.println(-1);
    }
}
