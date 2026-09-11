import java.util.Arrays;
import java.util.Scanner;
 
 
public class Main {
    static boolean[] isDist;
    static int N;
    static int M;
    static int p;
    public static int INF = 1 << 24;
    public static void main(String[] args) {
        Scanner stdIn = new Scanner(System.in);
        N = stdIn.nextInt();
        M = stdIn.nextInt();
        p = stdIn.nextInt();
        Limit = N*50;
        isDist = new boolean[N];
        for(int i = 0; i < M ;i++) {
            int d = stdIn.nextInt();
            isDist[d] = true;
        }
        boolean[] isDist2 = Arrays.copyOf(isDist,N);
         
        solv(1,p,0,0,false,isDist);
        solv(-1,p,0,0,false,isDist2);
        System.out.println(min);
         
    }
    static int min = 1 << 24; 
    static int Limit;
    public static void solv(int vec, int now, int coun, int cos, boolean alC,boolean[] isDist) {
         
        int nowp = now;
        int cost = cos;
        int count = coun;
        while(true) {
            if(min <= cost)return;
            if(nowp == N) nowp = 0;
            else if(nowp == -1) nowp = N-1;
            if(count == M ) {
                if(min > cost)min = cost;
                return;
            }
            if(isDist[nowp]) {
                 
                isDist[nowp] = false;
                count++;
                if(!alC && cost < Limit) {
                    boolean[] isDist2 = Arrays.copyOf(isDist, N);
                     
                    solv(vec*-1,nowp+vec*-1,count,cost+100,true,isDist2);
                }
            }
            if(count == M ) {
                if(min > cost)min = cost;
                return;
            }
            while(true) {
                if(nowp == N) nowp = 0;
                else if(nowp == -1) nowp = N-1;
                if(!isDist[nowp]) {
                    nowp += vec;
                    cost += 100;
                }
                else {
                    break;
                }
            }
        }
    }
}
