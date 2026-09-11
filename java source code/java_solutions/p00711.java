import java.util.Scanner;

public class Main {
    static int w, h;
    static int[][] map;
    static int c;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            w = sc.nextInt();
            h = sc.nextInt();
            if ((w | h) == 0) {
                break;
            }
            map = new int[h][w];
            int sx = 0;
            int sy = 0;
            c = 0;
            for (int i = 0; i < h; i++) {
                String line = sc.next();
                for (int j = 0; j < w; j++) {
                    if (line.charAt(j) == '@') {
                        sy = i;
                        sx = j;
                        map[i][j] = 0;
                    } else {
                        map[i][j] = line.charAt(j) == '.' ? 0 : 1;
                    }
                }
            }
            dfs(sy, sx);
            System.out.println(c);
        }
    }

    public static void dfs(int y, int x) {
        if (y < 0 || y >= h || x < 0 || x >= w) {
            return;
        }
        if (map[y][x] == 1) {
            return;
        }
        map[y][x] = 1;
        int[] d = new int[] { 0, 1, 0, -1, 0 };
        for (int i = 0; i < 4; i++) {
            dfs(y + d[i], x + d[i + 1]);
        }
        c++;
    }

}
