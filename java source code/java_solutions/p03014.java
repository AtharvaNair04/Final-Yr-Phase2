import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int h = sc.nextInt();
        int w = sc.nextInt();
        int[][] map = new int[h][w];
        int data[][] = new int[h][w];
        int dataW[][] = new int[h][w];
        // 1は障害物
        for (int i = 0; i < h; i++) {
            String s = sc.next();
            for (int j = 0; j < w; j++) {
                map[i][j] = s.charAt(j) == '#' ? 1 : 0;
            }
        }
        for (int i = 0; i < h; i++) {
            if (map[i][0] == 0) {
                data[i][0] = 1;
            }
            for (int j = 1; j < w; j++) {
                if (map[i][j] == 0) {
                    data[i][j] = data[i][j - 1] + 1;
                }
            }
        }
        for (int i = 0; i < h; i++) {
            for (int j = w - 2; j >= 0; j--) {
                if (data[i][j] != 0) {
                    data[i][j] = Math.max(data[i][j], data[i][j + 1]);
                }
            }
        }
        for (int i = 0; i < w; i++) {
            if (map[0][i] == 0) {
                dataW[0][i] = 1;
            }
            for (int j = 1; j < h; j++) {
                if (map[j][i] == 0) {
                    dataW[j][i] = dataW[j - 1][i] + 1;
                }
            }
        }
        for (int i = 0; i < w; i++) {
            for (int j = h - 2; j >= 0; j--) {
                if (dataW[j][i] != 0) {
                    dataW[j][i] = Math.max(dataW[j][i], dataW[j + 1][i]);
                }
            }
        }

        int ans = 0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                ans = Math.max(ans, data[i][j] + dataW[i][j]);
            }
        }
        System.out.println(ans - 1);

        sc.close();
    }
}
