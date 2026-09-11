import java.util.*;

public class Main{
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);

    long pre_total = 0;
    long total = 0;
    int mojisuu = 0;

    long n = sc.nextLong();
    StringBuilder val = new StringBuilder();


    while(n>0){
      val.append(alpha((int)((n-1)%26)));
      n = (n-1) /26;
    }




      //0x61 = ９７ = a

      System.out.println(val.reverse());
    }








  static char alpha(int n){
      return (char)(n+97);
  }
}
