import java.util.*;
import java.lang.*;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) {
        //コード
        Scanner scanner = new Scanner(System.in);
        Integer n = Integer.parseInt(scanner.next());
        Integer[] num_seq = new Integer[n];

        for(int i = 0; i < n; i++) {
            num_seq[i] = Integer.parseInt(scanner.next());
        }

        int count = 0;
        boolean[] total = new boolean[n-1];
        for(int i = 0; i < num_seq.length -1; i++) {
            if(num_seq[i] < num_seq[i+1]) {
                total[i] = true;
            } else {
                total[i] = false;
                count++;
            }
        }

        if(count > 2) {
            System.out.println("NO");
            System.exit(0);
        }

        int tmp = -1;
        int hoge = 0;
        boolean is_first = true;
        for(int i = 0; i < total.length; i++) {
            if (total[i] == false) {
                if(is_first) {
                    tmp = i;
                    is_first = false;
                } else {
                    hoge = num_seq[tmp];
                    num_seq[tmp] = num_seq[i+1];
                    num_seq[i+1] = hoge;
                }
            }
        }

        for(int i = 0; i < num_seq.length -1; i++) {
            if( !(num_seq[i] < num_seq[i+1])) {
                System.out.println("NO");
                System.exit(0);
            }
        }
        System.out.println("YES");
    }
}
