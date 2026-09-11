import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int h = scan.nextInt(), w = scan.nextInt();

        boolean[][] bombs = new boolean[h + 2][w + 2];
        for (int i = 0; i < h; i++) {
            String s = scan.next();
            char[] arr = s.toCharArray();
            for (int j = 0; j < arr.length; j++) {
                if (arr[j] == '#') {
                    bombs[i + 1][j + 1] = true;
                }
            }
        }
        char[][] res = new char[h+2][w+2];
        for (int i = 1; i <= h; i++ ) {
            for (int j = 1; j <= w; j++) {
                if (bombs[i][j]) {
                    res[i][j] = '#';
                } else {
                    int count = 0;
                    for (int k = -1; k <= 1; k++) {
                        for (int l = -1; l <= 1; l++) {
                            if (k == 0 && l == 0) {
                                continue;
                            }
                            if (bombs[i + k][j + l]) {
                                count++;
                            }
                        }
                    }
                    res[i][j] = (char)(count + '0');
                }
            }
        }
        for (int i = 1; i <= h; i++) {
            for (int j = 1; j <= w; j++) {
                System.out.print(res[i][j]);
            }
            System.out.println();
        }
    }

}
