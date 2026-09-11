import java.util.*;
public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        String S = sc.next();
        int cnt = 0;
        int y = 0;
        int n = 0;
        for(int i = 0;i < S.length();i++){
            for(int j = 0;j < S.length(); j++){
                if(S.charAt(i) == S.charAt(j)){
                    cnt++;
                }
            }
            if(cnt % 2 == 0){
                y++;
            }else{
                n++;
            } 
        }
        if(n == 0){
            System.out.println("Yes");
        }else{
            System.out.println("No");
        }
        
    }
}
