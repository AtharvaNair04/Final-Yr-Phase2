import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        long maxIndex = 0;
        long maxValue = 0;
        int n = Integer.valueOf(sc.nextLine().replaceAll(" ", ""));
        for (int i = 0; i < n; i++) {
            String str = sc.nextLine();
            String[] split = str.split(" ");
            long a = Long.valueOf(split[0]);
            long b = Long.valueOf(split[1]);
            if (maxIndex < a) {
                maxIndex = a;
                maxValue = b;
            }
        }
        System.out.println(maxIndex + maxValue);
    }
}
