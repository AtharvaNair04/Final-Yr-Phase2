import java.util.*;

public class Main{

    int w,h;
    int[][] grid;
    int[] dx = {-1,0,1,0};
    int[] dy = {-1,-1,-1,-2};

    void solve(){
        Scanner sc = new Scanner(System.in);

        while(true){
            w = sc.nextInt();
            h = sc.nextInt();
            if(w==0 && h==0) break;

            grid = new int[h][w];
            for(int i=0; i<h; i++){
                for(int j=0; j<w; j++){
                    grid[i][j] = sc.nextInt();
                }
            }

            int[][] dp = new int[h][w];
            for(int i=0; i<w; i++){
                if(grid[0][i]==0) dp[0][i] = 1;
            }

            for(int i=0; i<h; i++){
                for(int j=0; j<w; j++){
                    if(grid[i][j]==0){
                        for(int k=0; k<4; k++){
                            int nx = j+dx[k], ny = i+dy[k];
                            if(nx<0 || nx>=w || ny<0 || ny>=h) continue;
                            if((k<=2 && grid[ny][nx]==0) || (k==3 && grid[ny][nx]==2)){
                                dp[i][j] += dp[ny][nx];
                            }
                        }
                    }else if(grid[i][j]==2){
                        if(i-1>=0 && grid[i-1][j]==0) dp[i][j] += dp[i-1][j];
                        if(i-2>=0 && grid[i-2][j]==2) dp[i][j] += dp[i-2][j];
                    }
                }
                //System.out.println(Arrays.toString(dp[i]));
            }

            int ans = 0;
            for(int i=0; i<w; i++){
                ans += dp[h-1][i];
                if(h>=3 && grid[h-2][i]==2) ans += dp[h-2][i];
            }

            System.out.println(ans);
        }
    }

    public static void main(String[] args){
        new Main().solve();
    }
}
