import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

class Main{
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int min=Integer.MAX_VALUE;
        int[][] p=new int[n][2];
        for(int i=0;i<n;i++) {
            p[i][0]=sc.nextInt();
            p[i][1]=sc.nextInt();
        }
        Arrays.sort(p, new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return a[0]-b[0];
            }
        });
        for(int i=1;i<n;i++) {
            min=Math.min(min, (p[i][0]-p[i-1][0])*(p[i][0]-p[i-1][0])+(p[i][1]-p[i-1][1])*(p[i][1]-p[i-1][1]));
        }
        Arrays.sort(p, new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return a[1]-b[1];
            }
        });
        for(int i=1;i<n;i++) {
            min=Math.min(min, (p[i][0]-p[i-1][0])*(p[i][0]-p[i-1][0])+(p[i][1]-p[i-1][1])*(p[i][1]-p[i-1][1]));
        }
        System.out.println(min);
    }
}
