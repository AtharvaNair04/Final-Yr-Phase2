import java.util.*;

public class Main {


    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int D = sc.nextInt();
        int[] c = new int[27];
        int[] t = new int[D+1];
        int[] last_day = new int[27];
        Arrays.fill(last_day, 0);
        int[][] s = new int[D + 1][27];
        for (int i = 1; i <= 26; i++) {
            c[i] = sc.nextInt();
        }
        for (int i = 1; i <= D; i++) {
            for (int j = 1; j <= 26; j++) {
                s[i][j] = sc.nextInt();
            }
        }
        for (int i = 1; i <= D; i++) {
            t[i] = sc.nextInt();
        }
//        int m = sc.nextInt();
//        for(int i = 0;i<m;i++){
//            t[sc.nextInt()] = sc.nextInt();
//        }


        long ans[] = new long[D+1];
        ans[0] = 0;
        for (int i = 1; i <= D; i++) {
            last_day[t[i]] = i;
            ans[i] = ans[i - 1] + calcS(c, s, t[i], i,last_day);
            System.out.println(ans[i]);
        }
        System.exit(0);


    }

    private static long calcS(int[] c, int[][] s, int kaisai, int day,int[] last_day) {

        long loss = 0;
        for (int i = 1; i <= 26; i++) {
            if (i == kaisai) {
                last_day[i] = day;
                continue;
            }
            loss -= c[i] * (day - last_day[i]);
        }

        long plus = s[day][kaisai];
        return plus + loss;
    }

}
