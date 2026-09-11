// "static void main" must be defined in a public class.
import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String str = scan.nextLine();
        //要素数
        long n = str.length()-1;
        //最終的な合計を格納する値
        long total = 0;
        String hi = new String();
        
        //System.out.println(n);
        //ビット全探索で探索を行う
        for(long i = 0; i < Math.pow(2,n); i++){
            //System.out.println(i);
            String ans = new String();
            hi = new String();
            for(int j = 0; j <= n; j++){
                ans += Character.toString(str.charAt(j));

                //+を挟むタイミングでans文字列に格納してある数だけ合計に足す
                if((1 & i >> j) == 1){
                    total += Long.parseLong(ans);
                    //System.out.println(ans);
                    hi += (ans + "+");
                    ans = new String();
                    }
                }
            hi += (ans);
            ///System.out.println(hi);
            //System.out.println(ans);
            total += Long.parseLong(ans);
            }
            System.out.println(total);
        }
        
    }
