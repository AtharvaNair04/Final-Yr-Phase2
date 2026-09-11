import java.util.*;
public class Main {
    Scanner sc;
    int d[] = new int [3];
    int e[] = new int [3];
    String[] bet = {"lunch","dinner","midnight"};
    void run() {
        sc = new Scanner(System.in);
        while (true) {
            int n = sc.nextInt();
            if (n == 0) break;
            int[] t = new int[n];
            int[] s = new int[n];
            for (int j=0;j<3;j++) d[j] = e[j] = 0;
            for (int i=0;i<n;i++) {
                String[] str = sc.next().split(":");
                t[i] = Integer.parseInt(str[0]) * 100 + Integer.parseInt(str[1]);
                s[i] = sc.nextInt();
                int serve = s[i]-t[i]%100;
                if (serve < 0) serve += 60;
                if (1100 <= t[i] && 1459 >= t[i] ) {
                    d[0]++;
                    if (serve <= 8) e[0]++;
                }
                if (1800 <= t[i] && 2059 >= t[i] ) {
                    d[1]++;
                    if (serve <= 8) e[1]++;
                }
                if ((2100 <= t[i] && 2359 >= t[i]) || ( 0 <= t[i] && 159 >= t[i]) ) {
                    d[2]++;
                    if (serve <= 8) e[2]++;
                }
            }
            for (int i=0;i<3;i++) {
            	if (d[i] == 0) System.out.println(bet[i] + " " + "no guest");
        	    else System.out.println(bet[i] + " " + (int) ( (double)e[i]/(double)d[i]*100 ));
        	}
        }
        
    }

    public static void main(String[] args) {
        new Main().run();
    }

}
