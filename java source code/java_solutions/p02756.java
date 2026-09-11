import java.util.*;
class Main{
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        String s = scanner.next();
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        int q = scanner.nextInt();
        boolean rev = false;
        for (int i = 0; i < q; i++) {
            int ti = scanner.nextInt();
            if (ti == 1) {
                if (rev) rev = false;
                else rev = true;
            } else {
                int f = scanner.nextInt();
                String c = scanner.next();
                if ((f == 1 && !rev) || (f == 2 && rev)) {
                    sb1.append(c);
                } else {
                    sb2.append(c);
                }
            }
        }
 
        if (!rev) {
            sb1.reverse();
            System.out.println(sb1 + s + sb2);
        } else {
            sb2.reverse();
            StringBuilder S = new StringBuilder(s);
            S.reverse();
            System.out.println(sb2 + S.toString() + sb1);
        }
    }
}
