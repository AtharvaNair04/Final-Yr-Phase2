import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int a[] = new int[n];
        boolean flag = true;
        for (int i = 0; i < n; i++) {
            a[i] = sc.nextInt();
            if (a[i] % 2 == 1) {
                flag = false;
            }
        }
        if (!flag) {
            System.out.println("first");
        } else {
            System.out.println("second");
        }

    }
}
