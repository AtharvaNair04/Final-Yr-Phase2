import java.util.*;
 
public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();
        long[] x = new long[n];
        long[] y = new long[n];
        long[] z = new long[n];
        long xa, ya, za;
        boolean[] f = new boolean[n];
        int[][] s = {{1,1,1}, {1,1,-1}, {1,-1,1}, {1,-1,-1}, {-1,1,1}, {-1,1,-1}, {-1,-1,1}, {-1,-1,-1}};
        for (int i = 0; i < n; i++) {
            x[i] = sc.nextLong();
            y[i] = sc.nextLong();
            z[i] = sc.nextLong();
        }
        long[] a = new long[8];
        
        
        for (int i = 0; i < 8; i++) {
            xa = 0;
            ya = 0;
            za = 0;
            for (int j = 0; j < n; j++) {
                f[j] = true;
            }
            for (int j = 0; j < m; j++) {
                long xb = xa;
                long yb = ya;
                long zb = za;
                int p = -1;
                for (int k = 0; k < n; k++) {
                    long xc = xa + x[k];
                    long yc = ya + y[k];
                    long zc = za + z[k];
                    if (f[k]) {
                        if (xb*s[i][0] + yb*s[i][1] + zb*s[i][2] < xc*s[i][0] + yc*s[i][1] + zc*s[i][2] || (xa == xb && ya == yb && za == zb)) {
                            p = k;
                            xb = xc;
                            yb = yc;
                            zb = zc;
                        }
                    }
                }
                f[p] = false;
                xa += x[p];
                ya += y[p];
                za += z[p];
            }
            a[i] = Math.abs(xa) + Math.abs(ya) + Math.abs(za);
        }
        
        
        Arrays.sort(a);
        System.out.println(a[7]);
    }    
}
