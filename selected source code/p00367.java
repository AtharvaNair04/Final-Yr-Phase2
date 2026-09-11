import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[][][] b = new int[n][3][2];
        ;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < 3; j++)
                for (int k = 0, h, m; k < 2; k++)
                    b[i][j][k] = sc.nextInt() * 60 + sc.nextInt();
        int ans = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                for (int k = 0; k < n; k++) {
                    int res = 0;
                    for (int p = 0; p < n; p++) {
                        if (b[p][0][0] <= b[i][0][1] && b[i][0][1] <= b[p][0][1] &&
                                b[p][1][0] <= b[j][1][1] && b[j][1][1] <= b[p][1][1] &&
                                b[p][2][0] <= b[k][2][1] && b[k][2][1] <= b[p][2][1])
                            res++;
                    }
                    ans = Math.max(ans, res);
                }
        System.out.println(ans);
    }
}
