import java.io.*;
import java.util.Scanner;
 
public class Main {
    public static double btsp(int[] x, int[] y) {
        int n = x.length;

        double[][] aux = new double[n][n];
        for (int j = 1; j < n; j++) {
            aux[0][j] = aux[0][j-1] + distance(x[j-1], y[j-1], x[j], y[j]);
        }

        for (int i = 1; i < n; i++) {
            for (int j = i; j < n; j++) {
                if (i == j) {
                    aux[i][j] = aux[i-1][j] + distance(x[i-1], y[i-1], x[j], y[j]);
                } else if (i == j - 1) {
                    double min = Double.POSITIVE_INFINITY;
                    for (int k = 0; k < j; k++) {
                        min = Math.min(min, aux[k][j-1] + distance(x[k], y[k], x[j], y[j]));
                    }
                    aux[i][j] = min;
                } else {
                    aux[i][j] = aux[i][j-1] + distance(x[j-1], y[j-1], x[j], y[j]);
                }
            }
        }
        return aux[n-1][n-1];
    }

    public static double distance(int x1, int y1, int x2, int y2) {
        return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
    }

    public static void main (String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int x[] = new int[n];
        int y[] = new int[n];

        for(int i = 0; i < n; i++) {           
            x[i] = sc.nextInt();
            y[i] = sc.nextInt();
        }

        System.out.println(btsp(x, y));
    }
}
