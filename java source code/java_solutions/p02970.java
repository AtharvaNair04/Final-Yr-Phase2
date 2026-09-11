import java.util.*;
import java.lang.*;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) {
        //コード
        Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.next());
        int d = Integer.parseInt(scanner.next());

        for(int i = 1; i < n; i++) {
            int bottom = i - d;
            int top = i + d;
            if(bottom == 1) {
                if(n % top == 0) {
                    System.out.println(n/top);
                    System.exit(0);
                } else {
                    System.out.println((n/top) + 1);
                    System.exit(0);
                }
            }
        }
        System.out.println(1);
    }
}
