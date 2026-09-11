import java.util.*;

public class Main{
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);

    String[] s = sc.next().split("");
    int l =  s.length;
    int count =0;
    for(int i=0; i<l; i++){
      if(s[i].equals("U")||s[i].equals("D")){
        count +=1;
        continue;
      }


      if(i%2==0){
        if(s[i].equals("R")){
          count += 1;
        }
      }else{
        if(s[i].equals("L")){
          count += 1;
        }
      }
    }
    if(count == l){
      System.out.println("Yes");
    }else{
      System.out.println("No");
    }




  }
}
