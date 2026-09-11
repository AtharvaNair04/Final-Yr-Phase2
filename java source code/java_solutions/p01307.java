import java.util.*;

public class Main{

    void solve(){
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        while(n-->0){
            String s = sc.next();
            int ans = 0;
            while(s.length()>=2){
                ans = 1-ans;
                int sum = (int)(s.charAt(0)-'0') + (int)(s.charAt(1)-'0');
                if(s.length()>=3) s = sum + s.substring(2,s.length());
                else s = Integer.toString(sum);
            }

            if(ans==0) System.out.println("Audrey wins.");
            else System.out.println("Fabre wins.");
        }
    }

    public static void main(String[] args){
        new Main().solve();
    }
}
