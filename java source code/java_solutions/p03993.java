import java.util.*;

public class Main {
    
    public static void main(String[] args) {
       Scanner scanner = new Scanner(System.in);
       
       int N = scanner.nextInt();
       int[] ar1 = new int[N];
       
       for (int i=0;i<N;i++){
        ar1[i] = scanner.nextInt();
       }
       
       int pairCount = 0;       
       
       for (int i=1;i<=N;i++){
           int val = ar1[i-1];
           if (ar1[val-1] == i){
               pairCount++;
           }
       }       
      System.out.print(pairCount/2);
    }
}
