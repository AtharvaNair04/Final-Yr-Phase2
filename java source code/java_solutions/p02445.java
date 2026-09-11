import java.util.Scanner;
public class Main{
    public static void swap(long a[], int b, int e, int t){
        for(int i = 0 ; i < e-b ; i++){
            long tmp = a[b+i];
            a[b+i] = a[t+i];
            a[t+i] = tmp; 
        }
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
            int e = sc.nextInt();
            int t = sc.nextInt();
            swap(a,b,e,t);
        }
        for(int i = 0 ; i < n-1 ; i++)
            System.out.print(a[i] + " ");
        System.out.println(a[n-1]);
    }
}
