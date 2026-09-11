import java.math.BigDecimal;
import java.util.*;

public class Main {

    Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        new Main().run();
    }

    void run() {

        int n = sc.nextInt();
        int[][] arr = new int[n][2];

        Data[] data = new Data[n];
        for (int i=0; i<n; i++) {
//            arr[i][0] = sc.nextInt();
//            arr[i][1] = sc.nextInt();
            data[i] = new Data(arr[i][0], arr[i][1]);
        }

        int[] l = new int[n];
        int[] r = new int[n];
        for (int i=0; i<n; i++) {
            l[i] = sc.nextInt();
            r[i] = sc.nextInt();
        }

        Arrays.sort(l);
        Arrays.sort(r);

//        System.out.println(data[0].b);

        if (n%2 == 1) {
            System.out.println(r[n/2]-l[n/2]+1);
            return;
        }

        int min = l[n/2-1] + l[n/2];


//        Data2[] data2s = new Data2[n];
//        for (int i=0; i<n; i++) {
//            data2s[i] = new Data2(arr[i][0], arr[i][1]);
//        }

//        Arrays.sort(data2s);
        int max = r[n/2-1] + r[n/2];

        System.out.println((max-min+1));
    }
    public static class Data implements Comparable<Data> {
        int a, b;
        public Data(int a, int b) {
            this.a = a;
            this.b = b;
        }
        public int compareTo(Data other) {
            if (this.a == other.a) {
                return this.b - other.b;
            } else {
                return this.a - other.a;
            }
        }
    }
    public static class Data2 implements Comparable<Data2> {
        int a, b;
        public Data2(int a, int b) {
            this.a = a;
            this.b = b;
        }
        public int compareTo(Data2 other) {
            if (this.b == other.b) {
                return this.a - other.a;
            } else {
                return this.b - other.b;
            }
        }
    }
}
