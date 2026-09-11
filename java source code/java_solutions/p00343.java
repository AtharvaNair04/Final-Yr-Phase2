import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        while (n-- > 0) {
            boolean[] used = new boolean[14];
            boolean ans = false;
            for (int i = 0; i < 6; i++) {
                used[sc.nextInt()] = true;
            }
            if (used[1] == used[13])
                ans=!used[1];
            else {
                int l = 6, r = 8;
                boolean t=false;
                for (;;) {
                    if (t == used[1]) {
                        if (used[r]^t)
                            r++;
                        else if (used[l]^t)
                            l--;
                    } else  {
                        if (used[l]^t)
                            l--;
                        else if (used[r]^t)
                            r++;
                    }
                    if (r == 13 || l==1) {
                      //  ans= !used[1];
                        ans=used[1]^r==13;
                        break;
                    }
//                    if (l == 1) {
//                        ans=  used[1];
//                        break;
//                    }
                    t = !t;
                }
            }
            System.out.println(ans ? "yes" : "no");
        }
    }
}
