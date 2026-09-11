import java.util.Scanner;

class Main {
    private static boolean isModuicSquare(int[][] table, int moduic) {
        int modulo = (table[0][0] + table[0][1] + table[0][2]) % moduic;
        for (int i = 1; i < 3; i++) {
            if ((table[i][0] + table[i][1] + table[i][2]) % moduic != modulo) {
                return false;
            }
        }
        for (int j = 0; j < 3; j++) {
            if ((table[0][j] + table[1][j] + table[2][j]) % moduic != modulo) {
                return false;
            }
        }
        if ((table[0][0] + table[1][1] + table[2][2]) % moduic != modulo) {
            return false;
        }
        if ((table[2][0] + table[1][1] + table[0][2]) % moduic != modulo) {
            return false;
        }
        return true;
    }

    private static int rec(int n, int[][] table, int moduic, boolean[] used) {
        int x = n / 3;
        int y = n % 3;
        int result = 0;
        if (n > 8) {
            for (int i = 1; i < 11; i++) {
                if (!used[i]) {
                    moduic = i;
                }
            }
            if (isModuicSquare(table, moduic)) {
                result = 1;
            } else {
                result = 0;
            }
        } else if (table[x][y] == 0) {
            for (int i = 1; i < 11; i++) {
                if (!used[i]) {
                    table[x][y] = i;
                    used[i] = true;
                    result += rec(n + 1, table, moduic, used);
                    table[x][y] = 0;
                    used[i] = false;
                }
            }
        } else {
            result = rec(n + 1, table, moduic, used);
        }
        return result;
    }
         
    private static void solve() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            int[][] table = new int[3][3];
            boolean[] used = new boolean[11];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    table[i][j] = scanner.nextInt();
                }
            }
            int moduic = scanner.nextInt();
            if (moduic == -1) {
                break;
            }
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    used[table[i][j]] = true;
                }
            }
            used[moduic] = true;
            System.out.println(rec(0, table, moduic, used));
        }
    }

    public static void main(String... args) {
        solve();
    }
}
