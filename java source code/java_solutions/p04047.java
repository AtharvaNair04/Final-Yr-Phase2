import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        int[] L = new int[2*N];
        int temp = 0, result = 0;
        for (int i = 0; i < 2*N; i++) {
            L[i] = scanner.nextInt();
        }
        for (int i = 0; i < L.length; i++) {
            for (int j = i; j < L.length; j++) {
                if (L[i] > L[j]) {
                    temp = L[i];
                    L[i] = L[j];
                    L[j] = temp;
                }
            }
        }
        for (int i = 0; i < L.length; i=i+2) {
            result += L[i];
        }
        System.out.println(result);
    }
}
