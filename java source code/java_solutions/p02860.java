import java.util.*;
import java.lang.*;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) {
        //コード
        Scanner scanner = new Scanner(System.in);
        Integer x = Integer.parseInt(scanner.next());
        String[] s = scanner.next().split("");

        int half =  x / 2;
        if (x % 2 != 0) {
            System.out.println("No");
            System.exit(0);
        }
        boolean flag = true;
        for(int i = 0; i < half; i++) {
            if(!(s[i].equals(s[half+i]))) {
                flag = false;
                break;
            }
        }

        if (flag && half != 0) {
            System.out.println("Yes");
        } else {
            System.out.println("No");
        }
    }
}
