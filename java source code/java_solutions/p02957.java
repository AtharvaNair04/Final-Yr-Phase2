import java.util.Scanner;
import java.util.Arrays;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) {
        //コード
        Scanner scanner = new Scanner(System.in);
        long a = scanner.nextLong();
        long b = scanner.nextLong();
        if ((a+b) %2 == 0) {
            System.out.println((a+b) /2);
        } else {
            System.out.println("IMPOSSIBLE");
        }

    }
}
