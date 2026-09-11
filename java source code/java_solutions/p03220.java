import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int N = s.nextInt();
        int T = s.nextInt();
        int A = s.nextInt();
        int ans = 0;
        double nearlest = 10000;
        for(int i = 0; i < N; i++){
            double n = Math.abs(A - (T - s.nextInt() * 0.006)); //TT忘れちまったぜ☆
            if(n < nearlest){
                ans = i + 1;
                nearlest = n;
            }
        }
        System.out.println(ans);
    }
}
