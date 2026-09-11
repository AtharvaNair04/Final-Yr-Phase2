import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        String[] in = s.nextLine().split(" ");
        int S = Integer.parseInt(in[0]);
        int W = Integer.parseInt(in[1]);

        if (W >= S) {
            System.out.println("unsafe");
        } else {
            System.out.println("safe");
        }
    }
}
