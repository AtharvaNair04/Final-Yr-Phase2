import java.util.Scanner;
public class Main {
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        while (scan.hasNextInt()) {
            int a = scan.nextInt();
            int b = scan.nextInt();
            int sum = a+b;
            int length = (int) (Math.log10(sum) + 1);
            System.out.println(length);
        }
    }
}
