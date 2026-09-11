import java.util.Arrays;
import java.util.Scanner;

//155
class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt(), dp[] = new int[n], length = 0;
        Arrays.fill(dp, Integer.MAX_VALUE);
        for (int i = 0; i < n; i++) {
            int tmp= sc.nextInt();
            if(i == 0){
                dp[0] = tmp;
            }else{
                if(dp[length] < tmp){
                    length++;
                    dp[length] = tmp;
                }else{
                    for(int j = 0; j <= length; j++){
                        if(dp[j] >= tmp){
                            dp[j] = tmp;
                            break;
                        }
                    }
                }
            }
        }
        System.out.println(length+1);
    }
}
