import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int e8 = in.nextInt();
        int sq = in.nextInt();
        in.close();
        String ans = (e8 < 9 && sq < 9) ? "Yay!" : ":(";
        System.out.println(ans);
    }
}
