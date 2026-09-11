import java.util.Scanner;
public class Main{
    public static boolean search(long a[], int k){
        int start = 0;
        int end = a.length-1;
        while(true){
            if(a[start] == k || a[end] == k)
                return true;
            else{
                int mid = (start + end)/2;
                if(a[mid] == k)
                    return true;
                else if(a[mid] > k)
                    end = mid;
                else
                    start = mid;
            }
            if(start+1 >= end)
                return false;
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
            int k = sc.nextInt();
            if(search(a,k))
                System.out.println(1);
            else
                System.out.println(0);
        }
    }
}
