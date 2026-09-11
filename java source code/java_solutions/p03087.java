import java.util.*;

public class Main {
    public static void main(String[] args) {
        //コード
        Scanner scanner = new Scanner(System.in);
        int N = Integer.parseInt(scanner.next());
        int Q = Integer.parseInt(scanner.next());
        String S = scanner.next();

        int[] ac = new int[N];
        Arrays.fill(ac, 0);

        for (int i = 0; i < S.length()-1; i++) {
            if(S.charAt(i) == 'A' && S.charAt(i+1) == 'C') {
                ac[i+1] = ac[i] + 1;
            } else {
                ac[i+1] = ac[i];
            }
        }
        for (int i = 0; i < Q; i++) {
            Integer l = Integer.parseInt(scanner.next()), r = Integer.parseInt(scanner.next());
            int count = 0;
            if(S.length() < l) continue;
            if(S.length() < r)  r = S.length();
            count = ac[r-1] - ac[l-1];
            System.out.println(count);
        }

    }
}
