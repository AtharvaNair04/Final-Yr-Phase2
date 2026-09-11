import java.util.Arrays;
import java.util.Scanner;

class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextLong();
        }
        long[] b = new long[n];
        for (int i = 0; i < n; i++) {
            b[i] = scanner.nextLong();
        }
        scanner.close();
        long[] sa = new long[n];
        for (int i = 0; i < n; i++) {
            sa[i] = a[i] - b[i];
        }
        Arrays.sort(sa);
        long countOfhu = 0;
        long amountOfhu = 0;
        for (int i = 0; i < n; i++) {
            if (sa[i] < 0) {
                countOfhu++;
                amountOfhu += sa[i];
            }
        }
        if (countOfhu == 0) {
            System.out.println(0);
            return;
        }

        long genjitendenosei_count = 0;
        long genjitendenosei_amount = 0;

        for (int i = 0; i < sa.length - countOfhu; i++) {
            genjitendenosei_amount += sa[sa.length - i - 1];
            genjitendenosei_count++;
            if (genjitendenosei_amount >= amountOfhu * -1) {
                System.out.println(genjitendenosei_count + countOfhu);
                return;
            }
        }
        System.out.println(-1);
    }

}
