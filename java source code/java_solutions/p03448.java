import java.util.*;
public class Main{
    public static void main(String[]args){
        Scanner sc = new Scanner(System.in);
        int A = sc.nextInt();
        int B = sc.nextInt();
        int C = sc.nextInt();
        int X = sc.nextInt();
        
        int cnt = 0;
        for(int i=0; i<=A; i++){
            for(int j=0; j<=B; j++){
                int c = (X - i*500 - j*100)/50;
                if(c>=0 && c<=C){
                    cnt++;
                }
            }
        }
        System.out.println(cnt);
    }
}
