import java.util.*;
public class Main{
	public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int y = sc.nextInt();
        for(int a=0; a<=n; a++){
            for(int b=0; b<=n-a; b++){
                int c = n-a-b;
                if(y == 10000*a + 5000*b + 1000*(c)){
                    System.out.println(a + " " + b + " " + c);
                    return;
                }
            }
        }
        System.out.println("-1 -1 -1");
    }
}
