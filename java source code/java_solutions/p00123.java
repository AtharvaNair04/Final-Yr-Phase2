import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String[] r = "AAA:AA:A:B:C:D:E".split(":");
        double[] m1 = { 35.5, 37.5, 40, 43,50, 55, 70 };
        double[] m2 = { 71, 77, 83, 89, 105, 116, 148 };
        out:while (sc.hasNext()) {
            double a = sc.nextDouble();
            double b = sc.nextDouble();
            for (int i = 0; i < m1.length; i++) {
                if (a < m1[i] && b < m2[i]) {
                    System.out.println(r[i]);
                    continue out;
                }
            }
            System.out.println("NA");
        }
    }
}
