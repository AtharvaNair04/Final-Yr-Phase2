import java.util.Scanner;

class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        int K = scanner.nextInt();
        String s = scanner.next();
        char[] c = s.toCharArray();
        if (c[K - 1] == 'A') {
            c[K - 1] = 'a';
        } else if (c[K - 1] == 'B') {
            c[K - 1] = 'b';
        } else {
            c[K - 1] = 'c';
        }
        System.out.println(String.valueOf(c));
    }

}
