import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //コード
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();

        String a = str.substring(2, 3);
        String b = str.substring(4, 5);
        String c = str.substring(3, 4);
        String d = str.substring(5, 6);

       if (a.equals(c) && (b.equals(d))) {
           System.out.println("Yes");
       } else{
           System.out.println("No");
       }
    }
}
