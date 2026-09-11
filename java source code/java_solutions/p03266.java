import java.util.Scanner;

class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long n = scanner.nextLong();
        long k = scanner.nextLong();
        scanner.close();

        if (k % 2 == 0) {
            System.out.println((n / k) * (n / k) * (n / k)
                    + ((n % k < k / 2) ? n / k : n / k + 1) * ((n % k < k / 2) ? n / k : n / k + 1)
                            * ((n % k < k / 2) ? n / k : n / k + 1));

        } else {
            System.out.println((n / k) * (n / k) * (n / k));

        }

    }
}
