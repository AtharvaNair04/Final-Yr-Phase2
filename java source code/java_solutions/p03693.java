import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        sc.nextInt();
        int g = sc.nextInt();
        int b = sc.nextInt();

        System.out.println(( 10 * g + b) % 4 == 0 ? "YES" : "NO");

        
    }
}
