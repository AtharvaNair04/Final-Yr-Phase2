import java.util.*;

public class Main {

    public static double dp[][][] = new double[301][301][301];

    public static double solve(int x,int y,int z,int  n){
        if(x < 0 || y < 0 || z < 0) return 0;
        if(x == 0 && y == 0 && z == 0) return  0;
        if(dp[x][y][z] > -0.9){
            return dp[x][y][z];
        }
        double exp = n + x * solve(x-1,y,z,n) + y * solve(x+1,y-1,z,n) + z * solve(x,y+1,z-1,n);
        return dp[x][y][z] = exp/(x+y+z);
    }

    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int one = 0;
        int two = 0;
        int three = 0;

        for(int i=1;i<=n;i++){
            int temp = sc.nextInt();
            if(temp == 1){
                one++;
            }
            else if(temp == 2){
                two++;
            }
            else{
                three++;
            }
        }

        for(int i=0;i<=300;i++){
            for(int j=0;j<=300;j++){
                for(int k=0;k<=300;k++){
                    dp[i][j][k] = -1;
                }
            }
        }

        System.out.println(solve(one,two,three,n));
    }
}
