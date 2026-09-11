// "static void main" must be defined in a public class.
import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String str = scan.nextLine();
        int[] sign = new int[4];
        for(int i = 0; i < Math.pow(4,2); i++){
            for(int j = 0; j < 4; j++){
                if((i>>j & 1) == 1){sign[j] = 1;}
                else{sign[j] = -1;}
            }
            int ans = sign[0] * (str.charAt(0)-'0') + 
                sign[1] * (str.charAt(1)-'0') + 
                sign[2] * (str.charAt(2)-'0') + 
                sign[3] * (str.charAt(3)-'0');
            if(ans == 7){
                String head = new String();
                if(str.charAt(0)-'0' != 0){
                    head = cv(sign[0]) == "+"?"":"-";
                }
                System.out.println(head+(str.charAt(0)-'0')
                                  +cv(sign[1])+(str.charAt(1)-'0')
                                  +cv(sign[2])+(str.charAt(2)-'0')
                                  +cv(sign[3])+(str.charAt(3)-'0') +"=7");
                break;
            }
        }
        
    }
    private static String cv(int a){
            if(a == 1){return "+";}
            else{return "-";}
        }
}
