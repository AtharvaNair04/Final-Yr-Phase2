import java.util.*;
public class Main{
	public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        long n = sc.nextLong();
        long m = 1;
        long ans = 0;

        while(m*m<n){
            if(n%m==0){
                if(n/m-1!=m) ans += n/m-1;
            }
            m++;
        }
        System.out.println(ans);
    }
}
