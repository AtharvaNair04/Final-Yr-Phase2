import java.util.*;
//System.out.println("")

public class Main{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();
        int cnt = 0;
        for(int i=0; i<n; i++){
            int a = sc.nextInt();
            int b =Math.min(a*2,(k-a)*2);
            if(b<0){
                cnt -= b;
            }else{
                cnt += b;
            }
        }
        System.out.println(cnt);
    }
}
