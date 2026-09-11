import java.util.*;

class Main{
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    //高橋君のモンスター
    int A = sc.nextInt(); //体力
    int B = sc.nextInt(); //攻撃力
    //青木君のモンスター
    int C = sc.nextInt(); //体力
    int D = sc.nextInt(); //攻撃力
    //高橋君→青木君→・・・と攻撃を繰り返す
    int count = 0;
    do{
      if (count % 2 == 0){
      	C = C - B;
        //System.out.println( "青木君の体力=" + C);
      }
      else{
        A = A - D;
        //System.out.println( "高橋君の体力=" + A);
      }
      count++ ;
    }while(A*C > 0);
    //高橋君が勝てばYes、青木君が勝てばNo.
    if (A <= 0)
      System.out.println("No");
    else{
     if (C <= 0) 
      System.out.println("Yes");
     else
       System.out.println("ERROR");
    }
    
  }
}
