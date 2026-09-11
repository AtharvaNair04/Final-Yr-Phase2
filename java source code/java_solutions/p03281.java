import java.util.*;
class Main{
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int ans = 0;
        int[] a = new int[n+1];
        for(int i = 1; i <= n; i+=2){
            for(int j = 1; j <= n/2; j++){
                if(i%j==0){a[i]++;}
            }
            if(a[i] == 7){ans++;}
        }
        System.out.println(ans);
    }
}
