import java.util.*;

public class Main{
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    int n = sc.nextInt();
    String[] array = new String[n];
    array = sc.next().split("");

    int count = 1;
    int i = 0;

    if(n==1){
      System.out.println("1");
    }else{
      while(true){
        if(!(array[i].equals(array[i+1]))){
          count +=1;
        }
        if(i==n-2){break;}
        i += 1;
      }
      System.out.println(count);
    }



  }
}
