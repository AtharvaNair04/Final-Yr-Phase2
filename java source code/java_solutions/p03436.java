import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int h = sc.nextInt();
        int w = sc.nextInt();
        int[][] map = new int[h + 2][w + 2];
        long wall = 0;
        for (int i = 0; i < h; i++) {
            String s = sc.next();
            for (int j = 0; j < w; j++) {
                char c = s.charAt(j);
                if (c == '#') {
                    map[i + 1][j + 1] = -1; // -1は通れない
                    wall++;
                } else {
                }
            }
        }
        Arrays.fill(map[0], -1);
        Arrays.fill(map[h + 1], -1);
        for (int i = 0; i < h + 2; i++) {
            map[i][0] = -1;
            map[i][w + 1] = -1;
        }
        sc.close();
        Queue<Pos> queue = new ArrayDeque<Pos>();
        boolean visited[][] = new boolean[h + 2][w + 2];
        queue.add(new Pos(1, 1));
        map[1][1] = 1;
        fin: while (!queue.isEmpty()) {
            Pos pos = queue.poll();
            int ci = pos.i;
            int cj = pos.j;
            int dx[] = { 0, 1, 0, -1 };
            int dy[] = { -1, 0, 1, 0 };
            for (int i = 0; i < 4; i++) {
                int ni = ci + dx[i];
                int nj = cj + dy[i];
                if (map[ni][nj] != -1 && !visited[ni][nj]) {
                    map[ni][nj] = map[ci][cj] + 1;
                    visited[ni][nj] = true;
                    queue.add(new Pos(ni, nj));
                    if (ni == h && nj == w) {
                        break fin;
                    }
                }
            }
        }

        if (map[h][w] == 0) {
            System.out.println(-1);
        } else {
            System.out.println(h * w - wall - map[h][w]);
        }
    }
}

class Pos {
    int i, j;

    Pos(int i, int j) {
        this.i = i;
        this.j = j;
    }
}
