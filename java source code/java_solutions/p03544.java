import java.util.*;
public class Main{
	public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        long a[] = new long[n+1];
        a[0] = 2;
        a[1] = 1;
        for(int i = 2; i<=n; i++){
            a[i] = a[i-1] + a[i-2];
        }
        System.out.println(a[n]);
    }
}
