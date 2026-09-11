import java.util.*;

class Main{
  static int A;
  public static void main(String[] args){
  	Scanner scan = new Scanner(System.in);
    int[] map = {1,9,7,4}; 
    boolean[] visit = new boolean[4];
    boolean flag = false;
    for(int i=0;i<4;i++){
      int A = scan.nextInt();
      for(int j=0;j<4;j++){
        if(A == map[j]){
         visit[j] = true;
        }
      }
    }
    for(int i=0;i<4;i++){
      if(!visit[i])
        flag = true;
    }
    if(flag)
        System.out.println("NO");
    else
        System.out.println("YES");
    
  }
}
