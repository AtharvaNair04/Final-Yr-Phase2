import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String[] a = scanner.nextLine().split(" ");
        int N = Integer.parseInt(a[0]);
        int K = Integer.parseInt(a[1]);

        if (K % 2 == 0) {
            long p = N / K;
            p = p * p * p;
            long p2 = (N + (K / 2)) / K;
            p2 = p2 * p2 * p2;
            System.out.println(p + p2);
        } else {
            long p = N / K;
            p = p * p * p;
            System.out.println(p);
        }
    }
}
