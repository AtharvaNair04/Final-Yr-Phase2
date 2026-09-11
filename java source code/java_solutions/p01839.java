import java.util.*;

class Main{           
  
   public static void main(String[] args){
         Scanner sc = new Scanner(System.in);  
         
         int N = sc.nextInt();
          String S[] = new String[N];
          
          for(int i = 0;i < N;i++){
              S[i] = sc.next();
          }
         
          int countA = 0;
          boolean ok = true;
          
          for(int i = 0;i < S.length;i++){
              if(S[i].charAt(0) == 'A'){
                  countA++;
              }
              else{
                  if(countA == 0){
                      ok = false;
                  }
                  countA--;                  
              }                            
          }
          
          if(countA != 0 || !ok){
              System.out.println("NO");
          }else{
              System.out.println("YES");
          }
          
         
   }         
         
 }
