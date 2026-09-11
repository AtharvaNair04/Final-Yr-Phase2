import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        char originalMap[][] = new char[n][n];
        for (int i = 0; i < n; i++) {
            String s = sc.next();
            for (int j = 0; j < n; j++) {
                originalMap[i][j] = s.charAt(j);
            }
        }
        int cnt = 0;
        // a,bはずらす値
        B: for (int b = 0; b < n; b++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int nj = (j + b) % n;
                    int ni = (i + b) % n;
                    if (originalMap[i][nj] == originalMap[j][ni]) {

                    } else {
                        continue B;
                    }
                }
            }
            cnt++;
        }
        System.out.println(cnt * n);
        sc.close();
    }
}
