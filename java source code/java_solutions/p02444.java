import java.util.Scanner;
public class Main{
    public static void rotate(long a[], int b, int m, int e){
        long A[] = new long[e-b];
        for(int i = 0 ; i < e-b ; i++)
            A[i] = a[b + (m-b+i)%(e-b)];
        for(int i = 0 ; i < e-b ; i++)
            a[b+i] = A[i];
    }
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        long a[] = new long[n];
        for(int i = 0 ; i < n ; i++)
            a[i] = sc.nextLong();
        int q = sc.nextInt();
        for(int i = 0 ; i < q ; i++){
            int b = sc.nextInt();
            int m = sc.nextInt();
            int e = sc.nextInt();
            rotate(a,b,m,e);
        }
        for(int i = 0 ; i < n-1 ; i++)
            System.out.print(a[i] + " ");
        System.out.println(a[n-1]);
    }
}
