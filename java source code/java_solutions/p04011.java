import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int K = sc.nextInt();
        int X = sc.nextInt();
        int Y = sc.nextInt();
        
        int total = 0;
        for(int i = 1;i <= N;i++){
            if(i <= K){
                total += X;
            }else{
                total += Y;
            }
        }
        System.out.println(total);
    }
}
