import java.util.*;
 
public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] array = new int[n];
        for (int i = 0; i < n; i++) {
            array[i] = sc.nextInt();
        }
        int ans = 0;
        for (int i = 0; i < n; i++) {
            while(true) {
                if (array[i] % 2 == 0) {
                    array[i] /= 2;
                    ans++;
                } else {
                    break;
                }
            }
        }
        System.out.println(ans);
    }    
}
