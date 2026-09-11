import java.util.*;

class Main{           
  
   public static void main(String[] args){
         Scanner sc = new Scanner(System.in);  
       
         HashSet<String> set = new HashSet<String>();
         
         int N = sc.nextInt();
         
         for(int i = 0;i < N;i++){
             set.add(sc.next());
         }
         
         int M = sc.nextInt();
         
         boolean flag = false;
         
         for(int i = 0;i < M;i++){
           String t = sc.next(); 
           if(!set.contains(t)){
               System.out.println("Unknown "  + t);
           }else{
               if(!flag){
                   System.out.println("Opened by " + t);
                   flag = !flag;
               }else{
                   System.out.println("Closed by " + t);
                   flag = !flag;
               }
           }       
         }
       
       
   }     
 }
