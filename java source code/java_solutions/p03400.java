import java.util.*;

public class Main{
      public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);
            int people= sc.nextInt();
            int days = sc.nextInt();
            int rest_cho = sc.nextInt();
            int ans = 0;
            for(int i = 0; i < people; i++){
              int eat = sc.nextInt();
              for(int j = 1; j <= days;){
      			ans++;
                j += eat;
            }
            }
        	
            System.out.println(ans + rest_cho);
      }
}
