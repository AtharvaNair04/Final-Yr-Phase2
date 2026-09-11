import java.util.Scanner;
 
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        int a = N % 10, b = N / 10;
        if (a == 9 || b == 9) {
            System.out.println("Yes");
        } else {
            System.out.println("No");
        }
    }
}
