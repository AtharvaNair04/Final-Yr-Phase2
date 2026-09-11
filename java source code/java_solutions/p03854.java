import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder s = new StringBuilder(scanner.nextLine().trim()).reverse();

        while (s.length() > 0) {
            if (s.indexOf("maerd") == 0 || s.indexOf("esare") == 0) {
                s.delete(0, 5);
                continue;
            }
            if (s.indexOf("resare") == 0) {
                s.delete(0, 6);
                continue;
            }
            if (s.indexOf("remaerd") == 0) {
                s.delete(0, 7);
                continue;
            }
            System.out.println("NO");
            System.exit(0);
        }
        System.out.println("YES");
    }
}
