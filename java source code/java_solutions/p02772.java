import java.util.*;

public class Main{
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    int n = sc.nextInt();
    boolean flag1 =  false;
    int num;
    int count = 0;
    int[] array = new int[n];

    for(int i=0; i<n; i++){
      num = sc.nextInt();
      if(num%2==0){
        array[count] = num;
        count++;
      }
    }



    for(int i=0; i<count; i++){
      if((array[i]%3!=0)&&(array[i]%5!=0)){
        System.out.println("DENIED");
        return;
      }
    }

    System.out.println("APPROVED");




  }
}
