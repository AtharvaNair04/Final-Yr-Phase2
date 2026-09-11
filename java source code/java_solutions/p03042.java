import java.util.*;

public class Main{
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    char[] c = sc.next().toCharArray();
    int number1 = Character.getNumericValue(c[0]) * 10 + Character.getNumericValue(c[1]);
    int number2 = Character.getNumericValue(c[2]) * 10 + Character.getNumericValue(c[3]);

    //System.out.println(number1);
    //System.out.println(number2);

    int count1 = 0;
    int count2 = 0;

    if(number1<=12 && number1>0){
      count1 += 1;
    }
    if(number2<=12 && number2>0){
      count2 += 1;
    }
    if(count1==1 && count2==1){
      System.out.println("AMBIGUOUS");
    }
    if(count1==0 && count2==1){
      //System.out.println(c);
      System.out.println("YYMM");
    }
    if(count1==1 && count2==0){
      //System.out.println(c[2]+""+c[3]+""+c[0]+""+c[1]);
      System.out.println("MMYY");
    }
    if(count1==0 && count2==0){
      System.out.println("NA");
    }


  }
}
