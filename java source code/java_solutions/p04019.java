import java.util.Scanner;
 
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String text = in.next();
        in.close();
        boolean u = text.contains("N");
        boolean d = text.contains("S");
        boolean r = text.contains("E");
        boolean l = text.contains("W");
        String dis = !(u ^ d) && !(r ^ l) ? "Yes" : "No";
        System.out.println(dis);
    }
}
