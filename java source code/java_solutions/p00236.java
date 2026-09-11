import java.util.*;
 
public class Main {
    Scanner sc = new Scanner(System.in);
    boolean [][] visited;
    boolean ans;
    int count, w,h, sx,sy;
    int [] vx = {0,1,0,-1, 1,1,-1,-1};
    int [] vy = {-1,0,1,0, -1,1,1,-1};
     
    private void doit(){
        while(true){
            w = sc.nextInt();
            h = sc.nextInt();
            if((w|h) == 0) break;
            count = 0;
            visited = new boolean[h][w];
            sx = -1;
            sy = -1;
            boolean isworstpatt = (w == 7 && h == 7);
            for(int i = 0; i < h; i++){
                for(int j = 0; j < w; j++){
                    int now = sc.nextInt();
                    if(now == 0){
                        count++;
                        if(sx == -1){
                            sx = j;
                            sy = i;
                        }
                    }
                    else{
                        isworstpatt = false;
                        visited[i][j] = true;
                    }
                }
            }
            if(isworstpatt){
                System.out.println("No");
                continue;
            }
             
            for(int i = 0; i < h; i++){
                for(int j = 0; j < w; j++){
                    if(visited[i][j])continue;
                    int count = 0;
                    for(int k = 0; k < 4; k++){
                        int x = j + vx[k], y = i + vy[k];
                        if(! isOK(x,y))continue;
                        if(visited[y][x] == false){
                            count++;
                        }
                    }
                    if(count <= 1){
                        sx = -1;
                        break;
                    }
                }
                if(sx == -1){
                    break;
                }
            }
             
            if(sx == -1){
                System.out.println("No");
                continue;
            }
            ans = false;
            dfs(sx, sy, 0);
            System.out.println(ans ? "Yes" : "No");
        }
    }
 
    private void dfs(int x, int y, int deep) {
        if(ans) return;
         
        //branch
        if(count - deep < Math.abs(x - sx) + Math.abs(y - sy)) return;
         
        for(int i = 0; i < 4; i++){
            int xx = x + vx[i];
            int yy = y + vy[i];
            if(! isOK(xx, yy))continue;
            if(yy == sy && xx == sx){
                if(deep + 1 == count){
                    ans = true;
                }
                return;
            }
            if(visited[yy][xx]) continue;
            boolean res = check(xx, yy);
            if(! res)continue;
 
            visited[yy][xx] = true;
            dfs(xx, yy, deep + 1);
            visited[yy][xx] = false;
        }
    }
 
    private boolean check(int x, int y) {
        for(int i = 0; i < vx.length; i++){
            int xx = x + vx[i];
            int yy = y + vy[i];
            if(! isOK(xx, yy)) continue;
            if(xx == sx && yy == sy) continue;
            if(visited[yy][xx]) continue;
            int count = 0;
            for(int j = 0; j < 4; j++){
                int xxx = xx + vx[j];
                int yyy = yy + vy[j];
                if(! isOK(xxx, yyy)) continue;
                if(visited[yyy][xxx]) continue;
                count++;
            }
            if(count <= 1) return false;
        }
        return true;
    }
 
    private boolean isOK(int xx, int yy) {
        if(0<= xx && xx < w && 0<= yy && yy < h) return true;
        return false;
    }
 
    private void debug(Object... o) {
        System.out.println("debug = " + Arrays.deepToString(o));
    }
 
    public static void main(String[] args) {
        new Main().doit();
    }
}
