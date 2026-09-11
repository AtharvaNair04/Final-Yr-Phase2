import java.util.*;
	class Main{
      public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int k = sc.nextInt();
        int x = sc.nextInt();
        int n = 1+x-k;
        for(int i = 0;i < 2*k-1; i++){
          System.out.println(n+i);
        }
      }
    }
