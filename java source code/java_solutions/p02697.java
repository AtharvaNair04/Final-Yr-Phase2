import java.util.*;
class Main{
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        if (n % 2 == 1) {
            for (int i = 1; i <= m; i++) {
                System.out.println(i + " " + (n - i));
            }
            return;
            } else {
            for (int i = 1; i <= m; i++) {
                if (i <= n / 4) {
                System.out.println(i + " " + (n - i + 1));
                } else {
                System.out.println(i + " " + (n - i));
                }
            }
        }
    }
}
