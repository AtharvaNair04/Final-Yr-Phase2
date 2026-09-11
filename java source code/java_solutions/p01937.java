import java.io.*;
import java.util.*;

/**
 * AIZU ONLINE JUDGE
 * 2837 Cryptex
 *    2018/02/17
 */
public class Main {

    int N;
    int M;
    int[] T;
    int nidx; // 負になる場所

    // 単純回数(2倍)を返す
    int min(int[] T) {
        int sum1 = 0;
        int sum2 = 0;
        int max1 = 0;
        int max2 = 0;

        for(int i = 0; i < N; i++) {
            if (T[i] > 0) {
                int t = T[i];
                sum1 += t;
                if (t > max1) {
                    max1 = t;
                }
            }
            else {
                int t = - T[i];
                sum2 += t;
                if (t > max2) {
                    max2 = t;
                }
            }
        }
        int f = 0;
        if ((sum1 % 2 != 0 || sum2 % 2 != 0)) {
            sum1 += 2; // +1手
            f = 1;
        }
        
        if (max1 * 2 - sum1 > 0) {
            log.printf("3bai1 %d\n", (max1 * 2 - sum1) * 2);
            sum1 += (max1 * 2 - sum1-f) * 2;
            f = 0;
        }
        if (max2 * 2 - sum2 > 0) {
            log.printf("3bai2 %d\n", (max2 * 2 - sum2) * 2);
            sum2 += (max2 * 2 - sum2-f) * 2;
            f = 0;
        }
        log.printf("sum1,sum2 = %d,%d\n",  sum1, sum2);
        return sum1 + sum2;
    }


    int sov() {
        int min = min(T);
        if (min % 2 == 0) {
            if (nidx > 0) {
                int old = T[nidx - 1];
                T[nidx - 1] -= M;
                int m = min(T);
                T[nidx - 1] = old;
                if (m < min) 
                    min = m;
            }
            if (nidx < N) {
                int old = T[nidx];
                T[nidx] += M;
                int m = min(T);
                T[nidx] = old;
                if (m < min) 
                    min = m;
            }
        }
        else {
            if (nidx == 0) {
                log.printf("3個反転1\n");
                // 3個反転
                T[0] += M;
                T[1] += M;
                T[2] += M;
                min = min(T);
                log.printf("再 %s min=%d\n", Arrays.toString(T), min);
            }
            else if (nidx == N) {
                log.printf("3個反転2\n");
                // 3個反転
                T[N - 1] -= M;
                T[N - 2] -= M;
                T[N - 3] -= M;
                min = min(T);
                log.printf("再 %s min=%d\n", Arrays.toString(T), min);
            }
            else {
                // 1個反転
                log.printf("1個反転\n");
                min = 99999999;
                if (true) {
                if (nidx > 0) {
                    int old = T[nidx - 1];
                    T[nidx - 1] -= M;
                    int m = min(T);
                    T[nidx - 1] = old;
                    if (m < min) 
                        min = m;
                }
                if (nidx < N) {
                    int old = T[nidx];
                    T[nidx] += M;
                    int m = min(T);
                    T[nidx] = old;
                    if (m < min) 
                        min = m;
                }
                }else {
                //T[nidx - 1] -= M;
                //T[nidx] += M;
                int minmin = 99999999;
                for(int i = 0; i < N; i++) {
                    int old = T[i];
                    if (T[i] >= 0)
                        T[i] -= M;
                    else 
                        T[i] += M;
                    min = min(T);
                    if (min < minmin) {
                        minmin = min;
                    }
                    //log.printf("all %s min=%d\n", Arrays.toString(T), min);
                    log.printf("all min=%d old=%d\n", min, old);
                    T[i] = old;
                }
                log.printf("minmin=%d\n", minmin/2);
                min = minmin;
                }
            }
        }

        return min / 2;

    }

    boolean main() throws IOException {

        Scanner sc = new Scanner(systemin);

        //int[] ir = readIntArray();
        N = sc.nextInt();//ir[0];
        M = sc.nextInt();//ir[1];
        log.printf("%d %d\n",  N, M);

        //ir = readIntArray();
        //int[] T = ir;
        T = new int[N];
        for(int i = 0; i < N; i++) {
            T[i] = sc.nextInt();
        }
        int ret = 0;
        if (N == 2 && T[0] != T[1]) {
            ret = -1;
        }
//        else if (N == 3 && T[0] == 1 && T[1] == 1 && T[2] == 1) {
//            ret = 3;
//        }
        else {
            Arrays.sort(T);
            nidx = -1; // 負になる場所
            for(int i = 0; i < N; i++) {
                if (T[i] > M / 2) {
                    if (nidx < 0)
                        nidx = i;
                    T[i] -= M;
                }
            }
            if (nidx < 0)
                nidx = N;
            log.printf("%s nidx = %d\n",  Arrays.toString(T), nidx);
            int min = min(T);
            log.printf(" min = %.1f\n",  min / 2.);
            if (M % 2 == 0 && min % 2 != 0) {
                ret = -1;
            }
            else {
                ret = sov();
            }
        }
        result.printf("%d\n", ret);
        sc.close();
        return false;
    }

    PrintStream log;
    PrintStream result = System.out;
    BufferedReader systemin;

    static Main instance = new Main();

    Main() {
        systemin = new BufferedReader(new InputStreamReader(System.in));
        log = new PrintStream(new OutputStream() { public void write(int b) {} } );
    }

    public static void main(String[] args) throws IOException {

        int N = Integer.MAX_VALUE;
        //int N = readIntArray()[0];

        for(int i = 0; i < N; i++) {
            boolean b = instance.main();
            if (!b)
                break;
        }

        instance.systemin.close();
    }


}
