import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        int n = sc.nextInt();
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<s.length(); i++) {
            if (i % n == 0) {
                sb.append(s.charAt(i));
            }
        }
        System.out.println(sb.toString());
    }
}
