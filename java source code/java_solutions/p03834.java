import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        String a = sc.nextLine();
        String b = a.replaceAll(","," ");
        System.out.println(b);
    }
}
