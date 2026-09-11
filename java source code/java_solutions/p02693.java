import java.util.Scanner;
import java.util.Arrays;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) {
        //コード
        Scanner scanner = new Scanner(System.in);
        Integer k = Integer.parseInt(scanner.next());
        Integer a = Integer.parseInt(scanner.next());
        Integer b = Integer.parseInt(scanner.next());


        int total = 0;
        for(int i = 1; i <= b; i++) {
            total = k * i;
            if(total >= a && total <= b) {
                System.out.println("OK");
                break;
            }
            if(total >= b) {
                System.out.println("NG");
                break;
            }
        }
    }
}
