import java.util.Scanner;

public class Main {

    static int makePond(int[][] g, int a, int b, int c, int d){
        int max = 0;
        for(int i=a;i<=c;i++){
            for(int j=b;j<=d;j++){
                max = Math.max(g[i][j], max);
            }
        }
        int min = 9;
        for(int j=b-1;j<=d+1;j++){
            if(g[a-1][j] <= max || g[c+1][j] <= max) {
                return 0;
            }
            min = Math.min(min, Math.min(g[a-1][j], g[c+1][j]));
        }
        for(int i=a-1;i<=c+1;i++){
            if(g[i][b-1] <= max || g[i][d+1] <= max) {
                return 0;
            }
            min = Math.min(min, Math.min(g[i][b-1], g[i][d+1]));
        }
        int sum = 0;
        for(int i=a;i<=c;i++){
            for(int j=b;j<=d;j++){
                sum += Math.abs(min - g[i][j]);
            }
        }
        return sum;
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        while(true){
            int d = sc.nextInt();
            int w = sc.nextInt();
            if(d + w == 0) break;
            int[][] garden = new int[d][w];
            for(int i=0;i<d;i++){
                for(int j=0;j<w;j++){
                    garden[i][j] = sc.nextInt();
                }
            }
            int max = 0;
            for(int i=1;i<d-1;i++){
                for(int j=1;j<w-1;j++){
                    for(int k=i;k<d-1;k++){
                        for(int l=j;l<w-1;l++){
                            max = Math.max(makePond(garden, i, j, k, l), max);
                        }
                    }
                }
            }
            System.out.println(max);
        }
    }
}
