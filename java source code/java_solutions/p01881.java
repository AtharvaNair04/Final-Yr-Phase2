import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        Scanner in = new Scanner(inputStream);
        PrintWriter out = new PrintWriter(outputStream);
        HelpThePrincess solver = new HelpThePrincess();
        solver.solve(1, in, out);
        out.close();
    }

    static class HelpThePrincess {
        int H;
        int W;
        Point goal;
        HelpThePrincess.State[][] cell;

        boolean inside(int h, int w) {
            return 0 <= h && h < H && 0 <= w && w < W;
        }

        public void solve(int testNumber, Scanner in, PrintWriter out) {
            H = in.nextInt();
            W = in.nextInt();
            String[] map = new String[H];
            for (int i = 0; i < H; i++) {
                map[i] = in.next();
            }
            cell = new HelpThePrincess.State[H][W];
            for (int i = 0; i < H; i++) {
                for (int j = 0; j < W; j++) {
                    if (map[i].charAt(j) == '#') {
                        cell[i][j] = HelpThePrincess.State.wall;
                    } else if (map[i].charAt(j) == '.') {
                        cell[i][j] = HelpThePrincess.State.empty;
                    } else if (map[i].charAt(j) == '@') {
                        cell[i][j] = HelpThePrincess.State.prince;
                    } else if (map[i].charAt(j) == '$') {
                        cell[i][j] = HelpThePrincess.State.soldier;
                    } else {
                        cell[i][j] = HelpThePrincess.State.empty;
                    }
                    if (map[i].charAt(j) == '%') {
                        goal = new Point(i, j);
                    }
                }
            }
            boolean ok = false;
            int[] dx = new int[]{0, 0, -1, 1};
            int[] dy = new int[]{-1, 1, 0, 0};
            while (check()) {
                HelpThePrincess.State[][] _cell = new HelpThePrincess.State[H][W];
                for (int i = 0; i < H; i++) {
                    for (int j = 0; j < W; j++) {
                        if (cell[i][j].equals(HelpThePrincess.State.wall)) {
                            _cell[i][j] = HelpThePrincess.State.wall;
                        } else {
                            _cell[i][j] = HelpThePrincess.State.empty;
                        }
                    }
                }
                for (int i = 0; i < H; i++) {
                    for (int j = 0; j < W; j++) {
                        for (int k = 0; k < 4; k++) {
                            int nx = i + dx[k];
                            int ny = j + dy[k];
                            if (inside(nx, ny) && !_cell[nx][ny].equals(HelpThePrincess.State.wall) && (cell[i][j].equals(HelpThePrincess.State.prince) || cell[i][j].equals(HelpThePrincess.State.soldier))) {
                                if (_cell[nx][ny].equals(HelpThePrincess.State.empty)) {
                                    _cell[nx][ny] = cell[i][j];
                                } else if (_cell[nx][ny].equals(HelpThePrincess.State.prince) && cell[i][j].equals(HelpThePrincess.State.soldier)) {
                                    _cell[nx][ny] = cell[i][j];
                                }
                            }
                        }
                    }
                }
                if (cell[goal.x][goal.y].equals(HelpThePrincess.State.prince)) {
                    ok = true;
                    break;
                } else if (cell[goal.x][goal.y].equals(HelpThePrincess.State.soldier)) {
                    break;
                }
                cell = _cell;
            }
            out.println(ok ? "Yes" : "No");
        }

        public boolean check() {
            for (int i = 0; i < H; i++) {
                for (int j = 0; j < W; j++) {
                    if (cell[i][j].equals(HelpThePrincess.State.prince)) {
                        return true;
                    }
                }
            }
            return false;
        }

        public enum State {
            prince,
            soldier,
            wall,
            empty,;
        }

        public class Point {
            public int x;
            public int y;

            public Point(int _x, int _y) {
                this.x = _x;
                this.y = _y;
            }

        }

    }
}
