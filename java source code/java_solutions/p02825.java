import java.util.*;

public class Main {
    Scanner sc = new Scanner(System.in);
    Random rand = new Random();
    int INF = Integer.MAX_VALUE / 2;
    long LINF = Long.MAX_VALUE / 2;

    boolean rantaku(int N, int M, int k) {
        int[][] mat = new int[N][M];
        int put = rand.nextInt(N * M / 2) + 1;
//        int put = rand.nextInt(N * 2) + 1;

        while (put-- != 0) {
            int muki = rand.nextInt(2); // 0 -> yoko, 1 -> tate
            if (muki == 0) {
                int row = rand.nextInt(N);
                int col = rand.nextInt(N - 1);

                if (mat[row][col] == mat[row][col + 1]) {
                    mat[row][col] = put + 1;
                    mat[row][col + 1] = put + 1;
                }
            } else {
                int row = rand.nextInt(N - 1);
                int col = rand.nextInt(N);

                if (mat[row][col] == mat[row + 1][col]) {
                    mat[row][col] = put + 1;
                    mat[row + 1][col] = put + 1;
                }
            }
        }

        int cnt = k;

        if (cnt == -1) {
            Set<Integer> st = new HashSet<>();
            for (int i = 0; i < M; ++i) {
                st.add(mat[0][i]);
            }
            cnt = st.size();
        }

        for (int ri = 0; ri < N; ++ri) {
            Set<Integer> st = new HashSet<>();
            for (int ci = 0; ci < M; ++ci) {
                if (mat[ri][ci] != 0) st.add(mat[ri][ci]);
            }
            if (st.size() != cnt) {
                return false;
            }
        }

        for (int ci = 0; ci < M; ++ci) {
            Set<Integer> st = new HashSet<>();
            for (int ri = 0; ri < N; ++ri) {
                if (mat[ri][ci] != 0) st.add(mat[ri][ci]);
            }
            if (st.size() != cnt) {
                return false;
            }
        }

        for (int ci = 0; ci < N; ++ci) {
//            for(int ri = 0; ri < M; ++ri){
//                System.out.println();
//            }
            System.out.println(Arrays.toString(mat[ci]));
        }

        return true;
    }


    void greedy(int N, int k) {
        while (!rantaku(N, N, k)) {

        }

    }

    void solve() {
        int N = sc.nextInt();
        if (N == 2) {
            System.out.println(-1);
            return;
        }

        if (N == 3) {
            System.out.println("aac");
            System.out.println("b.c");
            System.out.println("bdd");
            return;
        }

        if (N == 6) {
            System.out.println("aabb..\n" +
                    "b..zz.\n" +
                    "ba....\n" +
                    ".a..aa\n" +
                    "..a..b\n" +
                    "..a..b\n");
            return;
        }

        if (N == 7) {
            System.out.println(
                    "aab...x\n" +
                            "b.b...x\n" +
                            "baa...y\n" +
                            "...aaby\n" +
                            "...b.bz\n" +
                            "...baaz\n" +
                            "xxyyzz.\n"
            );
            return;
        }

        String[] base4 = {
                "abcc",
                "abdd",
                "ccab",
                "ddab",
        };

        String[] base5 = {
                "abbcc",
                "accd.",
                "bb.de",
                "a.b.e",
                "a.baa",
        };

        String[] base6 = {
                "aab.bb",
                "b.baa.",
                "baa..b",
                "a.bb.b",
                "ab...a",
                ".b.bba",
        };

        int x = -1, y = -1, z = -1;
        boolean loop_cont = true;
        for (x = 0; 4 * x <= N; ++x) {
            for (y = 0; 4 * x + 5 * y <= N; ++y) {
                if ((N - 4 * x - 5 * y) % 6 == 0) {
                    z = (N - 4 * x - 5 * y) / 6;
                    loop_cont = false;
                    break;
                }
            }

            if (!loop_cont) break;
//            for (y = 0; ; ++y) {
//                if (4 * x + 5 * y == N) {
//                    break;
//                } else if (4 * x + 5 * y > N) {
//                    break;
//                }
//            }
//            if (4 * x + 5 * y == N) {
//                break;
//            }
        }

        if (x == -1 || y == -1) {
            System.out.printf("-1");
            return;
        }

        char[][] ans = new char[N][N];
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                ans[i][j] = '.';
            }
        }
        int s = 0; // ans[s][s] ~ ans[s + x][s + x]みたいな感じで埋める
        while (x > 0 || y > 0 || z > 0) {
            if (x > 0) {
                for (int i = 0; i < 4; ++i) {
                    for (int j = 0; j < 4; ++j) {
                        ans[s + i][s + j] = base4[i].charAt(j);
                    }
                }
                s += 4;
                x--;
            }
            if (y > 0) {
                for (int i = 0; i < 5; ++i) {
                    for (int j = 0; j < 5; ++j) {
                        ans[s + i][s + j] = base5[i].charAt(j);
                    }
                }
                s += 5;
                y--;
            }
            if (z > 0) {
                for (int i = 0; i < 6; ++i) {
                    for (int j = 0; j < 6; ++j) {
                        ans[s + i][s + j] = base6[i].charAt(j);
                    }
                }
                s += 6;
                z--;
            }
        }

        for (int i = 0; i < N; ++i) {
//            String line = Arrays.toString(ans[i]);
            String line = "";
            for (int j = 0; j < N; ++j) {
                line += ans[i][j];
            }
            System.out.println(line);
        }
    }


    public static void main(String[] args) {
//        new Main().greedy(7, -1);
        new Main().solve();
    }
}
