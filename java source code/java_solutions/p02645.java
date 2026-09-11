import java.util.Scanner;
import java.util.ArrayList;
 
public class Main{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String S = sc.next();
        for(int i=0;i<3;i++){
            char c = S.charAt(i);
            System.out.print(c);
        }
    }
}
