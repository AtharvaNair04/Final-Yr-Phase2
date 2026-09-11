import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] sp = br.readLine().split(" ");
        int n = Integer.parseInt(sp[0]), m = Integer.parseInt(sp[1]);
        int[] dx = {-1, 0, 1, 0};
        int[] dy = {0, -1, 0, 1};
        
        class L {
            int a, b, c;
            L() {
                this.a = 0;
                this.b = 0;
                this.c = 0;
            }
            L(int a, int b, int c) {
                this.a = a;
                this.b = b;
                this.c = c;
            }
        }
        
        Map<Integer, Integer> mpX = new TreeMap<>();
        Map<Integer, Integer> mpY = new TreeMap<>();
        
        mpX.put(0, 0);
        mpY.put(0, 0);
        List<L> lh = new ArrayList<>();
        List<L> lv = new ArrayList<>();
        
        for(int i = 0; i < n; i++) {
            sp = br.readLine().split(" ");
            int a = Integer.parseInt(sp[0]), b = Integer.parseInt(sp[1]), c = Integer.parseInt(sp[2]);
            mpX.put(a, 0);
            mpX.put(b, 0);
            mpY.put(c, 0);
            lh.add(new L(a, b, c));
        }
        
        for(int i = 0; i < m; i++) {
            sp = br.readLine().split(" ");
            int c = Integer.parseInt(sp[0]), a = Integer.parseInt(sp[1]), b = Integer.parseInt(sp[2]);
            mpY.put(a, 0);
            mpY.put(b, 0);
            mpX.put(c, 0);
            lv.add(new L(a, b, c));
        }
        
        List<Integer> xs = new ArrayList<>();
        List<Integer> ys = new ArrayList<>();
        for(Integer p: mpX.keySet()) {
            mpX.put(p, xs.size());
            xs.add(p);
        }
        for(Integer p: mpY.keySet()) {
            mpY.put(p, ys.size());
            ys.add(p);
        }
        
        int h = xs.size() * 2;
        int w = ys.size() * 2;
        
        int[][] d = new int[h][w];
        for(int i = 0; i < n; i++) {
            int a = mpX.get(lh.get(i).a) * 2;
            int b = mpX.get(lh.get(i).b) * 2;
            int c = mpY.get(lh.get(i).c) * 2;
            for(int x = a; x < b; x++) {
                d[x][c] = -1;
            }
        }
        
        for(int i = 0; i < m; i++) {
            int a = mpY.get(lv.get(i).a) * 2;
            int b = mpY.get(lv.get(i).b) * 2;
            int c = mpX.get(lv.get(i).c) * 2;
            for(int y = a; y < b; y++) {
                d[c][y] = -1;
            }
        }
        
        Queue<int[]> q = new LinkedList<>();
        int[] z = new int[2];
        int sx = mpX.get(0) * 2, sy = mpY.get(0) * 2;
        z[0] = sx; z[1] = sy;
        q.add(z);
        d[sx][sy] = 1;
        while(!q.isEmpty()) {
            int[] p = q.poll();
            int x = p[0], y = p[1];
            //System.out.println(x + " " + y);
            for(int i = 0; i < 4; i++) {
                int nx = x + dx[i], ny = y + dy[i];
                if(nx < 0 || nx >= h || ny < 0 || ny >= w) continue;
                if(d[nx][ny] != 0) continue;
                d[nx][ny] = 1;
                int[] r = new int[2];
                r[0] = nx; r[1] = ny;
                q.add(r);
            }
        }
        
        long ans = 0;
        boolean bk = false;
        for(int x = 0; x < h; x++) {
            if(bk) break;
            for(int y = 0; y < w; y++) {
                if(d[x][y] != 1) continue;
                if(x == 0 || x == h-1 || y == 0 || y == w-1) {
                    System.out.println("INF");
                    bk = true;
                    break;
                }
                if(x%2 == 0 || y%2 == 0) continue;
                long ex = xs.get(x/2+1) - xs.get(x/2);
                long ey = ys.get(y/2+1) - ys.get(y/2);
                ans += ex * ey;
            }
        }
        if(!bk) System.out.println(ans);
        
        /*for(int i = 0; i < h; i++) {
            for(int j = 0; j < w; j++) {
                if(i == sx && j == sy) System.out.print("s");
                if(d[i][j] == -1) {
                    System.out.print("|");
                } else if(d[i][j] == 1) {
                    System.out.print("●");
                } else {
                    System.out.print("○");
                }
            }
            System.out.println();
        }
        */
    }
}
